package com.embl.ena.platform.file.writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.embl.ena.platform.bean.OutputBean;
import com.embl.ena.platform.constants.ApplicationConstants;
import com.embl.ena.platform.file.processor.FastaFileProcessor;

/**
 * Writer to write Report file to the output
 * 
 * @author bkotharu
 *
 */
public class FastaFileResponseWriter implements ResponseWriter {

    private static final Logger LOGGER = Logger.getLogger(FastaFileResponseWriter.class);

    @Override
    public void write(CopyOnWriteArrayList<OutputBean> outputList) {
	long totalFilesCount = outputList.size();
	long totalSequenceCount = 0;
	long totalSequenceBaseCount = 0;
	Map<Character, Integer> sequenceBaseMap = new TreeMap<>();

	for (OutputBean output : outputList) {
	    totalSequenceCount += output.getTotalSequenceCount();
	    totalSequenceBaseCount += output.getTotalSequenceBaseCount();

	    output.getSequenceBaseMap().forEach((k, v) -> {
		if (sequenceBaseMap.containsKey(k)) {
		    sequenceBaseMap.put(k, sequenceBaseMap.get(k) + v);
		} else {
		    sequenceBaseMap.put(k, v);
		}
	    });

	    LOGGER.info("Output::" + output);
	}

	StringBuilder fileOutput = new StringBuilder(50);
	fileOutput.append(ApplicationConstants.FILE_CNT_KEY).append(totalFilesCount).append(System.lineSeparator());
	fileOutput.append(ApplicationConstants.SEQUENCE_CNT_KEY).append(totalSequenceCount)
		.append(System.lineSeparator());
	fileOutput.append(ApplicationConstants.BASE_CNT_KEY).append(totalSequenceBaseCount)
		.append(System.lineSeparator());

	sequenceBaseMap.forEach((k, v) -> {
	    fileOutput.append(k).append("    ").append(v).append(System.lineSeparator());
	});

	LOGGER.info(fileOutput.toString());

	Path path = Paths.get(ApplicationConstants.OUTPUT_FILE_NAME);

	byte[] bytes = fileOutput.toString().getBytes();
	try {
	    Files.write(path, bytes);
	} catch (IOException e) {
	    LOGGER.error("Error writing output to file!", e);
	}
    }

}
