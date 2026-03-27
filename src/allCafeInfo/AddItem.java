package allCafeInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddItem {

    private Connection conn;

    public AddItem(Connection conn) {
        this.conn = conn;
    }

    public void add(String name, double price) {
        String query = "INSERT INTO menu (item_name, price) VALUES (?, ?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setDouble(2, price);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Item added successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error adding item.");
            e.printStackTrace();
        }
    }
}