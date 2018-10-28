package com.embl.ena.platform.file.writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import com.embl.ena.platform.bean.SequenceCommand;
import com.embl.ena.platform.constants.ApplicationConstants;

/**
 * Writer to write concatenated Sequences to the output file.
 * 
 * @author bkotharu
 *
 */
public class SequenceWriter implements Subject {

    private static final Logger LOGGER = Logger.getLogger(SequenceWriter.class);

    private List<Observer> observers;
    private final Object MUTEX = new Object();
    private FileWriter outputWriter;
    private int counter = 0;
    CopyOnWriteArrayList<SequenceCommand> sequencesList;

    public SequenceWriter(int fileCount) throws IOException {
	observers = new ArrayList<>();
	this.outputWriter = new FileWriter(new File(ApplicationConstants.OUTPUT_SEQUENCE_FILE_NAME));
	sequencesList = new CopyOnWriteArrayList<>();
    }

    public synchronized void writeSequenceToFile(SequenceCommand sequence) throws IOException {
	if (sequence != null) {
	    sequencesList.add(sequence);
	}
	if (sequencesList.size() == observers.size()) {
	    writeToFile();
	    synchronized (this) {
		sequencesList.clear();
		this.notifyObservers();
	    }
	}
    }

    private void writeToFile() throws IOException {
	Collections.sort(sequencesList, (p1, p2) -> p1.getFileOrder() - p2.getFileOrder());
	StringBuilder b = new StringBuilder();
	b.append(ApplicationConstants.SEQUENCE_PREFIX_KEY + (++counter)).append(System.lineSeparator());
	sequencesList.stream().map(SequenceCommand::getSequence).collect(Collectors.toList()).forEach(b::append);
	b.append(System.lineSeparator());
	outputWriter.write(b.toString());
	outputWriter.flush();
    }

    public boolean canProceedToNextLine() {
	return sequencesList.isEmpty();
    }

    @Override
    public void register(Observer obj) {
	if (obj == null)
	    throw new NullPointerException("Observer can not be null!");
	synchronized (MUTEX) {
	    if (!observers.contains(obj))
		observers.add(obj);
	}
    }

    @Override
    public synchronized void unregister(Observer obj) {
	synchronized (MUTEX) {
	    observers.remove(obj);
	    try {
		writeSequenceToFile(null);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }

    @Override
    public void notifyObservers() {
	for (Observer obj : this.observers) {
	    obj.update();
	}
    }

    public void compressSequenceTxtFile(String file, String gzipFile) throws IOException {
	FileInputStream fileInputStream = null;
	FileOutputStream fileOutputStream = null;
	GZIPOutputStream gzipOutputStream = null;
	try {
	    LOGGER.info(String.format("Compressing file %s as gzip file %s", file, gzipFile));
	    fileInputStream = new FileInputStream(file);
	    fileOutputStream = new FileOutputStream(gzipFile);
	    gzipOutputStream = new GZIPOutputStream(fileOutputStream);
	    byte[] buffer = new byte[1024];
	    int len;
	    while ((len = fileInputStream.read(buffer)) != -1) {
		gzipOutputStream.write(buffer, 0, len);
	    }
	    // Delete SEQUENCE.TXT
	    new File(file).delete();
	    LOGGER.info(String.format("Compression for file %s as gzip file %s is completed", file, gzipFile));
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    if (gzipOutputStream != null)
		gzipOutputStream.close();
	    if (fileOutputStream != null)
		fileOutputStream.close();
	    if (fileInputStream != null)
		fileInputStream.close();
	}

    }
}
