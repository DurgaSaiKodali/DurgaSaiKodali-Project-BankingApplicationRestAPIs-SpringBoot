package com.ds.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ds.entity.MarriageSeeker;

public interface IMarriageSeekerRepositery extends JpaRepository<MarriageSeeker, Integer> {
}
