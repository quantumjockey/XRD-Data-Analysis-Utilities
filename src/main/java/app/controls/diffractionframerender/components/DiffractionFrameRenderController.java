package app.controls.diffractionframerender.components;

import com.quantumjockey.mvvmbase.markup.MarkupControllerBase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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
        pixelTrack.setFont(Font.font(null, FontWeight.BOLD, 13));
    }

    @Override
    protected void setListeners() {

    }

}
