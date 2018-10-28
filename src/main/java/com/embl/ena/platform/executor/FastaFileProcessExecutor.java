package com.embl.ena.platform.executor;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.embl.ena.platform.bean.OutputBean;
import com.embl.ena.platform.bean.WorkerCommand;
import com.embl.ena.platform.constants.ApplicationConstants;
import com.embl.ena.platform.file.writer.FastaFileResponseWriter;
import com.embl.ena.platform.file.writer.ResponseWriter;
import com.embl.ena.platform.file.writer.SequenceWriter;

/**
 * Executor Service implementation for processing FASTA files in parallel
 * 
 * @author bkotharu
 *
 */
public class FastaFileProcessExecutor implements ProcessExecutor {

    private static final Logger LOGGER = Logger.getLogger(FastaFileProcessExecutor.class);

    private BlockingQueue<WorkerCommand> queue;
    private ExecutorService executorService;
    private CopyOnWriteArrayList<OutputBean> outputList;

    public FastaFileProcessExecutor() {
	outputList = new CopyOnWriteArrayList<>();
    }

    @Override
    public void execute(String args[]) throws InterruptedException, ExecutionException {
	long startTime = System.currentTimeMillis();
	int threadCount = args.length;
	SequenceWriter sequenceWriter = null;
	queue = new ArrayBlockingQueue<>(threadCount);
	executorService = Executors.newFixedThreadPool(threadCount);
	try {
	    sequenceWriter = new SequenceWriter(args.length);
	} catch (IOException e1) {
	    e1.printStackTrace();
	}

	// Create thread pool
	for (int threadId = 1; threadId < threadCount + 1; threadId++) {
	    FastaFileExecutorWorker worker = new FastaFileExecutorWorker(threadId, queue, outputList, sequenceWriter);
	    executorService.submit(worker);
	}

	// Submit worker commands to blocking queue
	int counter = 0;
	for (String fileName : args) {
	    WorkerCommand workerCommand = new WorkerCommand(++counter, fileName);
	    LOGGER.info("Created :" + workerCommand);
	    try {
		queue.put(workerCommand);
	    } catch (InterruptedException e) {
		LOGGER.error("Error while processing file: " + fileName);
		throw e;
	    }
	}

	// Wait until everything is processed
	while (outputList.size() != args.length) {
	    try {
		Thread.sleep(5000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    LOGGER.info("Queue is not empty. Sleeping for more time to finish processing!");
	}
	executorService.shutdown();

	long endTime = System.currentTimeMillis();
	LOGGER.info("Processed " + args.length + " FASTA files in " + (endTime - startTime) / 1000 + " Seconds");

	// Gather Results and create output file
	ResponseWriter responseWriter = new FastaFileResponseWriter();
	responseWriter.write(outputList);
	try {
	    sequenceWriter.compressSequenceTxtFile(ApplicationConstants.OUTPUT_SEQUENCE_FILE_NAME,
		    ApplicationConstants.OUTPUT_SEQUENCE__COMPRESSED_FILE_NAME);
	} catch (IOException e) {
	    LOGGER.error("Error occured during compressing the output file");
	}

    }

}
