# Bank Management System (Java + NetBeans + MariaDB)

A desktop bank management system built in Java with a role-based architecture, custom Swing UI, and a MariaDB/MySQL backend accessed through JDBC. Built as a self-directed summer project to review and apply OOP, database design, and software architecture concepts from coursework.

## Overview

The system supports three distinct roles, each with its own dashboard and permissions:

- **Advisor** — full oversight; views all clients and their balances in a live table, with deactivated clients visually grayed out for quick identification.
- **Worker** — registers new clients (client + account created together in a single atomic transaction, so a client can never exist without an account), looks up existing clients by phone number, and can activate/deactivate a client's access.
- **Client** — logs in to view their balance, deposit, withdraw, transfer funds to another account, and review their full transaction history.

## Tech Stack

- **Language:** Java (Swing, JDBC)
- **IDE:** Apache NetBeans
- **Database:** MariaDB / MySQL
- **Architecture:** Model–View–Controller (`Model`, `View`, `Controller` packages)

## Key Design Decisions

- **Atomic client + account creation** — uses `Connection.setAutoCommit(false)` with explicit `commit()`/`rollback()` so a client is never left without a linked account, even if the process fails mid-way.
- **Soft-delete for clients** — instead of deleting a client, a worker deactivates them (`is_active = FALSE`). Deactivated clients cannot log in, but their transaction history is preserved for audit purposes — matching how real banking systems handle account closure. Workers can also reactivate a client, restoring their access.
- **Balance validation before withdrawals/transfers** — checks sufficient funds inside the same transaction as the balance update, preventing overdrafts from concurrent or rapid actions.
- **BigDecimal for all monetary values** — avoids the floating-point rounding errors that `float`/`double` would introduce over repeated transactions.
- **Custom Swing rendering** — rounded "glass" panels, gradient cards, placeholder-text fields, and a custom `TableCellRenderer` that grays out inactive-client rows in the Advisor's client table. Several of these exist specifically to work around NetBeans/Nimbus's default Look and Feel silently overriding explicit color settings.
- **Generic login error messages** — a deactivated client entering correct credentials sees the same "Invalid username or password" message as a wrong password would produce, rather than a distinct "account deactivated" message — preventing the login screen from being used to confirm which accounts exist.

## Database Schema

Four core tables: `client`, `worker`, `account`, and `transaction` (transaction types: `deposit`, `withdraw`, `transfer`). Foreign keys link accounts to clients, transactions to accounts, and client records to the worker who created them. The `client` table includes an `is_active` flag for soft-delete.

The full schema is available in [`create_tables.sql`](./create_tables.sql).

## Setup

1. Import `create_tables.sql` into your MariaDB/MySQL server (e.g., via phpMyAdmin in XAMPP).
2. Update the database connection details in `Controller.java` (`connect()` method) to match your local setup.
3. Open the project in NetBeans and run `LoginFrame.java`.

## Status

Core features complete: authentication for all three roles, client registration, deposit/withdraw/transfer with transaction history, and client activation/deactivation with visual status indicators. Actively refined as a learning project — feedback and suggestions welcome.
