package com.embl.ena.platform.constants;

/**
 * Application specific constants
 * 
 * @author bkotharu
 *
 */
public interface ApplicationConstants {

    int MAX_THREAD_COUNT = 5;

    String INVALID_ARGS_MESSAGE = "Please supply atleast one argument for processing.";
    String INVALID_FILE_MESSAGE = "Please supply valid file for processing.";

    String OUTPUT_FILE_NAME = "REPORT.TXT";
    String OUTPUT_SEQUENCE_FILE_NAME = "SEQUENCE.TXT";
    String OUTPUT_SEQUENCE__COMPRESSED_FILE_NAME = "SEQUENCE.FASTA.GZ";

    String FILE_CNT_KEY = "FILE_CNT     ";
    String SEQUENCE_CNT_KEY = "SEQUENCE_CNT     ";
    String BASE_CNT_KEY = "BASE_CNT     ";
    String SEQUENCE_PREFIX_KEY = ">Fasta-read.";
}
