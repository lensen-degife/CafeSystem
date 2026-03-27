package allCafeInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class CafeGUI extends JFrame {

    private Connection conn;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Fonts
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 26);
    private final Font normalFont = new Font("Segoe UI", Font.PLAIN, 16);

    // Modern Colors
    private final Color PRIMARY = new Color(33, 150, 243);
    private final Color SECONDARY = new Color(76, 175, 80);
    private final Color BACKGROUND = new Color(245, 247, 250);
    private final Color CARD = Color.WHITE;
    private final Color TEXT = new Color(40, 40, 40);

    private JTextField loginUserField;
    private JPasswordField loginPassField;

    // ----------------- UI Helper Methods -----------------
    private JButton createModernButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    private JTextField createModernField() {
        JTextField field = new JTextField(15);
        field.setFont(normalFont);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        return panel;
    }

    // ----------------- Constructor -----------------
    public CafeGUI(Connection conn) {
        this.conn = conn;
        setTitle("Mini Café System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(showLoginScreen(), "login");

        add(mainPanel);
        setVisible(true);
    }

    // ----------------- Login Screen -----------------

    private JPanel showLoginScreen () {
            JPanel wrapper = new JPanel(new GridBagLayout());
            wrapper.setBackground(BACKGROUND);

            JPanel panel = createCardPanel();
            wrapper.add(panel);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            JLabel title = new JLabel("Welcome to Mini Café");
            title.setFont(titleFont);
            title.setForeground(PRIMARY);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(title, gbc);

            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.WEST;

            // Username
            JLabel userLabel = new JLabel("Username:");
            userLabel.setFont(normalFont);
            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(userLabel, gbc);

            loginUserField = createModernField();
            gbc.gridx = 1;
            panel.add(loginUserField, gbc);

            // Password
            JLabel passLabel = new JLabel("Password:");
            passLabel.setFont(normalFont);
            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(passLabel, gbc);

            loginPassField = createModernPasswordField();
            gbc.gridx = 1;
            panel.add(loginPassField, gbc);

        // Modern styled "Show Password" checkbox
        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.setFont(normalFont);
        showPassword.setForeground(TEXT); // matches your text color
        showPassword.setBackground(Color.WHITE);
        showPassword.setFocusPainted(false);
        showPassword.setBorder(BorderFactory.createEmptyBorder()); // removes the default border
        showPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));

// Add below password field
        GridBagConstraints gbcCheck = new GridBagConstraints();
        gbcCheck.gridx = 1;
        gbcCheck.gridy = 3;
        gbcCheck.anchor = GridBagConstraints.CENTER;
        gbcCheck.insets = new Insets(5, 0, 5, 0); // add padding
        panel.add(showPassword, gbcCheck);

