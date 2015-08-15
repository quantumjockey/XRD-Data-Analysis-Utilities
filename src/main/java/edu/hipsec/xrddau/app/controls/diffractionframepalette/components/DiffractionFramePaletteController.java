package edu.hipsec.xrddau.app.controls.diffractionframepalette.components;

import com.quantumjockey.colorramps.GradientRamp;
import com.quantumjockey.melya.action.ActionViewModel;
import com.quantumjockey.melya.controls.standard.zoomableimageview.ZoomableImageView;
import com.quantumjockey.melya.markup.MarkupControllerBase;
import edu.hipsec.xrddau.app.controls.dataexportcontrol.DataExportControl;
import edu.hipsec.xrddau.app.controls.maskoptionscontrol.MaskOptionsControl;
import edu.hipsec.xrddau.app.controls.renderoptionscontrol.RenderOptionsControl;
import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import edu.hipsec.xrdtiffoperations.constants.FileTypes;
import edu.hipsec.xrddau.xrdvisualization.DiffractionFrameVisualizer;
import edu.hipsec.xrddau.xrdvisualization.masking.BoundedMask;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.util.ArrayList;

public class DiffractionFramePaletteController extends MarkupControllerBase {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final String RENDER_ERROR_MESSAGE = "Image render error!";

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private ZoomableImageView imageRender;

    @FXML
    private MaskOptionsControl maskOptions;

    @FXML
    private RenderOptionsControl renderOptions;

    @FXML
    private DataExportControl exportOptions;

    private DiffractionFrame cachedImage;
    private GradientRamp selectedRamp;
    private String selectedImageType;

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void loadFrameData(DiffractionFrame image) throws IOException {

        int priorLowerBnd = this.maskOptions.getController().getLowerBound();
        int priorUpperBnd = this.maskOptions.getController().getUpperBound();

        this.updateMaskLimiters(image);
        this.imageRender.getController().resetRotation();

        if (this.cachedImage != null) {
            int priorMax, priorMin;

            priorMax = this.cachedImage.getMaxValue();
            priorMin = this.cachedImage.getMinValue();

            if (this.maskOptions.getController().getStickyBounds()) {
                if (priorLowerBnd > this.cachedImage.getMinValue() && priorLowerBnd != priorMin)
                    this.maskOptions.getController().setLowerBound(priorLowerBnd);
                if (priorUpperBnd < this.cachedImage.getMaxValue() && priorUpperBnd != priorMax)
                    this.maskOptions.getController().setUpperBound(priorUpperBnd);
            }
        }

        this.cachedImage = image;
        this.renderFrameMapping(image, this.renderOptions.getController().getAdaptiveRender());
        this.updatePixelScale(image);
    }

