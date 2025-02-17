package com.ds.service;

import java.util.Optional;

import com.ds.entity.MarriageSeeker;

public interface IMarriageSeekerMgmtService {
	public String registerMarriageSeeker(MarriageSeeker info);
	
	public Optional<MarriageSeeker> getMarriageSeekerInfo(int id);
	

}
