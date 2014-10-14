package app.martiffviewport.components;

import MvvmBase.markup.MarkupControllerBase;
import java.io.IOException;

import app.valueadjuster.ValueAdjuster;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffvisualization.MARTiffVisualizer;

public class MARTiffViewportController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private ImageView imageViewport;

    @FXML
    private ValueAdjuster maxBound;

    @FXML
    private ValueAdjuster minBound;

    @FXML
    private TitledPane viewportTitle;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffViewportController(){
        maxBound = new ValueAdjuster();
        minBound = new ValueAdjuster();
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public ImageView getViewport(){
        return imageViewport;
    }

    public void RenderImage(MARTiffImage image) throws IOException {
        MARTiffVisualizer marImageGraph = new MARTiffVisualizer(image);
        imageViewport.setImage(marImageGraph.RenderDataAsImage(false));
    }

    public void setViewportTitle(String title){
        viewportTitle.setText(title);
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    @Override
    protected void performInitializationTasks() {

    }

}
