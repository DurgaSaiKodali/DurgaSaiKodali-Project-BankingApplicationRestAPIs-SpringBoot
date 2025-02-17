package com.ds.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.ds.configuration.JwtTokenProvider;
import com.ds.dto.AccountInfo;
import com.ds.dto.BankResponce;
import com.ds.dto.CreditDebitRequest;
import com.ds.dto.EmailDetails;
import com.ds.dto.EnquiryRequest;
import com.ds.dto.LoginDto;
import com.ds.dto.TranactionDto;
import com.ds.dto.TransferRequest;
import com.ds.dto.UserRequest;
import com.ds.entity.Role;
import com.ds.entity.User;
import com.ds.repository.IUserRepository;
import com.ds.utils.AccountUtils;

@Component
public class UserServiceImpl implements IUserService {
	@Autowired
	private IUserRepository userRepo;

	@Autowired
    private IEmailService emailService;
	@Autowired
	private TransactionServiceImpl transactionService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	

	@Override
	public BankResponce createAccount(UserRequest userRequest) {

		/**
		 * Creating an account saving a new user into db Check if user already an
		 * account
		 */

		if (userRepo.existsByEmail(userRequest.getEmail())) {
			return BankResponce.builder().responceCode(AccountUtils.ACCOUNT_EXIST_CODE)
					.responceMessage(AccountUtils.ACCOUNT_EXIST_MESSAGE).accountInfo(null).build();
                    
		}

		User newUser = User.builder().firstName(userRequest.getFirstName()).lastName(userRequest.getLastName())
				.gender(userRequest.getGender()).address(userRequest.getAddress())
				.stateOfOrigin(userRequest.getStateOfOrigin()).accountNumber(AccountUtils.generateAccountNumber())
				.accountBalance(BigDecimal.ZERO).email(userRequest.getEmail())
				.password(passwordEncoder.encode(userRequest.getPassword()))
				.phoneNumber(userRequest.getPhoneNumber())
				.alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
				.status("ACTIVE")
				.role(Role.valueOf("ROLE_ADMIN"))
			.build();

		User savedUser = userRepo.save(newUser);
		// sent email alerts
		EmailDetails emailDetails = EmailDetails.builder().recipient(savedUser.getEmail()).subject("ACCOUNT CREATION")
				.messageBody("Congrlation! Your Account Has been successfully Created.\n Your Account Details are :"
						+ "Account Name " + savedUser.getFirstName() + "  " + savedUser.getLastName()
						+ "\nAccount Number " + savedUser.getAccountNumber())
				.build();
		emailService.sendEmailDetails(emailDetails);
		
     
		return BankResponce.builder().responceCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
				.responceMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
				.accountInfo(AccountInfo.builder().accountBalance(savedUser.getAccountBalance())
						.accountNumber(savedUser.getAccountNumber())
						.accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
						.build())

				.build();

	}
	
