package org.example.service;

import org.example.entity.Account;
import org.example.entity.Transaction;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service // Mówi Springowi: "To jest warstwa logiki biznesowej"
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    // Dependency Injection przez konstruktor (najlepsza praktyka)
    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    // Tworzy nowe konto
    public Account createAccount(String accountNumber, BigDecimal initialBalance) {
        Account account = new Account(accountNumber, initialBalance);
        return accountRepository.save(account); // Zapisuje do bazy
    }

    // Pobiera konto po numerze
    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    // Przelew między kontami
    @Transactional // KLUCZOWE! Jeśli coś się wywali, wszystko się cofnie (rollback)
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        // Pobierz konta
        Account fromAccount = getAccount(fromAccountNumber);
        Account toAccount = getAccount(toAccountNumber);

        // Wykonaj operacje
        fromAccount.withdraw(amount); // Może rzucić wyjątek
        toAccount.deposit(amount);

        // Zapisz zmiany
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Zapisz transakcję
        Transaction transaction = new Transaction(fromAccount, toAccount, amount);
        transactionRepository.save(transaction);
    }
}