package app.martiffviewport;

import MvvmBase.markup.MarkupControllerBase;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffvisualization.MARTiffVisualizer;

public class MARTiffViewportController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private ImageView imageViewport;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public MARTiffViewportController(){

    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public ImageView getViewport(){
        return imageViewport;
    }

    public void RenderImage(MARTiffImage image) throws IOException {
        MARTiffVisualizer marImageGraph = new MARTiffVisualizer(image);
        imageViewport.setImage(marImageGraph.RenderDataAsImage(false));
    }

}
