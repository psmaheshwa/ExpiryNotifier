import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserInterface {
    private JFrame frame;
    private JPanel panel;
    private JTextField productTxtBox;
    private JTextField barCodeTxtBox;
    private JTextField productNameTxtBox;
    private JTextField priceTxtBox;
    private JTextField quantityTxtBox;
    private JTable detailsTable;
    private JButton addInButton;
    private JButton sellOutButton;
    private DefaultTableModel model = new DefaultTableModel() {
        public boolean isCellEditable(int rowIndex, int mColIndex) {
            return false;
        }
    };


    public void init() {
        detailsTable.setModel(model);
        detailsTable.setAutoCreateRowSorter(true);
        detailsTable.setShowHorizontalLines(false);
        detailsTable.setShowVerticalLines(false);
        model.addColumn("S.No");
        model.addColumn("ID");
        model.addColumn("Product Name");

    }
}
