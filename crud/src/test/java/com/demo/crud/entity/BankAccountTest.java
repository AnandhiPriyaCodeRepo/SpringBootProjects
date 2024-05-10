package com.demo.crud.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankAccountTest {

	@Test
	void testBankAccountCreation() {
		BankAccount account = new BankAccount();
		account.setId(1);
		account.setAccountHolder("Kathy Jane");
		account.setAccountNumber("2587469545");
		account.setBalance(500.00);
		assertEquals(1, account.getId());
		assertEquals("Kathy Jane", account.getAccountHolder());
		assertEquals("2587469545", account.getAccountNumber());
		assertEquals(500.00, account.getBalance());
	}

	@Test
	void testParameterizedConstructor() {
		BankAccount account = new BankAccount("Daniel Smith", "5896334587", 710.00);
		assertEquals("Daniel Smith", account.getAccountHolder());
		assertEquals("5896334587", account.getAccountNumber());
		assertEquals(710.00, account.getBalance());
	}
}
