package expense_income_tracker;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.regex.Pattern;

public class ExpensesIncomesTracker extends JFrame {

    private final ExpenseIncomeTableModel tableModel;
    private final JTable table;
    private final JTextField dateField;
    private final JTextField descriptionField;
    private final JTextField amountField;
    private final JComboBox<String> typeCombobox;
    private final JButton addButton;
    private final JButton deleteButton;
    private final JButton editButton;
    private final JLabel netRevenueLabel;
    private final JLabel grossRevenueLabel;
    private double grossRevenue;
    private double netRevenue;

    // Database connection
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/expensesincomestracker?zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "";

    public ExpensesIncomesTracker() {
        // Initialize database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
        }

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to Set FlatDarkLaf LookAndFeel");
        }

        // UI Customization
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("TextField.background", Color.DARK_GRAY);
        UIManager.put("TextField.caretForeground", Color.RED);
        UIManager.put("ComboBox.foreground", Color.YELLOW);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        UIManager.put("ComboBox.selectionBackground", Color.BLACK);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.background", Color.ORANGE);
        UIManager.put("Label.foreground", Color.WHITE);

        Font customFont = new Font("Arial", Font.PLAIN, 18);
        UIManager.put("Label.font", customFont);
        UIManager.put("TextField.font", customFont);
        UIManager.put("ComboBox.font", customFont);
        UIManager.put("Button.font", customFont);

        grossRevenue = 0.0;
        netRevenue = 0.0;
        tableModel = new ExpenseIncomeTableModel();

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        dateField = new JTextField(10);
        descriptionField = new JTextField(20);
        amountField = new JTextField(10);
        typeCombobox = new JComboBox<>(new String[]{"Expense", "Income"});

        addButton = new JButton("Add");
        addButton.addActionListener(e -> addEntry());

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteEntry());

        editButton = new JButton("Edit");
        editButton.addActionListener(e -> editEntry());

        netRevenueLabel = new JLabel("Net Revenue: Rs" + netRevenue);
        grossRevenueLabel = new JLabel("Gross Revenue: Rs" + grossRevenue);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Date"));
        inputPanel.add(dateField);

        inputPanel.add(new JLabel("Description"));
        inputPanel.add(descriptionField);

        inputPanel.add(new JLabel("Amount"));
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("Type"));
        inputPanel.add(typeCombobox);

        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(editButton);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(netRevenueLabel);
        bottomPanel.add(grossRevenueLabel);
        setLayout(new BorderLayout());

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setTitle("Revenue Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);

        // Fetch initial data and populate the table
        fetchDataAndUpdateTableModel();
    }

    // Method to fetch data from the database and update the table model
    private void fetchDataAndUpdateTableModel() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String selectQuery = "SELECT * FROM revenuetracker";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Clear existing data in the table model
            tableModel.clearEntries();

            // Add fetched data to the table model
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String date = resultSet.getString("date");
                String description = resultSet.getString("description");
                double amount = resultSet.getDouble("amount");
                String type = resultSet.getString("type");

                // Create a new ExpenseIncomeEntry and add it to the table model
                ExpenseIncomeEntry entry = new ExpenseIncomeEntry(id, date, description, amount, type);
                tableModel.addEntry(entry);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to fetch data from the database", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addEntry() {
        String date = dateField.getText().trim();
        String description = descriptionField.getText();
        String amountStr = amountField.getText();
        String type = (String) typeCombobox.getSelectedItem();
        double amount;

        if (date.isEmpty() || !Pattern.matches("\\d{4}-\\d{2}-\\d{2}", date)) {
            JOptionPane.showMessageDialog(this, "Enter the Date in YYYY-MM-DD format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the Description", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the Amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Amount Format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (type.equals("Expense")) {
            amount *= -1;
        }

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String insertQuery = "INSERT INTO revenuetracker(date, description, amount, type) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, date);
            preparedStatement.setString(2, description);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, type);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to add entry", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (type.equals("Income")) {
            grossRevenue += amount;
        }
        netRevenue += amount;

        netRevenueLabel.setText("Net Revenue: Rs" + netRevenue);
        grossRevenueLabel.setText("Gross Revenue: Rs" + grossRevenue);

        clearInputFields();

        // Refresh table model after adding entry
        fetchDataAndUpdateTableModel();
    }

    private void deleteEntry() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ExpenseIncomeEntry entryToDelete = tableModel.getEntry(selectedRow);
        String deleteQuery = "DELETE FROM revenuetracker WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, entryToDelete.getId());
            preparedStatement.executeUpdate();

            if (entryToDelete.getType().equals("Income")) {
                grossRevenue -= entryToDelete.getAmount();
            }
            netRevenue -= entryToDelete.getAmount();

            netRevenueLabel.setText("Net Revenue: Rs" + netRevenue);
            grossRevenueLabel.setText("Gross Revenue: Rs" + grossRevenue);

            tableModel.removeEntry(selectedRow);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to delete entry", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   private void editEntry() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a row to edit", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    ExpenseIncomeEntry selectedEntry = tableModel.getEntry(selectedRow);

    // Set the input fields with the data of the selected entry
    dateField.setText(selectedEntry.getDate());
    descriptionField.setText(selectedEntry.getDescription());
    amountField.setText(String.valueOf(selectedEntry.getAmount()));
    typeCombobox.setSelectedItem(selectedEntry.getType());

    // Now, we need to handle the update operation when the user clicks the "Add" button after editing.
    // We'll perform the update operation in the addEntry() method, but we need to adjust it to handle updates.
    addButton.setText("Update");
    addButton.removeActionListener(addButton.getActionListeners()[0]); // Remove the previous ActionListener

    addButton.addActionListener(e -> {
        // Remove the previous entry
        deleteEntry();

        // Add the updated entry
        addEntry();

        // Reset the button text and action listener
        addButton.setText("Add");
        addButton.removeActionListener(addButton.getActionListeners()[0]);
        addButton.addActionListener(ev -> addEntry());
    });
}

    private void clearInputFields() {
        dateField.setText("");
        descriptionField.setText("");
        amountField.setText("");
        typeCombobox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExpensesIncomesTracker().setLocationRelativeTo(null));
    }
}