package org.example.config;

import org.example.entity.Account;
import org.example.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Configuration // Mówi Springowi: "Tu są definicje fasolek (beans)"
public class DataInitializer {

    @Bean
        // Spring uruchomi tę metodę przy starcie
    CommandLineRunner initDatabase(AccountRepository repository) {
        return args -> {
            // Sprawdzamy czy baza jest pusta, żeby nie dublować przy każdym restarcie
            if (repository.count() == 3) {
                List<Account> accounts = new ArrayList<>();
                System.out.println("--- SEEDING DATA ---");

                // +100 kont generowanych
                for (int i = 1; i <= 100; i++) {
                    String accountNumber = String.format("ACC%06d", i); // ACC000001..ACC000100

                    // losowe saldo 0.00 - 10000.00
                    BigDecimal balance = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0, 10_000))
                            .setScale(2, RoundingMode.HALF_UP);

                    accounts.add(new Account(accountNumber, balance));
                }

                repository.saveAll(accounts);

                System.out.println("--- DATA SEEDED: " + accounts.size() + " accounts ---");
            } else {
                System.out.println("--- DATABASE NOT EMPTY, SKIPPING SEED ---");
            }
        };
    }
}