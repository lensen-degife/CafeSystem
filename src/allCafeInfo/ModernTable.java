package allCafeInfo;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class ModernTable extends JTable {

    public ModernTable(DefaultTableModel model) {
        super(model);

        // Table font and row height
        setFont(new Font("Segoe UI", Font.PLAIN, 18)); // larger, readable font
        setRowHeight(45); // taller rows
        setFillsViewportHeight(true);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        setSelectionBackground(new Color(33, 150, 243));
        setSelectionForeground(Color.WHITE);
        setBackground(Color.WHITE);

        // Header customization
        JTableHeader header = getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 20)); // bigger header font
        header.setBackground(new Color(245, 247, 250));
        header.setForeground(new Color(40, 40, 40));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // Cell padding and alternating row colors
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // padding
                setHorizontalAlignment( SwingConstants.CENTER );

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 247, 250)); // alternate row color
                }
                return c;
            }
        }
        );
    }
}