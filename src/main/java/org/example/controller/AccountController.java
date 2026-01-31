package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.TransferRequest;
import org.example.entity.Account;
import org.example.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController // Mówi Springowi: "To jest API, zwracaj JSONy"
@RequestMapping("/api/accounts") // Prefiks dla wszystkich endpointów w tej klasie
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // POST /api/accounts?number=123&initialBalance=1000
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestParam String number,
                                                 @RequestParam BigDecimal initialBalance) {
        return ResponseEntity.ok(accountService.createAccount(number, initialBalance));
    }

    // GET /api/accounts/123
    @GetMapping("/{number}")
    public ResponseEntity<Account> getAccount(@PathVariable String number) {
        return ResponseEntity.ok(accountService.getAccount(number));
    }

    // POST /api/accounts/transfer
    @PostMapping("/transfer")
    public ResponseEntity<Map<String, String>> transfer(@RequestBody @Valid TransferRequest request) {
        accountService.transfer(
                request.fromAccountNumber(),
                request.toAccountNumber(),
                request.amount()
        );
        return ResponseEntity.ok(Map.of("message", "Transfer successful"));
    }
}