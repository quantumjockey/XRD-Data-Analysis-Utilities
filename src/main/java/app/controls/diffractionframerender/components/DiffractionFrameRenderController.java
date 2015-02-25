package app.controls.diffractionframerender.components;

import com.quantumjockey.mvvmbase.markup.MarkupControllerBase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;

public class DiffractionFrameRenderController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private ImageView imageViewport;

    @FXML
    private Label pixelTrack;

    @FXML
    private ScrollPane scrollViewport;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public ImageView getImageViewport(){
        return imageViewport;
    }

    public Label getPixelTrack(){
        return pixelTrack;
    }

    public ScrollPane getScrollViewport(){
        return scrollViewport;
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {

    }

    @Override
    protected void setBindings() {

    }

    @Override
    protected void setDefaults() {

    }

    @Override
    protected void setListeners() {

    }

}
