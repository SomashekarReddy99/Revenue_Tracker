package expense_income_tracker;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ExpenseIncomeTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Date", "Description", "Amount", "Type"};
    private final List<ExpenseIncomeEntry> entries;

    public ExpenseIncomeTableModel() {
        entries = new ArrayList<>();
    }

    public void addEntry(ExpenseIncomeEntry entry) {
        entries.add(entry);
        fireTableRowsInserted(entries.size() - 1, entries.size() - 1);
    }

    public void removeEntry(int rowIndex) {
        entries.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public ExpenseIncomeEntry getEntry(int rowIndex) {
        return entries.get(rowIndex);
    }

    public void clearEntries() {
        entries.clear();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return entries.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ExpenseIncomeEntry entry = entries.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> entry.getDate();
            case 1 -> entry.getDescription();
            case 2 -> entry.getAmount();
            case 3 -> entry.getType();
            default -> null;
        };
    }
}
