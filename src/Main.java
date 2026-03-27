import allCafeInfo.*;

import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/cafe_db?useSSL=false&serverTimezone=UTC";
        String username = "cafe_user";
        String password = "Cafe1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(url, username, password);
                 Scanner scanner = new Scanner(System.in)) {

                CafeMenu cafeMenu = new CafeMenu(conn);

                // LOGIN
                String role = login(conn, scanner);

                if (role == null) {
                    System.out.println("Invalid login! Exiting...");
                    return;
                }

                // ================= CUSTOMER =================
                if (role.equals("customer")) {

                    System.out.println("\n*************************");
                    System.out.println("  Welcome to Mini Café!");
                    System.out.println("*************************\n");

                    cafeMenu.displayMenu();

                    Order order = new Order();
                    String more;

                    do {
                        System.out.print("\nEnter Item ID you want to order: ");
                        int itemId = scanner.nextInt();

                        System.out.print("Enter quantity: ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();

                        order.addItem(itemId, quantity);

                        System.out.print("Add another item? (yes/no): ");
                        more = scanner.nextLine();

                    } while (more.equalsIgnoreCase("yes"));

                    // SUMMARY
                    order.printSummary(cafeMenu);
                    double total = order.calculateTotal(cafeMenu);

                    // PAYMENT
                    System.out.println("\nPayment Section:");
                    System.out.print("Enter Account Number: ");
                    String accountNumber = scanner.next();

                    System.out.print("Enter PIN: ");
                    int pin = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Confirm payment? (yes/no): ");
                    String confirm = scanner.nextLine();

                    ProcessPayment payment =
                            new ProcessPayment(conn, accountNumber, pin);

                    boolean success =
                            payment.payment(total, confirm.equalsIgnoreCase("yes"));

                    System.out.println("\n-------------------------------------");
                    if (success) {
                        System.out.println("Payment successful! Enjoy your order.");
                    } else {
                        System.out.println("Payment failed.");
                    }
                    System.out.println("-------------------------------------");
                }

                // ================= ADMIN =================
                else if (role.equals("admin")) {

                    System.out.println("\nWelcome Admin!");

                    boolean exit = false;

                    while (!exit) {
                        System.out.println("\n1. View Menu");
                        System.out.println("2. Add Item");
                        System.out.println("3. Delete Item");
                        System.out.println("4. Update Item");
                        System.out.println("5. Exit");
                        System.out.print("Choose option: ");

                        int choice = scanner.nextInt();

                        switch (choice) {
                            case 1:
                                cafeMenu.displayMenu();
                                break;

                            case 2:
                                scanner.nextLine();
                                System.out.print("Enter item name: ");
                                String name = scanner.nextLine();

                                System.out.print("Enter price: ");
                                double price = scanner.nextDouble();

                                AddItem addItem = new AddItem(conn);
                                addItem.add(name, price);
                                break;

                            case 3:
                                System.out.print("Enter item ID: ");
                                int id = scanner.nextInt();

                                DeleteItem deleteItem = new DeleteItem(conn);
                                deleteItem.delete(id);
                                break;

                            case 4:
                                System.out.print("Enter item ID to update: ");
                                int updateId = scanner.nextInt();
                                scanner.nextLine();

                                System.out.print("Enter new name: ");
                                String newName = scanner.nextLine();

                                System.out.print("Enter new price: ");
                                double newPrice = scanner.nextDouble();

                                UpdateItem updateItem = new UpdateItem(conn);
                                updateItem.update(updateId, newName, newPrice);

                                cafeMenu.displayMenu(); // optional but nice
                                break;

                            case 5:
                                exit = true;
                                break;
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String login(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter username: ");
        String username = scanner.next();

        System.out.print("Enter password: ");
        String password = scanner.next();

        String query = "SELECT role FROM users WHERE username=? AND password=?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getString("role"); // returns admin or customer
        } else {
            return null;
        }
    }
}