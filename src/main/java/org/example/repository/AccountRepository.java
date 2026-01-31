package org.example.repository;

import org.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Mówi Springowi: "To jest warstwa dostępu do danych"
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Spring automatycznie generuje implementację tego zapytania!
    Optional<Account> findByAccountNumber(String accountNumber);

    // Możesz też pisać własne zapytania:
    // @Query("SELECT a FROM Account a WHERE a.balance > :minBalance")
    // List<Account> findRichAccounts(@Param("minBalance") BigDecimal minBalance);
}