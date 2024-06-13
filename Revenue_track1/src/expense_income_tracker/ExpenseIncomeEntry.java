package expense_income_tracker;

public class ExpenseIncomeEntry {
    private int id;
    private String date;
    private String description;
    private double amount;
    private String type;

    public ExpenseIncomeEntry(int id, String date, String description, double amount, String type) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.type = type;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
