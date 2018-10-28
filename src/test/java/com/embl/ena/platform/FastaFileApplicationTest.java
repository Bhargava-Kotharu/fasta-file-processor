package com.embl.ena.platform;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.embl.ena.platform.constants.ApplicationConstants;

import junitx.framework.FileAssert;

public class FastaFileApplicationTest {

    @Test(expected = IllegalArgumentException.class)
    public void testMain_WithNoAgs() throws FileNotFoundException {

	String[] args = {};
	FastaFileApplication.main(args);
    }

    // @Before
    public void setup() {
	System.out.println("Deleting files before test!");
	File reportFile = new File(ApplicationConstants.OUTPUT_FILE_NAME);
	File sequenceFile = new File(ApplicationConstants.OUTPUT_SEQUENCE__COMPRESSED_FILE_NAME);
	reportFile.delete();
	sequenceFile.delete();
    }

    // @AfterClass
    public static void cleanup() {
	System.out.println("Deleting files after all tests are executed!");
	File reportFile = new File(ApplicationConstants.OUTPUT_FILE_NAME);
	File sequenceFile = new File(ApplicationConstants.OUTPUT_SEQUENCE__COMPRESSED_FILE_NAME);
	reportFile.delete();
	sequenceFile.delete();
    }

    @Test
    public void testMain_WithSingleFile() throws FileNotFoundException {

	String[] args = { "src/test/resources/1.gz" };
	FastaFileApplication.main(args);

	File expectedReportFile = new File("src/test/resources/expected/SINGLE_FILE_REPORT.TXT");

	File actualReportFile = new File(ApplicationConstants.OUTPUT_FILE_NAME);
	assertTrue(actualReportFile.exists());
	FileAssert.assertEquals(expectedReportFile, actualReportFile);

	File sequenceFile = new File(ApplicationConstants.OUTPUT_SEQUENCE__COMPRESSED_FILE_NAME);
	assertTrue(sequenceFile.exists());

    }

    @Test
    public void testMain_WithMultipleFile() throws FileNotFoundException {

	String[] args = { "src/test/resources/1.gz", "src/test/resources/2.gz" };
	FastaFileApplication.main(args);

	File expectedReportFile = new File("src/test/resources/expected/MULTIPLE_FILE_REPORT.TXT");

	File actualReportFile = new File(ApplicationConstants.OUTPUT_FILE_NAME);
	assertTrue(actualReportFile.exists());
	FileAssert.assertEquals(expectedReportFile, actualReportFile);

	File sequenceFile = new File(ApplicationConstants.OUTPUT_SEQUENCE__COMPRESSED_FILE_NAME);
	assertTrue(sequenceFile.exists());

    }
}
