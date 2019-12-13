import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserInterface extends JFrame{
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
    private JTextField dateTxtBox;
    private JButton pickButton;
    private DefaultTableModel model = new DefaultTableModel() {
        public boolean isCellEditable(int rowIndex, int mColIndex) {
            return false;
        }
    };



    UserInterface()
    {
        this.setTitle("Expiry Notifier");
        this.setMinimumSize(new Dimension(800,600));
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.pack();
        init();

    }


    private void init() {

        detailsTable.setModel(model);
        detailsTable.setAutoCreateRowSorter(true);
        detailsTable.setShowHorizontalLines(false);
        detailsTable.setShowVerticalLines(false);
        model.addColumn("S.No");
        model.addColumn("ID");
        model.addColumn("Product Name");
        model.addColumn("Quantity");
        model.addColumn("Expiry Date");



    }
}
