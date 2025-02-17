package com.ds.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.stereotype.Component;


import com.ds.dto.TranactionDto;
import com.ds.entity.Transaction;
import com.ds.repository.TranasctionRepository;

@Component
public class TransactionServiceImpl implements ITransactionService {

	@Autowired
	private TranasctionRepository transactionRepo;
	

	@Override
	public void saveTransaction(TranactionDto transactionDto) {
	     Transaction transaction=Transaction.builder()
	    		 .transactionType(transactionDto.getTransactionType())
	    		 .accountNumber(transactionDto.getAccountNumber())
	    		 .amount(transactionDto.getAmount())
	    		 .status("SUCCESS")
	    		 .build();
	     transactionRepo.save(transaction);
	     System.out.println(" Transaction Saved Successfully ");
		
	}

}
