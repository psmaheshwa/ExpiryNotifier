import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

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
    private Connection dbConnection;
    private PreparedStatement preparedStatement;
    private ResultSet rs;



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
        dateTxtBox.setToolTipText("YYYY-MM-DD");
        try {
            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ExpiryNotifier?useSSL=false","root","Wanna Cry7!");
            preparedStatement = dbConnection.prepareStatement("select * from product_details");
            rs = preparedStatement.executeQuery();
            while (rs.next())
            {
                model.addRow(new Object[]{rs.getInt("s_no"), rs.getString("productId"), rs.getString("productName"),rs.getString("price"),rs.getString("quantity"),rs.getDate("expiryDate")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JTableUtilities.setCellsAlignment(detailsTable);
        //createUIComponents();
    }


    private void actionEvent(){
        addInButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == addInButton){

            String productName = productNameTxtBox.getText();
            String productID = productTxtBox.getText();
            String price = priceTxtBox.getText();
            String barcode = barCodeTxtBox.getText();
            String expiryDate = dateTxtBox.getText();
            String quantity = quantityTxtBox.getText();

            if (validateInput(productID, price, barcode, expiryDate, quantity, productName)){
                try {
                    dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ExpiryNotifier?useSSL=false","root","Wanna Cry7!");
                    preparedStatement = dbConnection.prepareStatement("INSERT INTO `ExpiryNotifier`.`product_details` (`productName`, `productId`, `price`, `quantity`, `barcode`, `expiryDate`) VALUES (?,?,?,?,?,?)");
                    preparedStatement.setString (1, productName);
                    preparedStatement.setString (2, productID);
                    preparedStatement.setString (3, price);
                    preparedStatement.setString (4, quantity);
                    preparedStatement.setString (5, barcode);
                    preparedStatement.setDate(6, Date.valueOf(expiryDate));
                    preparedStatement.execute();
                    dbConnection.close();
                    if (rs.next())
                    {
                        model.addRow(new Object[]{rs.getInt("s_no"), rs.getString("productId"), rs.getString("productName"),rs.getString("price"),rs.getString("quantity"),rs.getDate("expiryDate")});
                    }
                    clear();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            JTableUtilities.setCellsAlignment(detailsTable);

        }
    }

    private boolean validateInput(String productID, String price, String barcode, String expiryDate, String quantity, String productName) {


        if(productName ==null) return false;
        if (productID == null) return false;
        if (Integer.parseInt(price) < 0) return false;
        if (barcode == null) return false;
        if (Integer.parseInt(quantity) < 0) return false;
        return expiryDate.matches("\\d{4}-\\d{2}-\\d{2}");
    }


    private void clear(){
        dateTxtBox.setText("");
        quantityTxtBox.setText("");
        barCodeTxtBox.setText("");
        priceTxtBox.setText("");
        productTxtBox.setText("");
        productNameTxtBox.setText("");
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