    public void renderFrameMapping(DiffractionFrame image, boolean isAdaptive) throws IOException {
        if (image != null) {
            DiffractionFrameVisualizer marImageGraph = new DiffractionFrameVisualizer(image, this.selectedImageType);
            this.imageRender.getController().render(
                    marImageGraph.renderDataMapping(
                            this.selectedRamp,
                            new BoundedMask(
                                    this.maskOptions.getController().getLowerBound(),
                                    this.maskOptions.getController().getUpperBound(),
                                    this.maskOptions.getController().getMaskHue()),
                            isAdaptive
                    )
            );
        }
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private ArrayList<ActionViewModel<Void>> createExportSelections() {
        ArrayList<ActionViewModel<Void>> exportActions = new ArrayList<>();
        exportActions.add(new ActionViewModel<>(FileTypes.TIFF_32_BIT_INT, this::exportThirtyTwoBitIntImage));
        exportActions.add(new ActionViewModel<>(FileTypes.TIFF_32_BIT_FLOAT, this::exportThirtyTwoBitFloatImage));
        return exportActions;
    }

    private Void exportThirtyTwoBitIntImage() {
        double rotation = this.imageRender.getController().getImageRotation();
        this.cachedImage.rotate(rotation);
        this.exportOptions.getController().exportImageWithAttributes(
                this.cachedImage,
                FileTypes.TIFF_32_BIT_INT,
                this.maskOptions.getController().getLowerBound(),
                this.maskOptions.getController().getUpperBound()
        );
        return null;
    }

    private Void exportThirtyTwoBitFloatImage() {
        double rotation = this.imageRender.getController().getImageRotation();
        this.cachedImage.rotate(rotation);
        this.exportOptions.getController().exportImageWithAttributes(
                this.cachedImage,
                FileTypes.TIFF_32_BIT_FLOAT,
                this.maskOptions.getController().getLowerBound(),
                this.maskOptions.getController().getUpperBound()
        );
        return null;
    }

    private void updateFrameLoad() {
        try {
            this.loadFrameData(this.cachedImage);
        } catch (IOException ex) {
            System.out.println(this.RENDER_ERROR_MESSAGE);
        }
    }

    private void updateFrameRender() {
        try {
            this.renderFrameMapping(this.cachedImage, this.renderOptions.getController().getAdaptiveRender());
        } catch (IOException ex) {
            System.out.println(this.RENDER_ERROR_MESSAGE);
        }
    }

    private void updateMaskLimiters(DiffractionFrame image) {
        int max = image.getMaxValue();
        int min = image.getMinValue();
        this.maskOptions.getController().setLimiters(min, max);
    }

    private void updatePixelScale(DiffractionFrame image) {
        if (image == null)
            this.renderOptions.getController().setOffset(0);
        else
            this.renderOptions.getController().setOffset(Math.abs(image.getMinValue()));
    }

    /////////// Protected Methods /////////////////////////////////////////////////////////////

    @Override
    protected void setBindings() {

    }

    @Override
    protected void setDefaults() {
        this.updatePixelScale(this.cachedImage);
        this.exportOptions.getController().updateSelections(this.createExportSelections());
        this.selectedRamp = this.renderOptions.getController().getActiveRamp();
        this.selectedImageType = this.renderOptions.getController().getActiveImageType();
        this.imageRender.getController().addToCaption((y, x) -> "Intensity: " + this.cachedImage.getIntensityMapValue(y, x));
    }

    @Override
    protected void setEvents() {

    }

    @Override
    protected void setListeners() {

        ChangeListener<Boolean> onAdaptiveRenderingChange = (observable, oldValue, newValue) -> {
            if (newValue)
                this.maskOptions.getController().setMaskHue(this.renderOptions.getController().getActiveRamp().getRampColorValue(0.0));
            this.updateFrameRender();
        };

        ChangeListener<Color> onHueChange = (observable, oldValue, newValue) -> this.updateFrameRender();

        ChangeListener<String> onImageTypeChange = (observable, oldValue, newValue) -> {
            this.selectedImageType = newValue;
            this.updateFrameRender();
        };

        ChangeListener<GradientRamp> onRampChange = (observable, oldValue, newValue) -> {
            this.selectedRamp = newValue;
            this.updateFrameRender();
        };

        ChangeListener<Number> onScaleChange = (observable, oldValue, newValue) -> this.updateFrameRender();

        ChangeListener<Boolean> onStickyBoundsChange = (observable, oldValue, newValue) -> this.updateFrameLoad();

        this.renderOptions.getController().activeImageTypeProperty().addListener(onImageTypeChange);
        this.renderOptions.getController().activeRampProperty().addListener(onRampChange);
        this.renderOptions.getController().adaptiveRenderProperty().addListener(onAdaptiveRenderingChange);
        this.maskOptions.getController().lowerBoundProperty().addListener(onScaleChange);
        this.maskOptions.getController().upperBoundProperty().addListener(onScaleChange);
        this.maskOptions.getController().maskHueProperty().addListener(onHueChange);
        this.maskOptions.getController().stickyBoundsProperty().addListener(onStickyBoundsChange);
    }

}
