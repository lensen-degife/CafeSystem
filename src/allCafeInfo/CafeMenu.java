package allCafeInfo;
import java.sql.*;
public class CafeMenu {

    private Connection conn;

    public CafeMenu(Connection conn) {
        this.conn = conn;
    }

    // Display all menu items
    public void displayMenu() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM menu")) {

            System.out.println("Here is the menu:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("item_name");
                double price = rs.getDouble("price");
                System.out.printf("%-3d %-15s ----- %.2f birr%n", id, name, price);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get item by ID
    public MenuItem getItemById(int id) {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT item_name, price FROM menu WHERE id = ?")) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new MenuItem(
                        id,
                        rs.getString("item_name"),
                        rs.getDouble("price")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}