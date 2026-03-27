package allCafeInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateItem {

    private Connection conn;

    public UpdateItem(Connection conn) {
        this.conn = conn;
    }

    public void update(int id, String name, double price) {
        String query = "UPDATE menu SET item_name = ?, price = ? WHERE id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setInt(3, id);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Item updated successfully!");
            } else {
                System.out.println("Item not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating item.");
            e.printStackTrace();
        }
    }
}