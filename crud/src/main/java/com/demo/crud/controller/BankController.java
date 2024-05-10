package com.demo.crud.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.crud.entity.BankAccount;
import com.demo.crud.service.BankService;

@RestController
@RequestMapping("/bank-accounts")
public class BankController {

	BankService bankService;

	@Autowired
	public BankController(BankService service) {
		this.bankService = service;
	}

	@PostMapping("/create-single")
	public ResponseEntity<Object> createAccount(@RequestBody BankAccount account) {
		return bankService.createAccount(account);
	}

	@PostMapping("/create-multiple")
	public ResponseEntity<Object> createMultipleAccounts(@RequestBody List<BankAccount> account) {
		return bankService.createMultipleAccounts(account);
	}

	@GetMapping("/retrieve/{id}")
	public ResponseEntity<Object> getAccountHolder(@PathVariable Integer id) {
		return bankService.getAccountHolder(id);
	}

	@GetMapping("/retrieve-all-accounts")
	public ResponseEntity<Object> getMultipleAccountHolders() {
		return bankService.getMultipleAccountHolders();
	}

	@PutMapping("/update-single/{id}")
	public ResponseEntity<Object> updateAccount(@RequestBody BankAccount account, @PathVariable Integer id) {
		return bankService.updateSingleAccount(account, id);
	}

	@PatchMapping("/update-multiple")
	public ResponseEntity<Object> updateMultipleAccounts(@RequestBody List<BankAccount> accounts) {
		return bankService.updateMultipleAccount(accounts);
	}

	@DeleteMapping("/delete-single/{id}")
	public ResponseEntity<Object> deleteSingleAccount(@PathVariable Integer id) {
		return bankService.deleteSingleAccount(id);
	}

	@DeleteMapping("/delete-multiple")
	public ResponseEntity<Object> deleteMultipleAccounts(@RequestBody List<Integer> accountid) {
		return bankService.deleteMultipleAccounts(accountid);
	}

	@DeleteMapping("delete-all")
	public ResponseEntity<Object> deleteAll() {
		return bankService.deleteAll();
	}
}
