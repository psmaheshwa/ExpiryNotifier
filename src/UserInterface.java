import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface extends JFrame implements ActionListener {
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
    int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
    int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);;



    UserInterface()
    {
        this.setTitle("Expiry Notifier");
        this.setMinimumSize(new Dimension(800,600));
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.pack();
        init();
        actionEvent();

    }


    private void init() {

        detailsTable.setModel(model);
        detailsTable.setAutoCreateRowSorter(true);
        detailsTable.setShowHorizontalLines(false);
        detailsTable.setShowVerticalLines(false);
        model.addColumn("S.No");
        model.addColumn("ID");
        model.addColumn("Product Name");
        model.addColumn("Price");
        model.addColumn("Quantity");
        model.addColumn("Expiry Date");
        dateTxtBox.setToolTipText("DD-MM-YY");
        //createUIComponents();
    }


    private void actionEvent(){
        addInButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == addInButton){

            String productName = productNameTxtBox.getText();
            productNameTxtBox.setText("");

            String productID = productTxtBox.getText();
            productTxtBox.setText("");

            String price = priceTxtBox.getText();
            priceTxtBox.setText("");

            String barcode = barCodeTxtBox.getText();
            barCodeTxtBox.setText("");

            String expiryDate = dateTxtBox.getText();
            dateTxtBox.setText("");

            String quantity = quantityTxtBox.getText();
            quantityTxtBox.setText("");

            model.addRow(new Object[]{"1",productID,productName,price,quantity,expiryDate});
            JTableUtilities.setCellsAlignment(detailsTable, SwingConstants.CENTER);

        }
    }



//    private void createUIComponents() {
//        UtilDateModel utilDateModel = new UtilDateModel();
//        Properties properties = new Properties();
//        properties.put("text.today", "Today");
//        properties.put("text.month", "Month");
//        properties.put("text.year", "Year");
//        JDatePanelImpl1 = new JDatePanelImpl(utilDateModel,properties);
//        JDatePickerImpl datePicker = new JDatePickerImpl(JDatePanelImpl1,new DateComponentFormatter());
//
//        this.add(datePicker);
//    }
}
