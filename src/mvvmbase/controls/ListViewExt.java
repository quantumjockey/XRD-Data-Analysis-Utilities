package mvvmbase.controls;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import java.util.ArrayList;

public class ListViewExt {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public static void populate(ListView<String> selector, ArrayList<String> temp, ChangeListener<String> onSelectionChanged, SelectionMode mode, boolean editable){
        selector.getItems().clear();
        selector.setItems(FXCollections.observableList(temp));
        if (mode == SelectionMode.MULTIPLE) {
            selector.getSelectionModel().setSelectionMode(mode);
        }
        else {
            selector.getSelectionModel().setSelectionMode(mode);
            selector.getSelectionModel().select(0);
        }
        selector.setEditable(editable);
        if (onSelectionChanged != null) {
            selector.getSelectionModel().selectedItemProperty().addListener(onSelectionChanged);
        }
    }

}
