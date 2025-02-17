package com.ds.utils;

import java.time.Year;

public class AccountUtils {
	public static final String ACCOUNT_EXIST_CODE = "001";
	public static final String ACCOUNT_EXIST_MESSAGE = "This User Already has an Account Created! ";
	public static final String ACCOUNT_CREATION_SUCCESS = "001";
	public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully Created. ";
	public static final String ACCOUNT_NOT_EXIST_CODE = "003";
	public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User with the Provided Account Number Doesn't Exist";
	public static final String ACCOUNT_FOUND_CODE = "004";
	public static final String ACCOUNT_FOUND_MESSAGE = "User Account Found ";
	public static final String INSUFFICIENT_BALANCE_CODE = "005";
	public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance";
	public static final String AMOUNT_DEBIT_SUCCESS = "006";
	public static final String AMOUNT_DEBIT_SUCCESS_MESSAGE = "Accountt has been successfully debited .";
    public static final String TRANSFER_SUCCESSFULL_CODE="007";
    public static final String TRANSFER_SUCCESSFULL_MESSAGE="Transfer Successfull";
    
    
    
    
    
	public static String generateAccountNumber() {
		/**
		 * 2024 + randomSixDigits
		 */

		Year currentYear = Year.now();

		int min = 100000;
		int max = 999999;
		// generate a random number between min and max
		int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

		// convert the current and randomNumber to string , then concatnation both of
		// them

		String year = String.valueOf(currentYear);

		String randomNumber = String.valueOf(randNumber);

		StringBuilder accountNumber = new StringBuilder();

		return accountNumber.append(year).append(randomNumber).toString();
	}
}
