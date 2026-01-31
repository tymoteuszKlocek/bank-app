package org.example.repository;

import org.example.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Na razie puste, ale możesz dodać np.:
    // List<Transaction> findByFromAccountId(Long accountId);
}