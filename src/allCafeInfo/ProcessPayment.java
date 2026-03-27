package allCafeInfo;

import java.sql.*;

public class ProcessPayment {

    private String accountNumber;
    private int pin;
    private Connection conn;

    public ProcessPayment(Connection conn, String accountNumber, Integer pin) {
        this.conn = conn;
        this.accountNumber = accountNumber;
        this.pin = pin;
    }

    // Authentication
    public boolean authenticate() {
        String sql = "SELECT pin FROM accounts WHERE account_number = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int dbPin = rs.getInt("pin");
                    if (dbPin == pin) {
                        System.out.println("Authentication successful");
                        return true;
                    } else {
                        System.out.println("Invalid PIN");
                        return false;
                    }
                } else {
                    System.out.println("Account not found");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Log transaction
    public void logTransaction(double amount, String fromAccount, String toAccount, String status, String description) {
        String sql = "INSERT INTO transactions (from_account, to_account, amount, status, description) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fromAccount);
            ps.setString(2, toAccount);
            ps.setDouble(3, amount);
            ps.setString(4, status);
            ps.setString(5, description);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Warning: Failed to log transaction. " + e.getMessage());
        }
    }

    // Process payment
    public boolean payment(double amount, boolean confirm) {
        String cafeAccount = "CAFEBANK";

        try {
            conn.setAutoCommit(false); // Start transaction

            // 1. Authenticate
            if (!authenticate()) {
                logTransaction(amount, accountNumber, cafeAccount, "FAILED", "Authentication failed");
                conn.rollback();
                return false;
            }

            // 2. Confirm
            if (!confirm) {
                logTransaction(amount, accountNumber, cafeAccount, "FAILED", "User cancelled");
                conn.rollback();
                return false;
            }

            // 3. Get customer balance
            double customerBalance;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT balance FROM accounts WHERE account_number = ?")) {
                ps.setString(1, accountNumber);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        logTransaction(amount, accountNumber, cafeAccount, "FAILED", "Account not found");
                        conn.rollback();
                        return false;
                    }
                    customerBalance = rs.getDouble("balance");
                }
            }

            // 4. Check funds
            if (customerBalance < amount) {
                logTransaction(amount, accountNumber, cafeAccount, "FAILED", "Insufficient balance");
                conn.rollback();
                return false;
            }

            // 5. Deduct customer
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE account_number = ?")) {
                ps.setDouble(1, amount);
                ps.setString(2, accountNumber);
                ps.executeUpdate();
            }

            // 6. Add to café
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE accounts SET balance = balance + ? WHERE account_number = ?")) {
                ps.setDouble(1, amount);
                ps.setString(2, cafeAccount);
                ps.executeUpdate();
            }

            // 7. Log transaction
            logTransaction(amount, accountNumber, cafeAccount, "SUCCESS", "Cafe payment");

            conn.commit(); // Commit both updates
            System.out.println("Payment successful!");
            System.out.printf("Paid: %.2f%n", amount);
            return true;

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            logTransaction(amount, accountNumber, cafeAccount, "FAILED", "System error");
            e.printStackTrace();
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}