package com.embl.ena.platform.bean;

/**
 * Command to write Sequence to Output file
 * 
 * @author bkotharu
 *
 */
public class SequenceCommand {

    private int fileOrder;

    private String sequence;

    private int lineNumber;

    public SequenceCommand(int fileOrder, String sequence, int lineNumber) {

	this.fileOrder = fileOrder;
	this.sequence = sequence;
	this.lineNumber = lineNumber;
    }

    public int getFileOrder() {
	return fileOrder;
    }

    public String getSequence() {
	return sequence;
    }

    public int getLineNumber() {
	return lineNumber;
    }

    public void setFileOrder(int fileOrder) {
	this.fileOrder = fileOrder;
    }

    public void setSequence(String sequence) {
	this.sequence = sequence;
    }

    public void setLineNumber(int lineNumber) {
	this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
	return "SequenceCommand [fileOrder=" + fileOrder + ", lineNumber=" + lineNumber + "]";
    }

}
