import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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


    CustomerUI()
    {
        this.setTitle("Expiry Notifier");
        this.setMinimumSize(new Dimension(800,600));
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.pack();
        table.init(stockTable,model1);
        table.init(orderTable,model2);
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
    }



    public void addItem() {

    }

}
