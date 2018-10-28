package com.embl.ena.platform;

import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import com.embl.ena.platform.constants.ApplicationConstants;
import com.embl.ena.platform.executor.FastaFileProcessExecutor;
import com.embl.ena.platform.executor.ProcessExecutor;
import com.embl.ena.platform.file.processor.FastaFileProcessor;

/**
 * Entry point for the Application
 * 
 * @author bkotharu
 *
 */
public class FastaFileApplication {

    private static final Logger LOGGER = Logger.getLogger(FastaFileApplication.class);

    public static void main(String[] args) {

	FastaFileApplication application = new FastaFileApplication();

	if (args.length == 0) {
	    throw new IllegalArgumentException(ApplicationConstants.INVALID_ARGS_MESSAGE);
	}

	try {
	    application.processFASTAFiles(args);
	    LOGGER.info("FASTA File Processing completed!");
	} catch (Exception e) {
	    LOGGER.error("Error occured while processing FASTA files", e);
	}

    }

    public void processFASTAFiles(String[] args) throws InterruptedException, ExecutionException {

	ProcessExecutor executor = new FastaFileProcessExecutor();
	executor.execute(args);
    }
}
