package com.ds.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ds.entity.Transaction;

public interface TranasctionRepository extends JpaRepository<Transaction, String> {

}
