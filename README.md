
# CafeSystem

A **Café Management System** with a **Java GUI**, designed to manage menu items, orders, and payments for both customers and admins.


##  Overview

`CafeSystem` is a Java application with a **graphical user interface (GUI)** that simplifies café operations.  
It supports two main roles: **Customer** and **Admin**, each with different functionalities.

###  Customer

- Login and register
- Browse café menu visually
- Place orders using interactive forms
- Calculate total price
- Process payments

###  Admin

- Login as admin
- Add, update, or remove menu items
- View current menu
- Manage orders

##  Features

- Fully interactive **Java Swing GUI**
- Role-based access (Customer / Admin)
- Menu management (CRUD operations)
- Order processing with total calculation
- Basic payment simulation

## Tech Stack

- **Language:** Java  
- **GUI Framework:** Swing / AWT  
- **Database:** MySQL  
- **JDBC:** For database connectivity

##  Project Structure

CafeSystem/
├─ src/
│  ├─ allCafeInfo/
│  │  ├─ AddItem.java
│  │  ├─ CafeGUI.java        # Main GUI interface
│  │  ├─ CafeMenu.java
│  │  ├─ Order.java
│  │  ├─ ProcessPayment.java
│  │  └─ …other supporting classes
│  ├─ Main.java              # Entry point
├─ resources/                 # Optional images, icons for UI
├─ .gitignore


##  How It Works

1. **Database Connection:** Connects to MySQL using JDBC.
2. **User Login / Registration:** Users login via GUI forms.
3. **Role-Based GUI:** 
   - Customer sees menu and order interface.
   - Admin sees menu management interface.
4. **Interactive Operations:**  
   Buttons, tables, and forms handle CRUD operations and order processing.

##  Setup & Usage

###  Database

1. Create a MySQL database (e.g., `cafe_db`) and a `users` table.
2. Add admin and customer accounts:

```sql
CREATE DATABASE cafe_db;
USE cafe_db;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    password VARCHAR(50),
    role VARCHAR(20)
);

INSERT INTO users (username, password, role)
VALUES ('admin', 'admin123', 'admin'),
       ('user1', 'userpass', 'customer');
````

*(Adjust usernames and passwords as needed.)*

---

### Compile & Run

1. Make sure **Java JDK** and **MySQL JDBC driver** are installed.
2. Add the JDBC driver `.jar` to your classpath.
3. Compile:

```bash
javac -cp .:path/to/mysql-connector-java.jar src/*.java
```

4. Run:

```bash
java -cp .:path/to/mysql-connector-java.jar Main
```

5. GUI will launch, allowing login and interaction.

---

##  Notes

* Passwords are stored in plaintext — not secure for production.
* GUI uses **Swing**, can be enhanced with better visuals and responsive design.
* This project is meant for **learning and demo purposes**.


##  License
  MIT

## Author
  **Lensen Degife**


