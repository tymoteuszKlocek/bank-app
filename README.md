# Simple Bank API (bank-app)

Backend API w Javie oparty o Spring Boot + JPA/Hibernate z lokalną bazą PostgreSQL.
Projekt służy jako ćwiczenie pod system bankowy: konta, przelewy, transakcje, transakcyjność (`@Transactional`).

## Tech stack

- Java 17
- Spring Boot 3.x
- Spring Web (REST)
- Spring Data JPA (Hibernate)
- PostgreSQL (lokalnie)
- Maven

## Architektura (warstwy)

- `controller/` – endpointy HTTP (`@RestController`)
- `service/` – logika biznesowa (`@Service`), m.in. przelew i reguły domenowe
- `repository/` – dostęp do danych (`@Repository`, `JpaRepository`)
- `entity/` – encje JPA (`@Entity`) mapowane na tabele w DB
- `dto/` – obiekty wejścia/wyjścia dla API (request/response)

## Model danych (JPA)

### Account

- przechowuje `accountNumber` (unikalne) oraz `balance` (`BigDecimal`)
- relacja: konto posiada transakcje (1:N)

### Transaction

- zapis operacji przelewu: `fromAccount`, `toAccount`, `amount`, `timestamp`
- relacje: wiele transakcji do jednego konta (N:1) dla `fromAccount` i `toAccount`

## Transakcyjność

Przelew wykonywany jest w metodzie serwisowej oznaczonej `@Transactional`, co zapewnia:

- atomowość operacji (albo wszystko się uda, albo rollback)
- spójność w przypadku błędów (np. brak środków)

## Konfiguracja bazy (PostgreSQL)

Aplikacja łączy się z lokalnym PostgreSQL, np.:

- host: `localhost`
- port: `5432`
- db: `simple_bank`

Przykładowe ustawienia w `src/main/resources/application.properties`:

```properties
spring.application.name=simple-bank-api

spring.datasource.url=jdbc:postgresql://localhost:5432/simple_bank
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true