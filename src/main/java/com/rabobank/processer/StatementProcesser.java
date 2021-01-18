package com.rabobank.processer;

import java.util.List;

import com.rabobank.model.StatementData;

public interface StatementProcesser {
	List<StatementData> parseFile();
}
