package mvvmbase.controls;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import java.util.ArrayList;

public class ChoiceBoxExt {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public static void populate(ChoiceBox<String> selector, ArrayList<String> temp, ChangeListener<String> onSelectionChanged){
        selector.getItems().clear();
        selector.setItems(FXCollections.observableList(temp));
        selector.getSelectionModel().select(0);
        if (onSelectionChanged != null) {
            selector.valueProperty().addListener(onSelectionChanged);
        }
        selector.setTooltip(new Tooltip(selector.getSelectionModel().getSelectedItem()));
    }

}
