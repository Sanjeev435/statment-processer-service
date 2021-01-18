package com.rabobank.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Sets;
import com.rabobank.model.FailedStatement;
import com.rabobank.model.StatementData;
import com.rabobank.processer.ProcesserFactory;
import com.rabobank.processer.StatementProcesser;
import com.rabobank.util.RabobankUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "statement")
@RequestMapping(path = "/statement")
@RestController
public class StatementProcessController {

	@PostMapping(value = "/upload")
	@ApiOperation(value = "Upload Statement")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Statement uploaded successfully")})
	public ResponseEntity<List<FailedStatement>> processStatementFile(@RequestParam("file") MultipartFile file) {

		StatementProcesser processer = ProcesserFactory.getFileReader(RabobankUtil.getFileType(file.getContentType()),
				file);
		List<StatementData> statements = processer.parseFile();

		Function<List<StatementData>, Set<FailedStatement>> validationProcess = validationProcess();

		List<FailedStatement> failedStatements = new ArrayList<>(validationProcess.apply(statements));
		Collections.sort(failedStatements, Comparator.comparing(FailedStatement::getTransactionRefrence));

		return new ResponseEntity<List<FailedStatement>>(failedStatements, HttpStatus.OK);
	}

	private Function<List<StatementData>, Set<FailedStatement>> validationProcess() {
		Function<List<StatementData>, Set<FailedStatement>> validationProcess = data -> {
			Set<FailedStatement> failedStatements = Sets.newHashSet();

			Map<Integer, StatementData> tRefs = new HashMap<>();

			data.forEach(d -> {
				if (tRefs.containsKey(d.getTransactionRefrence())) {
					failedStatements.add(FailedStatement.builder().transactionRefrence(d.getTransactionRefrence())
							.description(d.getDescription()).build());
					failedStatements.add(FailedStatement.builder().transactionRefrence(d.getTransactionRefrence())
							.description(tRefs.get(d.getTransactionRefrence()).getDescription()).build());
				} else {
					tRefs.put(d.getTransactionRefrence(), d);
				}

				if (Double.compare((d.getEndBalance() + d.getMutation()), d.getStartBalance()) == 0) {
					failedStatements.add(FailedStatement.builder().transactionRefrence(d.getTransactionRefrence())
							.description(d.getDescription()).build());
				}
			});

			return failedStatements;
		};
		return validationProcess;
	}
}
