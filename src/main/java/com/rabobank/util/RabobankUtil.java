package com.rabobank.util;

import org.apache.commons.lang3.StringUtils;

public final class RabobankUtil {
	
	// Hiding default constructor
	private RabobankUtil() {}
	
	public static final String getFileType(String fileContentType) {
		switch(fileContentType) {
			case "application/vnd.ms-excel" :
				return RabobankConstants.CSV_FILE_TYPE;
			case "text/xml" :
				return RabobankConstants.XML_FILE_TYPE;
				default:
					return StringUtils.EMPTY;
		}
	}

}
