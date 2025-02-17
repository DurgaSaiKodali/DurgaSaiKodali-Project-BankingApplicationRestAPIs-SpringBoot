package com.ds.service;

import com.ds.dto.BankResponce;
import com.ds.dto.CreditDebitRequest;
import com.ds.dto.EnquiryRequest;
import com.ds.dto.LoginDto;
import com.ds.dto.TransferRequest;
import com.ds.dto.UserRequest;

public interface IUserService {
	
	BankResponce createAccount(UserRequest userRequest);
	BankResponce balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponce creditAccount(CreditDebitRequest request);
    BankResponce debitAccount(CreditDebitRequest request);
    BankResponce transfer(TransferRequest request);
    BankResponce login(LoginDto loginDto);
}
