package allCafeInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignOut {

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public SignOut(JFrame frame, CardLayout cardLayout, JPanel mainPanel) {
        this.frame = frame;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
    }

    public JPanel createSignOutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Are you sure you want to sign out?");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));
        panel.add(label, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        JButton yesBtn = new JButton("Yes, Sign Out");
        yesBtn.setFont(new Font("Arial", Font.BOLD, 18));
        yesBtn.setBackground(Color.RED);
        yesBtn.setForeground(Color.WHITE);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 18));
        cancelBtn.setBackground(Color.GRAY);
        cancelBtn.setForeground(Color.WHITE);

        buttonPanel.add(yesBtn);
        buttonPanel.add(cancelBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Yes button action: go back to login screen
        yesBtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "loginPanel"); // assumes your login screen card is named "loginPanel"
            JOptionPane.showMessageDialog(frame, "You have signed out successfully.");
        });

        // Cancel button action: go back to previous panel (e.g., customer panel)
        cancelBtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "customerPanel"); // replace with your previous panel's name
        });

        return panel;
    }
}