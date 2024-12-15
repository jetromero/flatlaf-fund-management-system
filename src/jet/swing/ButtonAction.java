package jet.swing;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Cursor;
import javax.swing.JButton;
/**
 *
 * @author jetve
 */
public class ButtonAction extends JButton {

    public ButtonAction() {
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "innerFocusWidth:0;"
                + "margin:5,20,5,20;"
                + "background:$Panel.background;"
                + "pressedBackground:#A7C957;");
    }
}
