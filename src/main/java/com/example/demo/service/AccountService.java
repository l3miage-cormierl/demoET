package com.example.demo.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import com.example.demo.exception.MyAccountNotFoundException;



import com.example.demo.data.Account;


public interface AccountService {
    @Transactional
    void createAccount(Account account);

    @Transactional
    void updateAccount(Account account);

    List<Account> getAllAccounts();

    Account getAccountById(Long id);

    Account getByAccountNumber(String accountNumber) throws MyAccountNotFoundException;

    @Transactional
    void deleteAccount(Long id);

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    void transferMoney(String fromAccountNumber, String toAccountNumber, double amount) throws MyAccountNotFoundException;

//    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    void transferMoneyLock(String fromAccountNumber, String toAccountNumbers, double amount) throws MyAccountNotFoundException;

}
