package com.demo.crud.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.demo.crud.entity.BankAccount;
import com.demo.crud.repository.BankRepository;

public class BankServiceTest {
	
	private BankRepository bankRepository;
    private BankService bankService;
    List<BankAccount> account = new ArrayList<BankAccount>();

    @BeforeEach
    void setUp() {
        bankRepository = mock(BankRepository.class);
        bankService = new BankService(bankRepository);
        account.add(new BankAccount("Kathy Jane", "2587469545", 500.00));
		account.add(new BankAccount("Daniel Smith", "5896334587", 710.00));
    }

    @Test
    void testCreateAccountOk() {
        when(bankRepository.save(account.get(0))).thenReturn(account.get(0));
        ResponseEntity<Object> response = bankService.createAccount(account.get(0));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(account.get(0), response.getBody());
    }
    
    @Test
    void testCreateAccountFailed() {
        when(bankRepository.save(account.get(0))).thenThrow(DataIntegrityViolationException.class);
        ResponseEntity<Object> response = bankService.createAccount(account.get(0));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Unable to create the requested resource with accountNumber: " + account.get(0).getAccountNumber(), response.getBody());
    }
    
    @Test
    void testCreateMultipleAccountsOk() {
    	when(bankRepository.saveAll(account)).thenReturn(account);
    	ResponseEntity<Object> response = bankService.createMultipleAccounts(account);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(account, response.getBody());
    }
    
    @Test
    void testCreateMultipleAccountsFailed() {
    	when(bankRepository.saveAll(account)).thenThrow(DataIntegrityViolationException.class);
    	ResponseEntity<Object> response = bankService.createMultipleAccounts(account);
    	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Unable to create the requested resources", response.getBody());
    }
    
    @Test
    void testGetAccountHolderOk() {
    	when(bankRepository.findById(1)).thenReturn(Optional.of(account.get(0)));
    	ResponseEntity<Object> response = bankService.getAccountHolder(1);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(account.get(0), response.getBody());
    }
    
    @Test
    void testGetAccountHolderFailed() {
    	when(bankRepository.findById(1)).thenReturn(Optional.empty());
    	ResponseEntity<Object> response = bankService.getAccountHolder(12);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Resource doesn't exist", response.getBody());
    }
    
    @Test
    void testGetMultipleAccountHoldersOk() {
    	when(bankRepository.findAll()).thenReturn(account);
    	ResponseEntity<Object> response = bankService.getMultipleAccountHolders();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(account, response.getBody());
    }
    
    @Test
    void testGetMultipleAccountHoldersFailed() {
    	when(bankRepository.findAll()).thenReturn(Collections.emptyList());
    	ResponseEntity<Object> response = bankService.getMultipleAccountHolders();
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("No Resource is found", response.getBody());
    }
    
    @Test
    void testUpdateSingleAccountOk() {
    	when(bankRepository.findById(1)).thenReturn(Optional.of(account.get(0)));
    	when(bankRepository.save(account.get(0))).thenReturn(account.get(0));
    	ResponseEntity<Object> response = bankService.updateSingleAccount(account.get(0), 1);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(account.get(0), response.getBody());
    }
    
    @Test
    void testUpdateSingleAccountFailed() {
    	when(bankRepository.findById(1)).thenReturn(Optional.empty());
    	ResponseEntity<Object> response = bankService.updateSingleAccount(account.get(0),1);
    	assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Resource not found for updation", response.getBody());
    }
    
    @Test
    void testUpdateSingleAccountDataIntegrity() {
    	when(bankRepository.findById(1)).thenThrow(DataIntegrityViolationException.class);
    	ResponseEntity<Object> response = bankService.updateSingleAccount(account.get(0),1);
    	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Unable to update the requested resource with accountNumber: " + account.get(0).getAccountNumber(), response.getBody());
    }
    
    @Test
    void testUpdateMultipleAccountOk() {
    	account.get(0).setId(1);
    	account.get(1).setId(2);
    	when(bankRepository.existsById(account.get(0).getId())).thenReturn(true);
    	when(bankRepository.existsById(account.get(1).getId())).thenReturn(true);
    	when(bankRepository.save(account.get(0))).thenReturn(account.get(0));
       	when(bankRepository.save(account.get(1))).thenReturn(account.get(1));
    	ResponseEntity<Object> response = bankService.updateMultipleAccount(account);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("The patch update on all the accountholders was sucessfull", response.getBody());
    }
    
    @Test
    void testUpdateMultipleAccountFailed() {
    	when(bankRepository.existsById(account.get(0).getId())).thenReturn(false);
    	when(bankRepository.existsById(account.get(1).getId())).thenReturn(true);
    	when(bankRepository.save(account.get(0))).thenReturn(account.get(0));
    	ResponseEntity<Object> response = bankService.updateMultipleAccount(account);
    	assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Data for the specified account number: "
				+ account.get(0).getAccountNumber() + " is not found for updation", response.getBody());
    }
    
    @Test
    void testUpdateMultipleAccountDataIntegrity() {
    	account.get(0).setId(1);
    	account.get(1).setId(2);
    	when(bankRepository.existsById(account.get(0).getId())).thenReturn(true);
    	when(bankRepository.existsById(account.get(1).getId())).thenReturn(true);
    	when(bankRepository.save(account.get(0))).thenThrow(DataIntegrityViolationException.class);
    	when(bankRepository.save(account.get(1))).thenThrow(DataIntegrityViolationException.class);
    	ResponseEntity<Object> response = bankService.updateMultipleAccount(account);
    	assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Unable to update the requested resource with accountNumber: "+account.get(0).getAccountNumber(), response.getBody());
    }
    
    @Test
    void testDeleteSingleAccountOk() {
    	account.get(0).setId(1);
    	when(bankRepository.existsById(account.get(0).getId())).thenReturn(true);
    	ResponseEntity<Object> response = bankService.deleteSingleAccount(1);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Resource was sucessfully deleted", response.getBody());
    }
    @Test
    void testDeleteSingleAccountFailed() {
    	account.get(0).setId(1);
    	when(bankRepository.existsById(account.get(0).getId())).thenReturn(false);
    	ResponseEntity<Object> response = bankService.deleteSingleAccount(3);
    	assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Resource not found for deletion", response.getBody());
    }
    
    @Test
    void testDeleteMultipleAccountsOk() {
    	List<Integer> id = new ArrayList<>();
		id.add(1);
		id.add(2);
    	account.get(0).setId(1);
    	account.get(1).setId(2);
    	when(bankRepository.existsById(account.get(0).getId())).thenReturn(true);
    	when(bankRepository.existsById(account.get(1).getId())).thenReturn(true);
    	ResponseEntity<Object> response = bankService.deleteMultipleAccounts(id);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Resources were sucessfully deleted", response.getBody());
    }
    
    @Test
    void testDeleteMultipleAccountsFailed() {
    	List<Integer> id = new ArrayList<>();
		id.add(1);
		id.add(2);
    	ResponseEntity<Object> response = bankService.deleteMultipleAccounts(id);
    	assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals("Data for the specified account id: "+id.get(0)+" is not found for deletion", response.getBody());
    }
    
    @Test
    void testDeleteAll() {
    	ResponseEntity<Object> response = bankService.deleteAll();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Resources were sucessfully deleted", response.getBody());
    }
}
