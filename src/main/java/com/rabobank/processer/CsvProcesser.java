package com.rabobank.processer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.web.multipart.MultipartFile;

import com.rabobank.exception.StatementParserExcepion;
import com.rabobank.model.StatementData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CsvProcesser implements StatementProcesser {

	private MultipartFile csvFile;

	public CsvProcesser(MultipartFile file) {
		this.csvFile = file;
	}

	@Override
	public List<StatementData> parseFile() {
		List<StatementData> statements = new ArrayList<>();
		try (BufferedReader fileReader = new BufferedReader(
				new InputStreamReader(this.csvFile.getInputStream(), "UTF-8"));
				CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader());) {

			csvParser.getRecords().stream()
					.forEach(record -> statements.add(StatementData.builder()
							.transactionRefrence(Integer.parseInt(record.get("Reference")))
							.accountNumber(record.get("AccountNumber")).description(record.get("Description"))
							.mutation(Double.parseDouble(record.get("Mutation")))
							.endBalance(Double.parseDouble(record.get("End Balance")))
							.startBalance(Double.parseDouble(record.get("Start Balance")))
							.build()));
			
		} catch (UnsupportedEncodingException ex) {
			log.error("UnSupported encoding found for uploaded CSV file : " + "Uploaded file : "
					+ this.csvFile.getOriginalFilename() + " : " + ex.getMessage(), ex);
			throw new StatementParserExcepion("UnSupported encoding found for uploaded CSV file : " + "Uploaded file : "
					+ this.csvFile.getOriginalFilename() + " : " + ex.getMessage(), ex);
		} catch (IOException ex) {
			log.error("Error while parsing data from XML : " + "Uploaded file : " + this.csvFile.getOriginalFilename()
					+ " : " + ex.getMessage(), ex);
			throw new StatementParserExcepion("Error while parsing data from XML : " + "Uploaded file : "
					+ this.csvFile.getOriginalFilename() + " : " + ex.getMessage(), ex);
		}
		return statements;
	}

}
