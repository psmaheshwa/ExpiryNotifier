import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

class JTableUtilities implements TableCellRenderer {

    public static final DefaultTableCellRenderer DEFAULT_RENDERER =
            new DefaultTableCellRenderer();

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Color background = null;

        java.util.Date date = new java.util.Date();
        java.util.Date expiry = (java.util.Date)table.getModel().getValueAt(row,5);
            if (expiry.compareTo(date) <= 0) {
                background = Color.RED;
            }
        renderer.setBackground(background);
        return renderer;
    }
}
