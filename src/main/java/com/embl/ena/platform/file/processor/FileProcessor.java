package com.embl.ena.platform.file.processor;

import java.io.IOException;

import com.embl.ena.platform.bean.OutputBean;
import com.embl.ena.platform.bean.WorkerCommand;
import com.embl.ena.platform.file.writer.SequenceWriter;

/**
 * File Processor
 * 
 * @author bkotharu
 *
 */
public interface FileProcessor {

    public OutputBean processFile(int threadId, WorkerCommand command, SequenceWriter sequenceWriter)
	    throws IOException;
}
