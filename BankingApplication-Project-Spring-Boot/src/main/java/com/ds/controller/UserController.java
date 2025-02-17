package com.ds.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ds.dto.BankResponce;
import com.ds.dto.CreditDebitRequest;
import com.ds.dto.EnquiryRequest;
import com.ds.dto.LoginDto;
import com.ds.dto.TransferRequest;
import com.ds.dto.UserRequest;
import com.ds.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api-user")
@Tag(name="User Account Management APIs")
public class UserController {

	@Autowired
	 UserServiceImpl userService;
	
	//swagger-documentation
	@Operation(
			summary = "Create new User Account",
			description = "Creating a new User and assigning an account ID"
			)
	@ApiResponse(
			responseCode = "201",
			description ="Http status 201 CREATED"
			)
	@PostMapping("/user")
	public BankResponce createAccount(@RequestBody UserRequest userRequest) {
		return userService.createAccount(userRequest);
	}
	
	@Operation(
			summary = "Balance Enquiry",
			description = "Given an Account Number, Check how much the user has "
			)
	@ApiResponse(
			responseCode = "20",
			description ="Http Status 200 SUCCESS"
			)
	//Rest Controllers
	@PostMapping("/login")
	public BankResponce login(@RequestBody LoginDto loginDto) {
		return userService.login(loginDto);
	}
	
	@GetMapping("/balanceEnquiry")
	public BankResponce balanceEnquiry(@RequestBody EnquiryRequest request) {
		
		return userService.balanceEnquiry(request);
	}
	   
	@GetMapping("/nameEnquiry")
	public String nameEnquiry(@ RequestBody EnquiryRequest request) {
		return userService.nameEnquiry(request);
	}
	@PostMapping("/creditAccount")
	public BankResponce creditAccount(@RequestBody CreditDebitRequest request) {
		
		return userService.creditAccount(request);
	}
	@PostMapping("/debitAccount")
	public BankResponce debitAccount(@RequestBody CreditDebitRequest request) {
		return userService.debitAccount(request);
	}
	@PostMapping("/transfer")
	public BankResponce transfer(@RequestBody TransferRequest request) {
		return userService.transfer(request);
	}
	/*@PostMapping("/login")
		public ResponseEntity<BankResponce> login(@RequestBody LoginDto loginDto){
			System.out.println("Login successfull");
			BankResponce result=userService.login(loginDto);
			return new ResponseEntity<BankResponce>(HttpStatus.ACCEPTED);
		}*/
	}
