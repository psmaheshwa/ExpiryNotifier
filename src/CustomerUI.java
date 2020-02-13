import javax.swing.*;
import java.awt.*;

public class CustomerUI extends JFrame{
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

    CustomerUI()
    {
        this.setTitle("Expiry Notifier");
        this.setMinimumSize(new Dimension(800,600));
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.pack();
//        init();
//        actionEvent();
    }


}
