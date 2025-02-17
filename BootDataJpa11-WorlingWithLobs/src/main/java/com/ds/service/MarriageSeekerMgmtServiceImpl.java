package com.ds.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ds.entity.MarriageSeeker;
import com.ds.repository.IMarriageSeekerRepositery;

@Service
public class MarriageSeekerMgmtServiceImpl implements IMarriageSeekerMgmtService {
	@Autowired
	private IMarriageSeekerRepositery marrigeRepo;  

	@Override
	public String registerMarriageSeeker(MarriageSeeker info) {
		int idVal = marrigeRepo.save(info).getSid();
		return "MarrriageSeeker Is Registered successfully with id value:" + idVal;

	}

	@Override
	public Optional<MarriageSeeker> getMarriageSeekerInfo(int id) {

		return marrigeRepo.findById(id);
	}

}
