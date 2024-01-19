package com.example.demo.controller;

// AccountController.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.AccountService;
import com.example.demo.data.Account;
import com.example.demo.exception.InsufficientFundsException;
import com.example.demo.exception.MyAccountNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @GetMapping("/byAccountNumber/{accountNumber}")
    public ResponseEntity<?> getByAccountNumber(@PathVariable String accountNumber) {
        try {
            Account account = accountService.getByAccountNumber(accountNumber);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (MyAccountNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public void createAccount(@RequestBody Account account) {
        accountService.createAccount(account);
    }

    @PutMapping("/{id}")
    public void updateAccount(@PathVariable Long id, @RequestBody Account account) {
        account.setId(id);
        accountService.updateAccount(account);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }

    @PutMapping("/transferMoney/{fromAccountNumber}/{toAccountNumber}/{amount}")
    public ResponseEntity<String> transferMoney(@PathVariable String fromAccountNumber,
                                                @PathVariable String toAccountNumber,
                                                @PathVariable double amount) {
        try {
            accountService.transferMoney(fromAccountNumber, toAccountNumber, amount);
            return new ResponseEntity<>("Money transferred successfully", HttpStatus.OK);
        } catch (InsufficientFundsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (MyAccountNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }
}
