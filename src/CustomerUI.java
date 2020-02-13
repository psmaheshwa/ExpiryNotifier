import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CustomerUI extends JFrame implements ActionListener {
    private JTextField nameTxtBox;
    private JTextField barcodeTxtBox;
    private JButton scanButton;
    private JTable stockTable;
    private JTable orderTable;
    private JButton addButton;
    private JButton orderButton;
    private JPanel panel;
    private JTextField mobNotxtBox;
    private JTextField textField1;
    private DefaultTableModel model1 = new DefaultTableModel() {
        public boolean isCellEditable(int rowIndex, int mColIndex) {
            return false;
        }
    };
    private DefaultTableModel model2 = new DefaultTableModel() {
        public boolean isCellEditable(int rowIndex, int mColIndex) {
            return false;
        }
    };
    Table table = new Table();
    private Connection dbConnection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;



    CustomerUI()
    {
        this.setTitle("Expiry Notifier");
        this.setMinimumSize(new Dimension(800,600));
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.pack();
        table.init(stockTable,model1,"select * from product_details");
        actionEvent();
    }

    public void actionEvent(){
        addButton.addActionListener(this);
        orderButton.addActionListener(this);
        scanButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == addButton) {
            addItem();
        }
        if (actionEvent.getSource() == scanButton) {
            scanner();
        }
    }



    private void addItem() {

    }

    private void scanner(){
        String barcode = barcodeTxtBox.getText();
        String sql = "select * from product_details where barcode = " + barcode;
        model1 = new DefaultTableModel() {
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };
        table.init(stockTable,model1,sql);
    }

}
