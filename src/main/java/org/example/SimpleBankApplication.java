package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Włącza auto-konfigurację, skanowanie komponentów itp.
public class SimpleBankApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimpleBankApplication.class, args);
    }
}