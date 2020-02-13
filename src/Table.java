import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Table {
    public void init(JTable stockTable, DefaultTableModel model) {
        stockTable.setModel(model);
        stockTable.setAutoCreateRowSorter(true);
        stockTable.setShowHorizontalLines(false);
        stockTable.setShowVerticalLines(false);
        model.addColumn("ID");
        model.addColumn("Barcode");
        model.addColumn("Product Name");
        model.addColumn("Price");
        model.addColumn("Quantity");
        model.addColumn("Expiry Date");
        dbInit(stockTable,model);

    }

    private void dbInit(JTable table,DefaultTableModel model) {
        try {
            Connection dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ExpiryNotifier?useSSL=false", "root", "Wanna Cry7!");
            PreparedStatement preparedStatement = dbConnection.prepareStatement("select * from product_details");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                model.addRow(new Object[]{resultSet.getInt("s_no"), resultSet.getString("barcode"), resultSet.getString("productName"), resultSet.getString("price"), resultSet.getString("quantity"), resultSet.getDate("expiryDate")});
                table.setDefaultRenderer(Object.class,new JTableUtilities());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
