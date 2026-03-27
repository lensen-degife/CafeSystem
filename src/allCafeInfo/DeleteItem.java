package allCafeInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteItem {

    private Connection conn;

    public DeleteItem(Connection conn) {
        this.conn = conn;
    }

    public void delete(int id) {
        String query = "DELETE FROM menu WHERE id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Item deleted successfully!");
            } else {
                System.out.println("Item not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting item.");
            e.printStackTrace();
        }
    }
}