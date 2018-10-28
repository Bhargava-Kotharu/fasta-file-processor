package com.embl.ena.platform.bean;

import java.util.Map;

/**
 * Bean to hold the output data
 * 
 * @author bkotharu
 *
 */
public class OutputBean {

    private int threadId;

    private String fileName;

    private long totalSequenceCount;

    private long totalSequenceBaseCount;

    private Map<Character, Integer> sequenceBaseMap;

    public int getThreadId() {
	return threadId;
    }

    public String getFileName() {
	return fileName;
    }

    public long getTotalSequenceCount() {
	return totalSequenceCount;
    }

    public long getTotalSequenceBaseCount() {
	return totalSequenceBaseCount;
    }

    public Map<Character, Integer> getSequenceBaseMap() {
	return sequenceBaseMap;
    }

    public void setThreadId(int threadId) {
	this.threadId = threadId;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public void setTotalSequenceCount(long totalSequenceCount) {
	this.totalSequenceCount = totalSequenceCount;
    }

    public void setTotalSequenceBaseCount(long totalSequenceBaseCount) {
	this.totalSequenceBaseCount = totalSequenceBaseCount;
    }

    public void setSequenceBaseMap(Map<Character, Integer> sequenceBaseMap) {
	this.sequenceBaseMap = sequenceBaseMap;
    }

    @Override
    public String toString() {
	return "OutputBean [threadId=" + threadId + ", fileName=" + fileName + ", totalSequenceCount="
		+ totalSequenceCount + ", totalSequenceBaseCount=" + totalSequenceBaseCount + ", sequenceBaseMap="
		+ sequenceBaseMap + "]";
    }

}
