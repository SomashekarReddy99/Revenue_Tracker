package expense_income_tracker;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        // Start by showing the login frame
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
