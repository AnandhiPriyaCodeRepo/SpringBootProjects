package com.demo.crud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.demo.crud.entity.BankAccount;
import com.demo.crud.repository.BankRepository;

import jakarta.validation.ConstraintViolationException;

@Service
public class BankService {

	BankRepository bankRepository;

	@Autowired
	public BankService(BankRepository repository) {
		this.bankRepository = repository;
	}

	// Creating a single account
	public ResponseEntity<Object> createAccount(BankAccount singleAccount) {
		try {
			BankAccount savedAccount = bankRepository.save(singleAccount);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
		} catch (DataIntegrityViolationException ex) {
			throw new DataIntegrityViolationException("Resource cannot be created " + ex.getMessage());
		} catch (ConstraintViolationException e) {
			throw new IllegalArgumentException("Resource cannot be created " + e.getMessage());
		}
	}

	// Creating multiple accounts
	public ResponseEntity<Object> createMultipleAccounts(List<BankAccount> multipleAccounts) {
		try {
			List<BankAccount> savedAccounts = bankRepository.saveAll(multipleAccounts);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedAccounts);
		} catch (DataIntegrityViolationException ex) {
			throw new DataIntegrityViolationException("Resources cannot be created " + ex.getMessage());
		} catch (ConstraintViolationException e) {
			throw new IllegalArgumentException("Resources cannot be created " + e.getMessage());
		}
	}

	// Retrieving account by id
	public ResponseEntity<Object> getAccountHolder(Integer id) {
		Optional<BankAccount> account = bankRepository.findById(id);
		if (account.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(account.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource doesn't exist");
		}
	}

	// Retrieving all the accounts
	public ResponseEntity<Object> getMultipleAccountHolders() {
		List<BankAccount> accountHolders = bankRepository.findAll();
		if (accountHolders.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Resource is found");
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(accountHolders);
		}
	}

	// Updating account by id
	public ResponseEntity<Object> updateSingleAccount(BankAccount account, Integer id) {
		try {
			Optional<BankAccount> retrivedAccount = bankRepository.findById(id);
			if (retrivedAccount.isPresent()) {
				BankAccount updatedAccount = retrivedAccount.get();
				if (account.getAccountHolder() != null) {
					updatedAccount.setAccountHolder(account.getAccountHolder());
				}
				if (account.getAccountNumber() != null) {
					updatedAccount.setAccountNumber(account.getAccountNumber());
				}
				if (account.getBalance() != null) {
					updatedAccount.setBalance(account.getBalance());
				}
				updatedAccount = bankRepository.save(updatedAccount);
				return ResponseEntity.status(HttpStatus.OK).body(updatedAccount);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found for updation");
			}
		} catch (DataIntegrityViolationException ex) {
			throw new DataIntegrityViolationException(
					"Unable to update the requested resource with accountNumber: " + account.getAccountNumber());
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Resource cannot be updated");
		}
	}

	// Updating multiple accounts
	public ResponseEntity<Object> updateMultipleAccount(List<BankAccount> accounts) {
		for (BankAccount account : accounts) {
			try {
				if (account.getId() != null && bankRepository.existsById(account.getId())) {
					bankRepository.save(account);
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data for the specified account number: "
							+ account.getAccountNumber() + " is not found for updation");
				}
			} catch (DataIntegrityViolationException ex) {
				throw new DataIntegrityViolationException(
						"Unable to update the requested resource with accountNumber: " + account.getAccountNumber());
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Resource cannot be updated");
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body("The patch update on all the accountholders was sucessfull");
	}

	// deleting account by id
	public ResponseEntity<Object> deleteSingleAccount(Integer id) {
		if (bankRepository.existsById(id)) {
			bankRepository.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Resource was sucessfully deleted");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found for deletion");
		}
	}

	// deleting multiple accounts
	public ResponseEntity<Object> deleteMultipleAccounts(List<Integer> accountid) {
		for (Integer id : accountid) {
			if (bankRepository.existsById(id)) {
				bankRepository.deleteById(id);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Data for the specified account id: " + id + " is not found for deletion");
			}
		}
		return ResponseEntity.status(HttpStatus.OK).body("Resources were sucessfully deleted");
	}

	// deleting all the accounts
	public ResponseEntity<Object> deleteAll() {
		bankRepository.deleteAll();
		return ResponseEntity.status(HttpStatus.OK).body("Resources were sucessfully deleted");
	}
}
