package com.demo.crud.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.crud.repository.BankRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

	private final BankRepository bankRepository;

	@Autowired
	public DataInitializer(BankRepository bankAccountRepository) {
		this.bankRepository = bankAccountRepository;
	}

	@PostConstruct
	public void initData() {
		bankRepository.save(new BankAccount("9123758964", "Kathy Joe", 1000.00));
		bankRepository.save(new BankAccount("9123757254", "Jane Smith", 500.00));
	}
}
