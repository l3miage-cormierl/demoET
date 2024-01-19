package com.example.demo.service;

// AccountServiceImpl.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.example.demo.exception.MyAccountNotFoundException;
import com.example.demo.exception.InsufficientFundsException;

import com.example.demo.data.Account;
import com.example.demo.repository.AccountRepository;


import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    private final Lock lock = new ReentrantLock();

    @Override
    public void createAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public void updateAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Account getByAccountNumber(String accountNumber) throws MyAccountNotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new MyAccountNotFoundException("Account not found");
        }
        return account;
    }

    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public void transferMoney(String fromAccountNumber, String toAccountNumber, double amount) throws MyAccountNotFoundException {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber);

         if (fromAccount != null && toAccount != null) {
            if (fromAccount.getBalance() >= amount) {
                fromAccount.setBalance(fromAccount.getBalance() - amount);
                toAccount.setBalance(toAccount.getBalance() + amount);

                accountRepository.save(fromAccount);
                accountRepository.save(toAccount);
            } else {
                throw new InsufficientFundsException("Insufficient funds for transfer");
            }
        } else {
            throw new MyAccountNotFoundException("One or both accounts not found");
        }
    }


    // TODO : check le finally avec la libération de verrou
    @Override
    public void transferMoneyLock(String fromAccountNumber, String toAccountNumber, double amount) throws MyAccountNotFoundException {
        lock.lock();
        transferMoney(fromAccountNumber,  toAccountNumber,  amount);
        lock.unlock();
    }
}

