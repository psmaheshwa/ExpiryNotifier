import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UserInterface extends JFrame implements ActionListener {
    private JPanel panel;
    private JTextField barCodeTxtBox;
    private JTextField productNameTxtBox;
    private JTextField priceTxtBox;
    private JTextField quantityTxtBox;
    private JTable detailsTable;
    private JButton addInButton;
    private JTextField dateTxtBox;
    private JButton pickButton;
    private JButton deleteButton;
    private JButton updateButton;
    private DefaultTableModel model = new DefaultTableModel() {
        public boolean isCellEditable(int rowIndex, int mColIndex) {
            return false;
        }
    };
    private Connection dbConnection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    UserInterface() {
        this.setTitle("Expiry Notifier");
        this.setMinimumSize(new Dimension(800,600));
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.pack();
        init();
        actionEvent();
    }

    private void dbInit() {
        try {
            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ExpiryNotifier?useSSL=false","root","Wanna Cry7!");
            preparedStatement = dbConnection.prepareStatement("select * from product_details");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                model.addRow(new Object[]{resultSet.getString("barcode"), resultSet.getString("productName"), resultSet.getString("price"), resultSet.getString("quantity"), resultSet.getDate("expiryDate")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void init() {

        detailsTable.setModel(model);
        detailsTable.setAutoCreateRowSorter(true);
        detailsTable.setShowHorizontalLines(false);
        detailsTable.setShowVerticalLines(false);
        model.addColumn("Barcode");
        model.addColumn("Product Name");
        model.addColumn("Price");
        model.addColumn("Quantity");
        model.addColumn("Expiry Date");
        dateTxtBox.setToolTipText("YYYY-MM-DD");
        dbInit();
        detailsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mouseclicked();
            }
        });
        //JTableUtilities.setCellsAlignment(detailsTable);
    }

    private void actionEvent(){
        addInButton.addActionListener(this);
        deleteButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == addInButton){
            addStock();
        }
        if(actionEvent.getSource() == deleteButton)
        {
            deleteStock();
        }
    }

    private boolean validateInput(String price, String barcode, String expiryDate, String quantity, String productName) {


        if(productName ==null) return false;
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
        productNameTxtBox.setText("");
    }

    private void addStock() {
        String productName = productNameTxtBox.getText();
        String price = priceTxtBox.getText();
        String barcode = barCodeTxtBox.getText();
        String expiryDate = dateTxtBox.getText();
        String quantity = quantityTxtBox.getText();

        if (validateInput(price, barcode, expiryDate, quantity, productName)){
            try {
                dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ExpiryNotifier?useSSL=false","root","Wanna Cry7!");
                preparedStatement = dbConnection.prepareStatement("INSERT INTO `ExpiryNotifier`.`product_details` (`productName`, `price`, `quantity`, `barcode`, `expiryDate`) VALUES (?,?,?,?,?)");
                preparedStatement.setString (1, productName);
                preparedStatement.setString (2, price);
                preparedStatement.setString (3, quantity);
                preparedStatement.setString (4, barcode);
                preparedStatement.setDate(5, Date.valueOf(expiryDate));
                preparedStatement.execute();
                model = new DefaultTableModel() {
                    public boolean isCellEditable(int rowIndex, int mColIndex) {
                        return false;
                    }
                };
                model.setRowCount(0);
                init();
                if (resultSet.next())
                {
                    model.addRow(new Object[]{resultSet.getInt("s_no"), resultSet.getString("productName"), resultSet.getString("price"), resultSet.getString("quantity"), resultSet.getDate("expiryDate")});
                }
                dbConnection.close();
                clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        JTableUtilities.setCellsAlignment(detailsTable);
    }

    private void deleteStock() {

    }

    private void mouseclicked(){
        DefaultTableModel model = (DefaultTableModel)detailsTable.getModel();
        int selectedrow =  detailsTable.getSelectedRow();
        barCodeTxtBox.setText(model.getValueAt(selectedrow,1).toString());
        quantityTxtBox.setText(model.getValueAt(selectedrow,3).toString());

    }
}
