package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.example.demo.data.Account;
import com.example.demo.service.AccountService;
import com.example.demo.exception.InsufficientFundsException;
import com.example.demo.exception.MyAccountNotFoundException;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = DemoApplication.class,
        properties = {
        "spring.datasource.embedded-database-connection=h2",
                "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
                "spring.jpa.show-sql=true"
        }
)
public class ConcurrencyTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void testConcurrentTransfer() {
        // Test createAccount
        Account account = new Account();
        account.setAccountNumber("123456");
        account.setBalance(100.0);
        accountService.createAccount(account);

        assertNotNull(account.getId(), "Account ID should not be null after creation");
        assertEquals("123456", account.getAccountNumber(), "Account number should match");
        assertEquals(100.0, account.getBalance(), 0.001, "Account balance should match");

        // Test updateAccount
        account.setBalance(150.0);
        accountService.updateAccount(account);
        Account updatedAccount = accountService.getAccountById(account.getId());
        assertEquals(150.0, updatedAccount.getBalance(), 0.001, "Account balance should be updated");

        // Test getAllAccounts
        assertEquals(1, accountService.getAllAccounts().size(), "Number of accounts should be 1");

        // Test transferMoney
        Account recipientAccount = new Account();
        recipientAccount.setAccountNumber("789012");
        recipientAccount.setBalance(50.0);
        accountService.createAccount(recipientAccount);

        try {
            accountService.transferMoney(account.getAccountNumber(), recipientAccount.getAccountNumber(), 50.0);
        } catch (InsufficientFundsException | MyAccountNotFoundException e) {
            // Handle specific exceptions as needed
            fail("Exception during concurrent transfer: " + e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions (unexpected)
            fail("Unexpected exception during concurrent transfer: " + e.getMessage());
        }        

        Account senderAfterTransfer = accountService.getAccountById(account.getId());
        Account recipientAfterTransfer = accountService.getAccountById(recipientAccount.getId());

        assertEquals(100.0, senderAfterTransfer.getBalance(), 0.001, "Sender balance should be updated after transfer");
        assertEquals(100.0, recipientAfterTransfer.getBalance(), 0.001, "Recipient balance should be updated after transfer");

        // Test deleteAccount
        Account badAccount = new Account();
        badAccount.setAccountNumber("654321");
        badAccount.setBalance(100.0);
        accountService.createAccount(badAccount);        
        accountService.deleteAccount(badAccount.getId());
        assertNull(accountService.getAccountById(badAccount.getId()), "Account should be deleted");
        assertEquals(2, accountService.getAllAccounts().size(), "Number of accounts should be 2 after deletion");
 
        // Create 10 threas for transferMoney
        ExecutorService executorService = Executors.newFixedThreadPool(1000);

        // Use executorService in a loop to execute 10 transferMoney from 123456 to 789012 with a value 10
        // you have to test different configuration (isolation mode, lock Mode)
        // you have to assert at the end that the base is coherent (what does it mean ?)



    }

}

