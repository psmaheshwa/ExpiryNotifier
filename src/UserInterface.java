import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.*;
import java.text.SimpleDateFormat;

public class UserInterface extends JFrame implements ActionListener, PropertyChangeListener  {
    private JPanel panel;
    private JTextField barCodeTxtBox;
    private JTextField productNameTxtBox;
    private JTextField priceTxtBox;
    private JTextField quantityTxtBox;
    private JTable detailsTable;
    private JButton addInButton;
    private JButton pickButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JFormattedTextField dateTxtBox;
    private DefaultTableModel model = new DefaultTableModel() {
        public boolean isCellEditable(int rowIndex, int mColIndex) {
            return false;
        }
    };
    private Connection dbConnection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private int ID;
    private CalenderWindow calenderWindow;
    private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);

    UserInterface() {
        this.setTitle("Expiry Notifier");
        this.setMinimumSize(new Dimension(800,600));
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.pack();
        init();
        actionEvent();
        dateTxtBox.setValue(simpleDateFormat.format(new java.util.Date()));
        calenderWindow = new CalenderWindow();
        calenderWindow.addPropertyChangeListener(this);
        //  calenderWindow.setUndecorated(true);
    }

    private void dbInit() {
        try {
            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ExpiryNotifier?useSSL=false","root","Wanna Cry7!");
            preparedStatement = dbConnection.prepareStatement("select * from product_details");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                    String today = simpleDateFormat.format((new java.util.Date()));
                    String tempDate = String.valueOf(resultSet.getDate("expiryDate"));
                    if(today.compareTo(tempDate) <= 0) {
                        model.addRow(new Object[]{resultSet.getInt("s_no"), resultSet.getString("barcode"), resultSet.getString("productName"), resultSet.getString("price"), resultSet.getString("quantity"), resultSet.getDate("expiryDate")});
                    }else
                    {
                        preparedStatement = dbConnection.prepareStatement("INSERT INTO `ExpiryNotifier`.`expired_products` (`productName`, `price`, `quantity`, `barcode`, `expiryDate`) VALUES (?,?,?,?,?)");
                        preparedStatement.setString (1, resultSet.getString("productName"));
                        preparedStatement.setString (2, resultSet.getString("price"));
                        preparedStatement.setString (3, resultSet.getString("quantity"));
                        preparedStatement.setString (4, resultSet.getString("barcode"));
                        preparedStatement.setDate(5, resultSet.getDate("expiryDate"));
                        preparedStatement.execute();
                        preparedStatement = dbConnection.prepareStatement("delete from product_details where s_no = ?");
                        preparedStatement.setInt(1,resultSet.getInt("s_no"));
                        preparedStatement.execute();
                    }
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
        model.addColumn("ID");
        model.addColumn("Barcode");
        model.addColumn("Product Name");
        model.addColumn("Price");
        model.addColumn("Quantity");
        model.addColumn("Expiry Date");
        dbInit();
        detailsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UserInterface.this.mouseClicked();
            }
        });
        //JTableUtilities.setCellsAlignment(detailsTable);
    }

    private void actionEvent(){
        addInButton.addActionListener(this);
        deleteButton.addActionListener(this);
        updateButton.addActionListener(this);
        pickButton.addActionListener(this);
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
        if(actionEvent.getSource() == updateButton)
        {
            updateStock();
        }
        if(actionEvent.getSource() == pickButton)
        {
            calenderEvent();
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
        dateTxtBox.setValue(simpleDateFormat.format(new java.util.Date()));
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
                dbConnection.close();
                clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        //JTableUtilities.setCellsAlignment(detailsTable);
    }

    private void deleteStock() {
        String barCode = barCodeTxtBox.getText();
        try {
            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ExpiryNotifier?useSSL=false","root","Wanna Cry7!");
            String sql = "delete from product_details where barcode = ?";
            //preparedStatement = dbConnection.prepareStatement("DELETE FROM product_details where barcode = '+barCode'");
            preparedStatement = dbConnection.prepareStatement(sql);
            preparedStatement.setString(1,barCode);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        model = new DefaultTableModel() {
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };
        model.setRowCount(0);
        init();
        clear();
    }

    private void updateStock() {
        String expiryDate = dateTxtBox.getText();
        try {
                dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ExpiryNotifier?useSSL=false","root","Wanna Cry7!");
                String sql = "UPDATE product_details SET barcode = ?," + "productName = ? , " + "price = ? ,"+ "quantity = ? , " + "expiryDate = ?  " + "where s_no = ?";
                preparedStatement = dbConnection.prepareStatement(sql);
                preparedStatement.setString(1,barCodeTxtBox.getText());
                preparedStatement.setString(2,productNameTxtBox.getText());
                preparedStatement.setString(3,priceTxtBox.getText());
                preparedStatement.setString(4,quantityTxtBox.getText());
                preparedStatement.setDate(5,Date.valueOf(dateTxtBox.getText()));
                preparedStatement.setInt(6,ID);
                preparedStatement.execute();
                model = new DefaultTableModel() {
                    public boolean isCellEditable(int rowIndex, int mColIndex) {
                        return false;
                    }
                };
                model.setRowCount(0);
                init();
                dbConnection.close();
                clear();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    private void mouseClicked(){
        DefaultTableModel model = (DefaultTableModel)detailsTable.getModel();
        int selectedRow =  detailsTable.getSelectedRow();
        barCodeTxtBox.setText(model.getValueAt(selectedRow,1).toString());
        productNameTxtBox.setText(model.getValueAt(selectedRow,2).toString());
        priceTxtBox.setText(model.getValueAt(selectedRow,3).toString());
        quantityTxtBox.setText(model.getValueAt(selectedRow,4).toString());
        dateTxtBox.setText(model.getValueAt(selectedRow,5).toString());
        ID = Integer.parseInt(model.getValueAt(selectedRow,0).toString());
    }

    private void calenderEvent() {
        calenderWindow.setLocation(dateTxtBox.getLocationOnScreen().x,(dateTxtBox.getLocationOnScreen().y + dateTxtBox.getHeight()));
        calenderWindow.setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if(propertyChangeEvent.getPropertyName().equals("selectedDate"))
        {
            java.util.Calendar cal = (java.util.Calendar)propertyChangeEvent.getNewValue();
            java.util.Date selDate = cal.getTime();
            dateTxtBox.setValue(simpleDateFormat.format(selDate));
        }
    }

}
