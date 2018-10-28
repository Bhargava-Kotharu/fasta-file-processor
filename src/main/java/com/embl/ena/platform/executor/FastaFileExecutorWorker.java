package com.embl.ena.platform.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.embl.ena.platform.bean.OutputBean;
import com.embl.ena.platform.bean.WorkerCommand;
import com.embl.ena.platform.file.processor.FastaFileProcessor;
import com.embl.ena.platform.file.processor.FileProcessor;
import com.embl.ena.platform.file.writer.SequenceWriter;

/**
 * Worker Thread for processing FASTA files
 * 
 * @author bkotharu
 *
 */
public class FastaFileExecutorWorker implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(FastaFileExecutorWorker.class);

    private int threadId;

    private BlockingQueue<WorkerCommand> queue;

    private CopyOnWriteArrayList<OutputBean> outputList;

    private SequenceWriter sequenceWriter;

    public FastaFileExecutorWorker(int threadId, BlockingQueue<WorkerCommand> queue,
	    CopyOnWriteArrayList<OutputBean> outputList, SequenceWriter sequenceWriter) {
	this.threadId = threadId;
	this.queue = queue;
	this.outputList = outputList;
	this.sequenceWriter = sequenceWriter;
    }

    @Override
    public void run() {
	LOGGER.info("Processing Thread: " + threadId);

	while (!Thread.currentThread().isInterrupted()) {

	    try {
		WorkerCommand workerCommand = this.queue.poll(2, TimeUnit.SECONDS);

		if (workerCommand == null || workerCommand.getFileName() == null) {
		    LOGGER.info("Thread #" + threadId + ":Empty worker command reveived. Nothing to process.");
		    break;
		}
		LOGGER.info("Thread #" + threadId + " Received: " + workerCommand);
		FileProcessor fileProcessor = new FastaFileProcessor();

		long startTime = System.currentTimeMillis();
		OutputBean outputBean = fileProcessor.processFile(threadId, workerCommand, sequenceWriter);
		long endTime = System.currentTimeMillis();

		outputList.add(outputBean);
		LOGGER.info("Thread #" + threadId + ": Processed file " + outputBean.getFileName() + " in "
			+ (endTime - startTime) + " MS");
	    } catch (Exception e) {
		e.printStackTrace();
		LOGGER.error(threadId + ":Error occured while processing: ", e);
	    }
	}
    }
}
