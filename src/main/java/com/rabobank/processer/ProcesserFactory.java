package com.rabobank.processer;

import org.springframework.web.multipart.MultipartFile;

import com.rabobank.exception.FileHandlerExcepion;
import com.rabobank.util.RabobankConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcesserFactory {


	public static StatementProcesser getFileReader(String fileContentType, MultipartFile file) {
		switch (fileContentType) {
		case RabobankConstants.CSV_FILE_TYPE:
			return new CsvProcesser(file);
		case RabobankConstants.XML_FILE_TYPE:
			return new XmlProcesser(file);
		default:
			log.error("No correct fileType specified while calling getFileReader() method.");
			throw new FileHandlerExcepion("Uploaded filetype not matched with allowed extensions.");
		}
	}
}
