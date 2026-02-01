package org.example.service;

import org.example.entity.Account;
import org.example.repository.AccountRepository;
import org.example.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Włącza wsparcie dla Mockito
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository; // "Udawane" repozytorium

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService; // Serwis, do którego zostaną wstrzyknięte mocki

    private Account fromAccount;
    private Account toAccount;

    @BeforeEach
    void setUp() {
        // Przygotowanie danych przed każdym testem
        fromAccount = new Account("ACC1", new BigDecimal("1000.00"));
        toAccount = new Account("ACC2", new BigDecimal("500.00"));
    }

    @Test
    @DisplayName("Should transfer money successfully")
    void shouldTransferMoneySuccessfully() {
        // GIVEN (Przygotowanie zachowania mocków)
        when(accountRepository.findByAccountNumber("ACC1")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("ACC2")).thenReturn(Optional.of(toAccount));

        // WHEN (Wykonanie akcji)
        accountService.transfer("ACC1", "ACC2", new BigDecimal("200.00"));

        // THEN (Weryfikacja wyników)
        assertEquals(new BigDecimal("800.00"), fromAccount.getBalance());
        assertEquals(new BigDecimal("700.00"), toAccount.getBalance());

        // Sprawdzamy, czy repozytoria zostały zawołane
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should throw exception when insufficient funds")
    void shouldThrowExceptionWhenInsufficientFunds() {
        // GIVEN
        when(accountRepository.findByAccountNumber("ACC1")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("ACC2")).thenReturn(Optional.of(toAccount));

        // WHEN & THEN
        assertThrows(IllegalStateException.class, () -> {
            accountService.transfer("ACC1", "ACC2", new BigDecimal("2000.00"));
        });

        // Weryfikujemy, że saldo się NIE zmieniło
        assertEquals(new BigDecimal("1000.00"), fromAccount.getBalance());
    }

    @Test
    @DisplayName("Should throw exception when source account not found")
    void shouldThrowExceptionWhenSourceAccountNotFound() {
        // GIVEN
        // Mówimy mockowi: gdy ktoś zapyta o ACC_MISSING, zwróć pusty Optional (brak konta)
        when(accountRepository.findByAccountNumber("ACC_MISSING")).thenReturn(Optional.empty());

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.transfer("ACC_MISSING", "ACC2", new BigDecimal("100.00"));
        });

        assertEquals("Account not found", exception.getMessage());

        // Weryfikujemy, że save() nigdy nie zostało zawołane (bo proces padł wcześniej)
        verify(accountRepository, never()).save(any());
    }
}