package com.ds.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankResponce {
//compositions
	private String responceCode;
	private String responceMessage;
	private AccountInfo accountInfo;
}