	public BankResponce login(LoginDto loginDto) {
		Authentication authentication=null;
		authentication =authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
				);
		EmailDetails loginAlert= EmailDetails.builder()
				.subject(" You are Logged in!")
				.recipient(loginDto.getEmail())
				.messageBody(" You logged into your account. if you didn't initiate please contact your bank ")
				.build();
		emailService.sendEmailDetails(loginAlert);
		return BankResponce.builder()
				.responceCode("Login Success ")
			   .responceMessage(jwtTokenProvider.generateToken(authentication))
				.build();	
	}
	
	@Override
	public BankResponce balanceEnquiry(EnquiryRequest request) {
		// check if the provided acn is wxist or not(acn=accountNumber)
		boolean isAccountExist = userRepo.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponce.builder().responceCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responceMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();
		}
		User foundUser = userRepo.findByAccountNumber(request.getAccountNumber());
		return BankResponce.builder().responceCode(AccountUtils.ACCOUNT_FOUND_CODE)
				.responceCode(AccountUtils.ACCOUNT_FOUND_MESSAGE)
				.accountInfo(AccountInfo.builder().accountBalance(foundUser.getAccountBalance())
						.accountNumber(request.getAccountNumber())
						.accountName(foundUser.getFirstName() + " " + foundUser.getLastName())

						.build())
				.build();
	}

	@Override
	public String nameEnquiry(EnquiryRequest request) {
		boolean isAccountExist = userRepo.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
		}
		User foundUser = userRepo.findByAccountNumber(request.getAccountNumber());

		return foundUser.getFirstName() + " " + foundUser.getLastName();

	}

	@Override
	public BankResponce creditAccount(CreditDebitRequest request) {
		// check if account exist
		boolean isAccountExist = userRepo.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponce.builder().responceCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responceMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();

		}
		User userToCredit = userRepo.findByAccountNumber(request.getAccountNumber());
		userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
		userRepo.save(userToCredit);
		
		//save transaction
		TranactionDto transactionDto=TranactionDto.builder()
				.accountNumber(userToCredit.getAccountNumber())
				.transactionType("CREDIT")
				.amount(request.getAmount())
				.status("SUCCESS")
				.build();
		transactionService.saveTransaction(transactionDto);
		
		return BankResponce.builder().responceCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
				.responceMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
						.accountBalance(userToCredit.getAccountBalance()).accountNumber(request.getAccountNumber())
						.build())

				.build();

	}

	@Override
	public BankResponce debitAccount(CreditDebitRequest request) {
		// check if the account is existed
		// check if the amount intend to withdraw is not more then current account
		// balance

		boolean isAccountExist = userRepo.existsByAccountNumber(request.getAccountNumber());
		if (!isAccountExist) {
			return BankResponce.builder().responceCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responceMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE).accountInfo(null).build();

		}
		User userToDebit = userRepo.findByAccountNumber(request.getAccountNumber());
		BigInteger avaliableBalance = userToDebit.getAccountBalance().toBigInteger();
		BigInteger debitAmount = request.getAmount().toBigInteger();
		if (avaliableBalance.intValue() < debitAmount.intValue()) {
			return BankResponce.builder().responceCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
					.responceMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE).accountInfo(null).build();
		} else {
			userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
			userRepo.save(userToDebit);
			
			
			//save transaction
			TranactionDto transactionDto=TranactionDto.builder()
					.accountNumber(userToDebit.getAccountNumber())
					.transactionType("CREDIT")
					.amount(request.getAmount())
					.status("SUCCESS")
					.build();
			transactionService.saveTransaction(transactionDto);
			
			
			return BankResponce.builder().responceCode(AccountUtils.AMOUNT_DEBIT_SUCCESS)
					.responceMessage(AccountUtils.AMOUNT_DEBIT_SUCCESS_MESSAGE)
					.accountInfo(AccountInfo.builder().accountNumber(request.getAccountNumber())
							.accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
							.accountBalance(userToDebit.getAccountBalance()).build())

					.build();
		}

	}
	@Override
	public BankResponce transfer(TransferRequest request) {
		/*
		 * Get account to debit(check if it is exist)
		 * check if the account im debiting is not more then the current balance
		 * debit the amount
		 * get account to credit
		 * credit the account
		 * */
		
		//boolean isSourceAccountExist=userRepo.existsByAccountNumber(request.getSourceAccountNumber());
		boolean isDestinationAccountExist=userRepo.existsByAccountNumber(request.getDestinationAccountNumber());
		if( ! isDestinationAccountExist) {
			return BankResponce.builder()
					.responceCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
					.responceMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
					.accountInfo(null)
					.build();
			
		}
		
			User sourceAccountUser=userRepo.findByAccountNumber(request.getSourceAccountNumber());
			if(request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0) {
				return BankResponce.builder()
						.responceCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
						.responceCode(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
						.accountInfo(null)
						.build();
				
				
			}
			sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
			String sourceUserName=sourceAccountUser.getFirstName()+" "+sourceAccountUser.getLastName();
			userRepo.save(sourceAccountUser);
			EmailDetails debitAlert=EmailDetails.builder()
					.subject("Debit Alert")
					.recipient(sourceAccountUser.getEmail())
					.messageBody("The sum of "+request.getAmount()+" has been deducted from your account ! your Account current Balance is "+sourceAccountUser.getAccountBalance())
					. build();
			
			emailService.sendEmailDetails(debitAlert);
			
			User destinationAccountUser=userRepo.findByAccountNumber(request.getDestinationAccountNumber());
			destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
			//String recipientUserName=destinationAccountUser.getFirstName()+" "+destinationAccountUser.getLastName();
			userRepo.save(destinationAccountUser);
			EmailDetails creditAlert=EmailDetails.builder()
					.subject("Credit Alert")
					.recipient(sourceAccountUser.getEmail())
					.messageBody(" The sum of  "+request.getAmount()+" has been sent to your account from "+sourceUserName+" your Current balance is "+sourceAccountUser.getAccountBalance())
					.build();
			        
		emailService.sendEmailDetails(creditAlert);
		
		//save transaction
				TranactionDto transactionDto=TranactionDto.builder()
						.accountNumber(destinationAccountUser.getAccountNumber())
						.transactionType("CREDIT")
						.amount(request.getAmount())
						.status("SUCCESS")
						.build();
				transactionService.saveTransaction(transactionDto);
		
		
		return BankResponce.builder()
				.responceCode(AccountUtils.TRANSFER_SUCCESSFULL_CODE)
				.responceMessage(AccountUtils.TRANSFER_SUCCESSFULL_MESSAGE)
				.accountInfo(null)
				.build();
	
	}
	
	
	
	

}
