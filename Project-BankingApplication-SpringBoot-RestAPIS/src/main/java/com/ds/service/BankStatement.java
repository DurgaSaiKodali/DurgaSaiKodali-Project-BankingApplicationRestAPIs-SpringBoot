package com.ds.service;

import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ds.dto.EmailDetails;
import com.ds.entity.Transaction;
import com.ds.entity.User;
import com.ds.repository.IUserRepository;
import com.ds.repository.TranasctionRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfChunk;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

	@Autowired
	private TranasctionRepository transactionRepo;

	@Autowired
	private EmailServiceImpl emailService;
	@Autowired
	private IUserRepository userRepo;

	@Autowired
	private static final String FILE = "K:\\bank-statement\\IndianCommercialBankStatement.pdf";

	/*
	 * retrive list of transactions within a date range given an account number
	 * generate a pdf file of transactions
	 * send the file through email
	 * */

	public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate)
			throws DocumentException, FileNotFoundException {
		LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
		LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
		List<Transaction> transactionList = transactionRepo.findAll().stream()
				.filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
				.filter(transaction -> transaction.getCreatedAt().isEqual(start))
				.filter(transaction -> transaction.getCreatedAt().isEqual(end)).toList();

		User user = userRepo.findByAccountNumber(accountNumber);
		String custmorName = user.getFirstName() + user.getLastName();
		// private void designStatement(List<Transaction> transactions) throws
		// FileNotFoundException, DocumentException {
		com.itextpdf.text.Rectangle statementSize = new com.itextpdf.text.Rectangle(PageSize.A4);
		Document document = new Document(statementSize);
		log.info("Setting size of Document");
		OutputStream outPutStream = new FileOutputStream(FILE);
		PdfWriter.getInstance(document, outPutStream);
		document.open();

		PdfPTable bankInfoTable = new PdfPTable(1);
		PdfPCell bankName = new PdfPCell(new Phrase("Indian Commercial Bank"));
		bankName.setBorder(0);
		bankName.setBackgroundColor(BaseColor.DARK_GRAY);
		bankName.setPadding(20f);

		PdfPTable statementInfo = new PdfPTable(1);
		PdfPCell custmorInfo = new PdfPCell(new Phrase("From Date : " + startDate));
		custmorInfo.setBorder(0);

		PdfPCell acNo = new PdfPCell(new Phrase("Account No : " + accountNumber));
		acNo.setBorder(1);
		PdfPCell gap = new PdfPCell();

		PdfPCell bankTitle = new PdfPCell(new Phrase("INDIAN COMMERCIAL BANK"));
		bankTitle.setBorder(0);

		PdfPCell statement = new PdfPCell(new Phrase("Statement of Account"));
		statement.setBorder(0);

		PdfPCell stopDate = new PdfPCell(new Phrase("upToDate" + endDate));
		PdfPCell sp = new PdfPCell();
		sp.setBorder(0);

			PdfPCell HolderName=new PdfPCell(new Phrase("Customer Name : "+ custmorName));
			HolderName.setBorder(0);
			PdfPCell space= new PdfPCell();

		PdfPTable transactionalTable = new PdfPTable(4);
		PdfPCell date = new PdfPCell(new Phrase("Date"));
		date.setBackgroundColor(BaseColor.GRAY);
		date.setBorder(0);

		PdfPCell transactionType = new PdfPCell(new Phrase("Transaction Type "));
		transactionType.setBackgroundColor(BaseColor.GRAY);
		transactionType.setBorder(0);
		PdfPCell transactionAmount = new PdfPCell(new Phrase("Transaction Amount"));
		transactionAmount.setBackgroundColor(BaseColor.GRAY);
		transactionType.setBorder(0);

		PdfPCell status = new PdfPCell(new Phrase("STATUS"));
		status.setBackgroundColor(BaseColor.GRAY);
		status.setBorder(0);

		transactionalTable.addCell(date);
		transactionalTable.addCell(transactionType);
		transactionalTable.addCell(transactionAmount);
		transactionalTable.addCell(status);

		transactionList.forEach(transaction -> {
			transactionalTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
			transactionalTable.addCell(new Phrase(transaction.getTransactionType()));
			transactionalTable.addCell(new Phrase(transaction.getAmount().toString()));
			transactionalTable.addCell(new Phrase(transaction.getStatus()));
		});
		statementInfo.addCell(bankTitle);
		statementInfo.addCell(custmorInfo);
		statementInfo.addCell(statement);
		statementInfo.addCell(endDate);
		statementInfo.addCell(HolderName);
		statementInfo.addCell(space);
		statementInfo.addCell(acNo);

		document.add(bankInfoTable);
		document.add(statementInfo);
		document.add(transactionalTable);

		document.close();

		EmailDetails emailDetails = EmailDetails.builder().recipient(user.getEmail())
				.subject("INDIAN COMMERCIAL BANK ACCOUNT STATEMENT ")
				.messageBody("Kindly Find Your  Requested Account Statement Attaches! ").attachment(FILE).build();
		emailService.sendEmailWithTransactionAttachment(emailDetails);

		return transactionList;

	}

}
