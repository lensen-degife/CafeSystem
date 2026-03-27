package allCafeInfo;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SignUpScreen extends JPanel {

    private Connection conn;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 26);
    private final Font normalFont = new Font("Segoe UI", Font.PLAIN, 16);
    // Colors
    private final Color PRIMARY = new Color(33, 150, 243);
    private final Color SECONDARY = new Color(76, 175, 80);
    private final Color BACKGROUND = new Color(245, 247, 250);
    private final Color CARD = Color.WHITE;

    private JButton createModernButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private JPasswordField createModernPasswordField() {
        JPasswordField field = new JPasswordField(15);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private JPanel createCardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        return panel;
    }
    private JPanel createIconField(JTextField field, String iconPath) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JLabel icon = new JLabel(new ImageIcon(getClass().getResource(iconPath)));
        icon.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

        field.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));

        panel.add(icon, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }


    public SignUpScreen(Connection conn, CardLayout cardLayout, JPanel mainPanel) {
        this.conn = conn;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setLayout(new GridBagLayout());
        setBackground(BACKGROUND);

        JPanel card = createCardPanel();
        add(card);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel title = new JLabel("Create Account");
        title.setFont(titleFont);
        title.setForeground(PRIMARY);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;

        // Username
        gbc.gridy = 1;
        gbc.gridx = 0;
        card.add(new JLabel("Username:"), gbc);

        JTextField userField = createModernField();
        userField.setFont(normalFont);
        gbc.gridx = 1;
        card.add(userField, gbc);

        // Password
        gbc.gridy = 2;
        gbc.gridx = 0;
        card.add(new JLabel("Password:"), gbc);

        JPasswordField passField = createModernPasswordField();
        passField.setFont(normalFont);
        gbc.gridx = 1;
        card.add(passField, gbc);


        // Register Button
        JButton registerBtn = createModernButton("Register", PRIMARY);
        JButton backBtn = createModernButton("Back", SECONDARY);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setBackground(CARD);

        btnPanel.add(registerBtn);
        btnPanel.add(backBtn);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;

        card.add(btnPanel, gbc);
        registerBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
                return;
            }

            try {
                String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);
                String role = "customer";
                stmt.setString(3, role);  // always "customer"

                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Account created successfully!");
                cardLayout.show(mainPanel, "login");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Username already exists!");
            }
        });

        // Back Button Logic
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));
    }
}