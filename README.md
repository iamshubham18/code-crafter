
ATM Machine Simulation - README
This repository contains a comprehensive ATM Machine Simulation project developed as part of the Object-Oriented Programming with Java (OOPJ) Lab. 
The system replicates core banking operations including secure authentication, transaction management, and data persistence.

Features:
Secure Authentication: PIN-based login system to ensure authorized access.
Core Banking Operations:
Deposit: Add funds to the account with real-time balance updates.
Withdrawal: Remove funds with automated validation for insufficient balance.
Balance Inquiry: Check current account holdings via a secure toggle display.
Transaction History: A detailed log of all activities, including type, amount, and timestamps.
Modern UI: A "Glass Portal" themed dashboard built with Java Swing, featuring gradients and frosted-glass effects.

🛠️ Tech Stack
Language: Java (JDK 17 or 21) 
Frontend: Java Swing (GUI) 
Backend: JDBC (Java Database Connectivity) 
Database: MySQL 
IDE: IntelliJ IDEA 

📂 Project Structure
 the project is organized as follows:
models/: Contains POJOs like User.java, Account.java, and Transaction.java.
services/: Handles business logic and database interactions (UserService.java, AccountService.java, TransactionService.java).
ui/: Contains all Swing-based graphical components like LoginScreen.java and Dashboard.java.
util/: Includes DBConnection.java for managing MySQL connectivity.

🗄️ Database Schema
The system uses a relational database named atm_db with three primary tables:
Table	Description	Key Fields
Users	Authentication data	
user_id, username, pin 
Accounts	Financial data	
account_number, user_id, balance 
Transactions	Audit logs	
transaction_id, type, amount, timestamp 

🔧 Setup & Installation
Database Setup:
Create a MySQL database named atm_db.
Execute the SQL scripts to create the users, accounts, and transactions tables.
Environment Configuration:
Ensure JDK 17+ is installed.
Configure your database credentials in the DBConnection.java utility.

Run the Application:

Compile and run Main.java to launch the application.



