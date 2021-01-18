package com.rabobank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatementData {
	
	private int transactionRefrence;
	private String accountNumber;
	private String description;
	
	private double mutation;
	private double endBalance;
	private double startBalance;
}
