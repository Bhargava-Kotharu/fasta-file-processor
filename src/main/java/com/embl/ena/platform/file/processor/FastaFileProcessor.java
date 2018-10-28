package com.embl.ena.platform.file.processor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;

import com.embl.ena.platform.bean.OutputBean;
import com.embl.ena.platform.bean.SequenceCommand;
import com.embl.ena.platform.bean.WorkerCommand;
import com.embl.ena.platform.executor.FastaFileExecutorWorker;
import com.embl.ena.platform.file.writer.Observer;
import com.embl.ena.platform.file.writer.SequenceWriter;

/**
 * File processor implementation to process FASTA files
 * 
 * @author bkotharu
 *
 */
public class FastaFileProcessor implements FileProcessor, Observer {

    private static final Logger LOGGER = Logger.getLogger(FastaFileProcessor.class);

    private boolean canProceed = false;

    @Override
    public OutputBean processFile(int threadId, WorkerCommand command, SequenceWriter sequenceWriter)
	    throws IOException {
	BufferedReader reader = this.readFile(threadId, command.getFileName());
	OutputBean output = new OutputBean();
	output.setFileName(command.getFileName());
	Map<Character, Integer> sequenceBaseMap = new TreeMap<>();
	String line;
	sequenceWriter.register(this);

	output.setThreadId(threadId);
	int lineCounter = 0;
	while (reader.ready() && (line = reader.readLine()) != null) {

	    if (line.startsWith(">")) {
		output.setTotalSequenceCount(output.getTotalSequenceCount() + 1);
	    } else {
		SequenceCommand seq = new SequenceCommand(command.getOrder(), line, ++lineCounter);
		LOGGER.debug("Thread #" + threadId + " Invoking Sequence Writer : " + seq);
		this.canProceed = false;
		sequenceWriter.writeSequenceToFile(seq);
		output.setTotalSequenceBaseCount(output.getTotalSequenceBaseCount() + line.length());

		for (char c : line.toCharArray()) {
		    if (sequenceBaseMap.containsKey(c)) {
			sequenceBaseMap.put(c, sequenceBaseMap.get(c) + 1);
		    } else {
			sequenceBaseMap.put(c, 1);
		    }
		}
		while (!this.canProceed) {
		    try {
			Thread.sleep(1);
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
	    }
	}
	output.setSequenceBaseMap(sequenceBaseMap);
	sequenceWriter.unregister(this);
	return output;
    }

    private BufferedReader readFile(int threadId, String fileName) throws IOException {

	GZIPInputStream gZipInputStream = new GZIPInputStream(new FileInputStream(fileName));
	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gZipInputStream));

	return bufferedReader;

    }

    @Override
    public void update() {
	this.canProceed = true;
    }

}
