import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

class JTableUtilities {
    static void setCellsAlignment(JTable table)
    {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        TableModel tableModel = table.getModel();

        for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++)
        {
            table.getColumnModel().getColumn(columnIndex).setCellRenderer(rightRenderer);
        }
    }
}
