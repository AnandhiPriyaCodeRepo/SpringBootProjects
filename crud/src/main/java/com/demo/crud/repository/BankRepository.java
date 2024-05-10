package com.demo.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.crud.entity.BankAccount;

@Repository
public interface BankRepository extends JpaRepository<BankAccount, Integer> {

}
