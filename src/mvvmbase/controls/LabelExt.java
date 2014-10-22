package mvvmbase.controls;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

public class LabelExt {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public static void update(Label label, String text, String tooltip){
        label.setText(text);
        if (tooltip != null) {
            label.setTooltip(new Tooltip(tooltip));
        }
    }

}
