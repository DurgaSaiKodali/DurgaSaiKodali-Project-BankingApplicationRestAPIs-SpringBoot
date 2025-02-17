package com.ds.controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ds.entity.Transaction;
import com.ds.service.BankStatement;
import com.itextpdf.text.DocumentException;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor
public class TransactionController {
	
	@Autowired
	private BankStatement bankStatement;
	
	@GetMapping("/statement")
	public List<Transaction> generateStatement(@RequestParam String accountNumber,
			                                                                        @RequestParam String startDate,
			                                                                        @RequestParam String endDate) throws FileNotFoundException, DocumentException{
		return bankStatement.generateStatement(accountNumber, startDate, endDate);
	}

}