// Toggle password visibility
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                loginPassField.setEchoChar((char) 0); // show password
            } else {
                loginPassField.setEchoChar('\u2022'); // hide password
            }
        });

            // Buttons
            JButton loginBtn = createModernButton("Login", PRIMARY);
            JButton signupBtn = createModernButton("Sign Up", SECONDARY);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
            btnPanel.setBackground(Color.WHITE);
            btnPanel.add(loginBtn);
            btnPanel.add(signupBtn);

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(btnPanel, gbc);

            // Login action
            loginBtn.addActionListener(e -> {
                String username = loginUserField.getText();
                String password = new String(loginPassField.getPassword());

                try {
                    String query = "SELECT role FROM users WHERE username=? AND password=?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String role = rs.getString("role");
                        if (role.equalsIgnoreCase("admin")) {
                            mainPanel.add(showAdminPanel(), "admin");
                            cardLayout.show(mainPanel, "admin");
                        } else {
                            mainPanel.add(showCustomerPanel(), "customer");
                            cardLayout.show(mainPanel, "customer");
                        }

                        // Clear login fields after successful login
                        loginUserField.setText("");
                        loginPassField.setText("");

                    } else {
                        JOptionPane.showMessageDialog(panel, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            // Signup action
            signupBtn.addActionListener(e -> {
                SignUpScreen signupScreen = new SignUpScreen(conn, cardLayout, mainPanel);
                mainPanel.add(signupScreen, "signup");
                cardLayout.show(mainPanel, "signup");
            });

            return wrapper;
        }

// ----------------- Modern Password Field -----------------
private JPasswordField createModernPasswordField () {
            JPasswordField field = new JPasswordField(15);
            field.setFont(normalFont);
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
            return field;
        }

    // ----------------- Admin Panel -----------------
    private JPanel showAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

// --------- Top Bar with Title and Sign Out ---------
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Admin Panel");
        title.setFont(titleFont);
        title.setHorizontalAlignment(SwingConstants.LEFT); // title to the left
        topBar.add(title, BorderLayout.WEST);

// Sign Out button on the right
        JButton signOutBtn = createModernButton("Sign Out", Color.DARK_GRAY);
        signOutBtn.addActionListener(e -> {
            mainPanel.add(showSignOutPanel("admin"), "signOutAdmin");
            cardLayout.show(mainPanel, "signOutAdmin");
        });
        topBar.add(signOutBtn, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

// --------- Table ---------
        DefaultTableModel tableModel = new DefaultTableModel();
        ModernTable table = new ModernTable(tableModel);

        tableModel.addColumn("ID");
        tableModel.addColumn("Item Name");
        tableModel.addColumn("Price");

        loadMenu(tableModel);

// Wrap table in a panel to add padding
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        tableWrapper.add(scrollPane, BorderLayout.CENTER);

        panel.add(tableWrapper, BorderLayout.CENTER);

// --------- Actions ---------
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addBtn = createModernButton("Add Item", SECONDARY);
        JButton updateBtn = createModernButton("Update Item", SECONDARY);
        JButton deleteBtn = createModernButton("Delete Item", SECONDARY);

        addBtn.addActionListener(e -> addItem(tableModel));
        updateBtn.addActionListener(e -> updateItem(table, tableModel));
        deleteBtn.addActionListener(e -> deleteItem(table, tableModel));

        actionsPanel.add(addBtn);
        actionsPanel.add(updateBtn);
        actionsPanel.add(deleteBtn);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ----------------- Customer Panel -----------------
    private JPanel showCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

// --------- Top Bar with Title and Sign Out ---------
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Customer Panel - Place Your Order");
        title.setFont(titleFont);
        title.setHorizontalAlignment(SwingConstants.LEFT); // title to the left
        topBar.add(title, BorderLayout.WEST);

// Sign Out button on the right
        JButton signOutBtn = createModernButton("Sign Out", Color.DARK_GRAY);
        signOutBtn.addActionListener(e -> {
            mainPanel.add(showSignOutPanel("customer"), "signOutCustomer");
            cardLayout.show(mainPanel, "signOutCustomer");
        });
        topBar.add(signOutBtn, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

// --------- Table ---------
// Make the table model read-only
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // disables editing
            }
        };

        ModernTable table = new ModernTable(tableModel);

        tableModel.addColumn("No");
        tableModel.addColumn("ID");
        tableModel.addColumn("Item Name");
        tableModel.addColumn("Price");

        loadMenu(tableModel);

// Hide the ID column
        if (table.getColumnCount() > 1) {
            table.getColumnModel().getColumn(1).setMinWidth(0);
            table.getColumnModel().getColumn(1).setMaxWidth(0);
            table.getColumnModel().getColumn(1).setWidth(0);
        }

// Set selection mode (optional, nice UX)
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

// Wrap table in a panel to add padding
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        tableWrapper.add(scrollPane, BorderLayout.CENTER);

        panel.add(tableWrapper, BorderLayout.CENTER);

// --------- Place Order Button ---------
        JButton orderBtn = createModernButton("Place Order", PRIMARY);
        orderBtn.setFont(titleFont);

        JPanel orderBtnWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        orderBtnWrapper.setBackground(Color.WHITE);
        orderBtnWrapper.add(orderBtn);

        panel.add(orderBtnWrapper, BorderLayout.SOUTH);

        orderBtn.addActionListener(e -> placeOrder(table, panel));

        return panel;
    }

    // ----------------- Helper Methods -----------------
    private void loadMenu(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String sql = "SELECT id, item_name, price FROM menu";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            int count = 1;

            while (rs.next()) {
                if (model.getColumnCount() == 4) {
                    model.addRow(new Object[]{
                            count++,
                            rs.getInt("id"),
                            rs.getString("item_name"),
                            rs.getDouble("price")

                    });
                } else {
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("item_name"),
                            rs.getDouble("price")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addItem(DefaultTableModel tableModel) {
        String name = JOptionPane.showInputDialog(this, "Item Name:");
        if (name == null || name.isEmpty()) return;

        String priceStr = JOptionPane.showInputDialog(this, "Price:");
        if (priceStr == null || priceStr.isEmpty()) return;

        try {
            double price = Double.parseDouble(priceStr);
            AddItem addItem = new AddItem(conn);
            addItem.add(name, price);
            loadMenu(tableModel);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateItem(JTable table, DefaultTableModel tableModel) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row first!");
            return;
        }
        int id = Integer.parseInt(table.getValueAt(row, 0).toString());

        String newName = JOptionPane.showInputDialog(this, "New Item Name:");
        if (newName == null || newName.isEmpty()) return;

        String priceStr = JOptionPane.showInputDialog(this, "New Price:");
        if (priceStr == null || priceStr.isEmpty()) return;

        try {
            double newPrice = Double.parseDouble(priceStr);
            UpdateItem updateItem = new UpdateItem(conn);
            updateItem.update(id, newName, newPrice);
            loadMenu(tableModel);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteItem(JTable table, DefaultTableModel tableModel) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a row first!");
            return;
        }
        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        DeleteItem deleteItem = new DeleteItem(conn);
        deleteItem.delete(id);
        loadMenu(tableModel);
    }

    private void placeOrder(JTable table, JPanel panel) {
        Map<Integer, Integer> orderMap = new HashMap<>();
        int[] selectedRows = table.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(panel, "Select items first!");
            return;
        }

        for (int row : selectedRows) {
            int id = (int) table.getValueAt(row, 1); // ✅ correct (hidden ID)
            String itemName = (String) table.getValueAt(row, 2);

            String qtyStr = JOptionPane.showInputDialog(panel, "Quantity for " + itemName + ":");
            if (qtyStr == null) continue;

            try {
                int qty = Integer.parseInt(qtyStr);
                orderMap.put(id, qty);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid quantity!");
                return;
            }
        }

        double total = 0;

        for (int row : selectedRows) {
            int qty = orderMap.get((int) table.getValueAt(row, 1));
            double price = (double) table.getValueAt(row, 3);

            total += price * qty;
        }

        JOptionPane.showMessageDialog(panel, "Total Amount: " + total + " birr\nPayment handled separately.");
    }

    // ----------------- Sign Out Panel -----------------
    private JPanel showSignOutPanel(String returnPanel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);

// Wrapper panel for label + buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(BACKGROUND);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

// Label
        JLabel label = new JLabel("Are you sure you want to sign out?");
        label.setFont(titleFont);
        label.setAlignmentX(Component.CENTER_ALIGNMENT); // center horizontally
        label.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0)); // top/bottom padding
        centerPanel.add(label);

// Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(BACKGROUND);

        JButton yesBtn = createModernButton("Sign Out", Color.RED);
        JButton cancelBtn = createModernButton("Cancel", Color.GRAY);

        buttonPanel.add(yesBtn);
        buttonPanel.add(cancelBtn);

// Center buttons below label
        centerPanel.add(buttonPanel);

        panel.add(centerPanel, BorderLayout.CENTER);

// Yes: go back to login
        yesBtn.addActionListener(e -> {

            loginUserField.setText("");
            loginPassField.setText("");

            cardLayout.show(mainPanel, "login");
            JOptionPane.showMessageDialog(this, "You have signed out successfully.");
        });

// Cancel: return to the previous panel (admin/customer)
        cancelBtn.addActionListener(e -> cardLayout.show(mainPanel, returnPanel));

        return panel;
    }
    // ----------------- Main -----------------

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/cafe_db?useSSL=false&serverTimezone=UTC";
        String user = "cafe_user";
        String pass = "Cafe1234";

        try {
            Connection conn = DriverManager.getConnection(url, user, pass);
            new CafeGUI(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}