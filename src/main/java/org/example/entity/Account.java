package org.example.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // Mówi Hibernate: "To jest tabela w bazie"
@Table(name = "accounts") // Nazwa tabeli (opcjonalne, domyślnie byłoby "account")
public class Account {

    @Id // Klucz główny
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
    private Long id;

    @Column(nullable = false, unique = true) // Kolumna NOT NULL i UNIQUE
    private String accountNumber;

    @Column(nullable = false, precision = 19, scale = 2) // Precyzja dla pieniędzy
    private BigDecimal balance;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Relacja 1:N (jedno konto ma wiele transakcji)
    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    // Konstruktor bezparametrowy (WYMAGANY przez JPA/Hibernate)
    protected Account() {
    }

    // Konstruktor biznesowy
    public Account(String accountNumber, BigDecimal initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.createdAt = LocalDateTime.now();
    }

    // Metody biznesowe
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(balance) > 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        this.balance = this.balance.subtract(amount);
    }

    // Gettery i settery (WYMAGANE przez JPA)
    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}