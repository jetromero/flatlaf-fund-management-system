package jet.table;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;

public class CheckBoxTableHeaderRenderer extends JCheckBox implements TableCellRenderer {

    private final JTable table;
    private final int column;
    private boolean listenerAdded = false;

    public CheckBoxTableHeaderRenderer(JTable table, int column) {
        this.table = table;
        this.column = column;
        init();
    }

    private void init() {
        putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Table.background;"
                + "icon.checkmarkColor:#A7C957");
        setHorizontalAlignment(SwingConstants.CENTER);

        if (!listenerAdded) {
            MouseAdapter mouseAdapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent me) {
                    if (SwingUtilities.isLeftMouseButton(me)) {
                        int col = table.columnAtPoint(me.getPoint());
                        if (col == column) {
                            boolean newState = !isSelected();
                            setSelected(newState);
                            selectedTableRow(newState);
                            table.getTableHeader().repaint();
                        }
                    }
                }
            };

            table.getTableHeader().addMouseListener(mouseAdapter);
            listenerAdded = true;
        }

        table.getModel().addTableModelListener((tme) -> {
            if (tme.getColumn() == column || tme.getType() == TableModelEvent.DELETE) {
                checkRow();
            }
        });
    }

    private void checkRow() {
        if (table.getRowCount() == 0) {
            setSelected(false);
            return;
        }

        boolean initValue = (boolean) table.getValueAt(0, column);
        boolean allSame = true;

        for (int i = 1; i < table.getRowCount(); i++) {
            boolean v = (boolean) table.getValueAt(i, column);
            if (initValue != v) {
                allSame = false;
                break;
            }
        }

        if (allSame) {
            putClientProperty(FlatClientProperties.SELECTED_STATE, null);
            setSelected(initValue);
        } else {
            putClientProperty(FlatClientProperties.SELECTED_STATE, FlatClientProperties.SELECTED_STATE_INDETERMINATE);
        }
        table.getTableHeader().repaint();
    }

    private void selectedTableRow(boolean selected) {
        for (int i = 0; i < table.getRowCount(); i++) {
            table.setValueAt(selected, i, column);
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
        return this;
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setColor(UIManager.getColor("TableHeader.bottomSeparatorColor"));
        float size = UIScale.scale(1f);
        g2.fill(new Rectangle2D.Float(0, getHeight() - size, getWidth(), size));
        g2.dispose();
        super.paintComponent(grphcs);
    }
}
