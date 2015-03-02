package edu.hipsec.xrddau.app.controls.filegroupselector.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import com.quantumjockey.mvvmbase.controls.factories.SelectableGroupTreeCellFactory;
import com.quantumjockey.mvvmbase.controls.initialization.TreeViewInitializer;
import com.quantumjockey.mvvmbase.icons.IconLibrary;
import com.quantumjockey.mvvmbase.markup.MarkupControllerBase;
import java.util.ArrayList;

public class FileGroupSelectorController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private Label hierarchyHeader;

    @FXML
    private TreeView<String> fileSelection;

    /////////// Properties //////////////////////////////////////////////////////////////////

    private StringProperty header = new SimpleStringProperty();

    public final String getHeader() {
        return this.header.get();
    }

    public final void setHeader(String header) {
        this.header.set(header);
    }

    public StringProperty headerProperty() {
        return this.header;
    }

    private ObjectProperty<Tooltip> tooltip = new SimpleObjectProperty<>();

    public final Tooltip getTooltip() {
        return this.tooltip.get();
    }

    public final void setTooltip(Tooltip tooltip) {
        this.tooltip.set(tooltip);
    }

    public ObjectProperty<Tooltip> tooltipProperty() {
        return this.tooltip;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public MultipleSelectionModel<TreeItem<String>> getSelectionModel() {
        return this.fileSelection.getSelectionModel();
    }

    public void populateTree(ArrayList<String> temp, String label, SelectionMode mode, ChangeListener<TreeItem<String>> selectionChangedEvent) {

        IconLibrary icons = new IconLibrary();
        String rootLabel = (label != null) ? label : "Root Directory";

        ArrayList<String> _temp = new ArrayList<>();
        for (String item : temp) {
            String prefix = this.extractPrefix(item);
            if (!_temp.contains(prefix))
                _temp.add(prefix);
        }

        TreeItem<String> root = new TreeItem<>(rootLabel, new ImageView(icons.getRootIcon()));
        for (String item : _temp) {
            TreeItem<String> content = new TreeItem<>(item, new ImageView(icons.getGroupIcon()));
            temp.forEach((value) -> {
                String prefix = extractPrefix(value);
                if (prefix.equals(item)) {
                    TreeItem<String> selectable = new TreeItem<>(value);
                    content.getChildren().add(selectable);
                }
            });
            content.setExpanded(false);
            root.getChildren().add(content);
        }

        if (mode == SelectionMode.MULTIPLE)
            TreeViewInitializer.populateTree(this.fileSelection, root, mode, false, (f) -> new SelectableGroupTreeCellFactory(), selectionChangedEvent);
        else
            TreeViewInitializer.populateTree(this.fileSelection, root, mode, false, null, selectionChangedEvent);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private String extractPrefix(String file) {
        String[] components = file.split("_");
        String prefix = "";
        for (int j = 0; j < components.length - 1; j++) {
            prefix += components[j];
            if (j != components.length - 2)
                prefix += "_";
        }
        return prefix;
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {

    }

    @Override
    protected void setBindings() {
        this.hierarchyHeader.textProperty().bindBidirectional(this.headerProperty());
        this.fileSelection.tooltipProperty().bindBidirectional(this.tooltipProperty());
    }

    @Override
    protected void setDefaults() {
        VBox.setVgrow(this.fileSelection, Priority.ALWAYS);
    }

    @Override
    protected void setEvents() {

    }

    @Override
    protected void setListeners() {

    }

}
