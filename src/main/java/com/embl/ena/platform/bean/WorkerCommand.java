package com.embl.ena.platform.bean;

/**
 * Worker Command for the threads to process
 * 
 * @author bkotharu
 *
 */
public class WorkerCommand {

    private int order;
    private String fileName;

    public WorkerCommand(int order, String fileName) {
	this.order = order;
	this.fileName = fileName;
    }

    public int getOrder() {
	return order;
    }

    public String getFileName() {
	return fileName;
    }

    @Override
    public String toString() {
	return "WorkerCommand [order=" + order + ", fileName=" + fileName + "]";
    }

}
