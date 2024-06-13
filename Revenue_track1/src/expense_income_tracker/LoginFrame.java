package expense_income_tracker;

import expense_income_tracker.ExpensesIncomesTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private final JTextField companyNameField;
    private final JTextField emailField;
    private final JButton loginButton;

    public LoginFrame() {
        setTitle("Login");
        setSize(400, 250); // Increased the size of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel to hold components with GridLayout
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Reduced padding
        panel.setBackground(new Color(173, 216, 230)); // Set background color to sky blue

        // Create a label for the company name (Revenue Tracker)
        JLabel companyNameLabel = new JLabel("REVENUE_TRACKER");
        companyNameLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Set font to bold and increase font size
        companyNameLabel.setForeground(Color.RED); // Set text color to red

        companyNameField = new JTextField();
        emailField = new JTextField();

        // Create labels and fields for username and password
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel companyLabel = new JLabel("Company Name:");
        JLabel emailLabel = new JLabel("Email:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        // Add components to the panel
        panel.add(companyNameLabel); // Add the company name label
        panel.add(new JLabel()); // Add an empty label for spacing
        panel.add(companyLabel);
        panel.add(companyNameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Add another empty label for spacing
        panel.add(loginButton);

        // Add the panel to the content pane
        getContentPane().add(panel);

        // Add action listener for the login button
        loginButton.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Perform validation
            if (isValidLogin(username, password)) {
                // Open the main application window
                new ExpensesIncomesTracker().setVisible(true);
                dispose(); // Close the login window
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Set the frame visible
        setVisible(true);
    }

    private boolean isValidLogin(String username, String password) {
        // Perform your validation here
        // For now, let's assume a valid login if username and password are not empty
        return !username.isEmpty() && !password.isEmpty();
    }

    public static void main(String[] args) {
        // Run the login frame
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
