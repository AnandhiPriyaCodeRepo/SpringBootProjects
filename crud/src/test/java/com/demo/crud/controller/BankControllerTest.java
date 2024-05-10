package com.demo.crud.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.demo.crud.entity.BankAccount;
import com.demo.crud.service.BankService;

import java.util.ArrayList;
import java.util.List;

public class BankControllerTest {

	private BankService bankService;
	private BankController bankController;
	List<BankAccount> account = new ArrayList<BankAccount>();

	@BeforeEach
	void setUp() {
		bankService = mock(BankService.class);
		bankController = new BankController(bankService);
		account.add(new BankAccount("Kathy Jane", "2587469545", 500.00));
		account.add(new BankAccount("Daniel Smith", "5896334587", 710.00));

	}

	@Test
	void testCreateAccountOk() {
		when(bankService.createAccount(account.get(0)))
				.thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(account.get(0)));
		ResponseEntity<Object> response = bankController.createAccount(account.get(0));
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(account.get(0), response.getBody());
	}

	@Test
	void testCreateaccountFailed() {
		when(bankService.createAccount(account.get(0)))
				.thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(account.get(0).getAccountNumber()));
		ResponseEntity<Object> response = bankController.createAccount(account.get(0));
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(account.get(0).getAccountNumber(), response.getBody());
	}

	@Test
	void testCreateMultipleAccountsOk() {
		when(bankService.createMultipleAccounts(account))
				.thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(account));
		ResponseEntity<Object> response = bankController.createMultipleAccounts(account);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(account, response.getBody());
	}

	@Test
	void testCreateMultipleAccountsFailed() {
		when(bankService.createMultipleAccounts(account)).thenReturn(
				ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to create the requested resources"));
		ResponseEntity<Object> response = bankController.createMultipleAccounts(account);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Unable to create the requested resources", response.getBody());
	}

	@Test
	void testGetAccountHolderOk() {
		when(bankService.getAccountHolder(account.get(0).getId()))
				.thenReturn(ResponseEntity.status(HttpStatus.OK).body(account.get(0)));
		ResponseEntity<Object> response = bankController.getAccountHolder(account.get(0).getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(account.get(0), response.getBody());
	}

	@Test
	void testGetAccountHolderFailed() {
		when(bankService.getAccountHolder(12))
				.thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource doesn't exist"));
		ResponseEntity<Object> response = bankController.getAccountHolder(12);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Resource doesn't exist", response.getBody());
	}

	@Test
	void testGetMultipleAccountHoldersOk() {
		when(bankService.getMultipleAccountHolders()).thenReturn(ResponseEntity.status(HttpStatus.OK).body(account));
		ResponseEntity<Object> response = bankController.getMultipleAccountHolders();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(account, response.getBody());
	}

	@Test
	void testGetMultipleAccountHoldersFailed() {
		when(bankService.getMultipleAccountHolders())
				.thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Resource is found"));
		ResponseEntity<Object> response = bankController.getMultipleAccountHolders();
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("No Resource is found", response.getBody());
	}

	@Test
	void testUpdateAccountOk() {
		when(bankService.updateSingleAccount(account.get(0), 1))
				.thenReturn(ResponseEntity.status(HttpStatus.OK).body(account.get(0)));
		ResponseEntity<Object> response = bankController.updateAccount(account.get(0), 1);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(account.get(0), response.getBody());
	}

	@Test
	void testUpdateAccountFailed() {
		when(bankService.updateSingleAccount(account.get(0), 1))
				.thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found for updation"));
		ResponseEntity<Object> response = bankController.updateAccount(account.get(0), 1);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Resource not found for updation", response.getBody());
	}

	@Test
	void testUpdateMultipleAccountsOk() {
		when(bankService.updateMultipleAccount(account)).thenReturn(
				ResponseEntity.status(HttpStatus.OK).body("The patch update on all the accountholders was sucessfull"));
		ResponseEntity<Object> response = bankController.updateMultipleAccounts(account);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("The patch update on all the accountholders was sucessfull", response.getBody());
	}

	@Test
	void testUpdateMultipleAccountsFailed() {
		when(bankService.updateMultipleAccount(account))
				.thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body(account.get(0).getAccountNumber()));
		ResponseEntity<Object> response = bankController.updateMultipleAccounts(account);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(account.get(0).getAccountNumber(), response.getBody());
	}

	@Test
	void testUpdateMultipleAccountsFailedDataIntegrity() {
		when(bankService.updateMultipleAccount(account))
				.thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(account.get(0).getAccountNumber()));
		ResponseEntity<Object> response = bankController.updateMultipleAccounts(account);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(account.get(0).getAccountNumber(), response.getBody());
	}

	@Test
	void testDeleteSingleAccountOk() {
		when(bankService.deleteSingleAccount(1))
				.thenReturn(ResponseEntity.status(HttpStatus.OK).body("Resource was sucessfully deleted"));
		ResponseEntity<Object> response = bankController.deleteSingleAccount(1);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Resource was sucessfully deleted", response.getBody());
	}

	@Test
	void testDeleteSingleAccountFailed() {
		when(bankService.deleteSingleAccount(1))
				.thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found for deletion"));
		ResponseEntity<Object> response = bankController.deleteSingleAccount(1);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Resource not found for deletion", response.getBody());
	}

	@Test
	void testDeleteMultipleAccountsOk() {
		var id = List.of(1, 2);
		when(bankService.deleteMultipleAccounts(id))
				.thenReturn(ResponseEntity.status(HttpStatus.OK).body("Resources were sucessfully deleted"));
		ResponseEntity<Object> response = bankController.deleteMultipleAccounts(id);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Resources were sucessfully deleted", response.getBody());
	}

	@Test
	void testDeleteMultipleAccountsFailed() {
		var id = List.of(1, 2);
		when(bankService.deleteMultipleAccounts(id))
				.thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body(id.get(0)));
		ResponseEntity<Object> response = bankController.deleteMultipleAccounts(id);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(id.get(0), response.getBody());
	}

	@Test
	void testDeleteAllOk() {
		when(bankService.deleteAll())
				.thenReturn(ResponseEntity.status(HttpStatus.OK).body("Resources were sucessfully deleted"));
		ResponseEntity<Object> response = bankController.deleteAll();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Resources were sucessfully deleted", response.getBody());
	}

}
