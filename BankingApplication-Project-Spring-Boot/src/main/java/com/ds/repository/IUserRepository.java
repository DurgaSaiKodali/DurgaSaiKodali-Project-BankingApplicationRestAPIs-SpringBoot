package com.ds.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ds.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {
	Boolean  existsByEmail(String email);
	Optional<User> findByEmail(String email);
	boolean existsByAccountNumber(String accountNumber);
	User findByAccountNumber(String accountNumber);
	
    
}
