package com.demo.crud.repository;

import com.demo.crud.entity.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class BankRepositoryTest {

    @Autowired
    private BankRepository bankRepository;
    
    List<BankAccount> account = new ArrayList<BankAccount>();

    @BeforeEach
    void setUp() {
        account.add(new BankAccount("Kathy Jane", "2587469545", 500.00));
		account.add(new BankAccount("Daniel Smith", "5896334587", 710.00));
    }

    @Test
    void testSaveAndGetById() {
        BankAccount savedAccount = bankRepository.save(account.get(0));
        Optional<BankAccount> retrievedAccountOptional = bankRepository.findById(savedAccount.getId());
        assertTrue(retrievedAccountOptional.isPresent());
        BankAccount retrievedAccount = retrievedAccountOptional.get();
        assertEquals(savedAccount, retrievedAccount);
    }

    @Test
    void testFindAll() {
        bankRepository.save(account.get(0));
        bankRepository.save(account.get(1));
        List<BankAccount> allAccounts = bankRepository.findAll();
        assertEquals(2, allAccounts.size());
    }

}

