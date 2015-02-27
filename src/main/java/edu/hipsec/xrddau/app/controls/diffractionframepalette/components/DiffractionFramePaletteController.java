package edu.hipsec.xrddau.app.controls.diffractionframepalette.components;

import com.quantumjockey.colorramps.GradientRamp;
import com.quantumjockey.dialogs.FileSaveChooserWrapper;
import com.quantumjockey.mvvmbase.action.ActionDelegate;
import com.quantumjockey.mvvmbase.markup.MarkupControllerBase;
import edu.hipsec.xrddau.app.controls.dataexportcontrol.DataExportControl;
import edu.hipsec.xrddau.app.controls.diffractionframerender.DiffractionFrameRender;
import edu.hipsec.xrddau.app.controls.maskoptionscontrol.MaskOptionsControl;
import edu.hipsec.xrddau.app.controls.renderoptionscontrol.RenderOptionsControl;
import edu.hipsec.xrddau.app.controls.zoomcontrol.ZoomControl;
import edu.hipsec.xrddau.app.filesystem.FileSysWriter;
import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import edu.hipsec.xrdtiffoperations.imagemodel.FileTypes;
import edu.hipsec.xrdtiffoperations.math.DataMasking;
import edu.hipsec.xrdtiffvisualization.masking.BoundedMask;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DiffractionFramePaletteController extends MarkupControllerBase {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final double DEFAULT_ZOOM_MAX = 6.0;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private DiffractionFrameRender imageRender;

    @FXML
    private MaskOptionsControl maskOptions;

    @FXML
    private RenderOptionsControl renderOptions;

    @FXML
    private DataExportControl exportOptions;

    @FXML
    private ZoomControl imageZoom;

    private DiffractionFrame cachedImage;
    private GradientRamp selectedRamp;

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void loadFrameData(DiffractionFrame image) throws IOException {

        int priorLowerBnd = this.maskOptions.getController().getLowerBound();
        int priorUpperBnd = this.maskOptions.getController().getUpperBound();

        this.updateMaskLimiters(image);

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
        this.updateZoomScale(image);
    }

    public void renderFrameMapping(DiffractionFrame image, boolean isAdaptive) throws IOException {

        this.imageRender.getController().renderFrame(image, this.selectedRamp,
                new BoundedMask(
                        this.maskOptions.getController().getLowerBound(),
                        this.maskOptions.getController().getUpperBound(),
                        this.maskOptions.getController().getMaskHue()),
                isAdaptive
        );
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void exportImage(String imageType) {

        FileSaveChooserWrapper dialog = new FileSaveChooserWrapper("Save to...");
        int maskLb = this.maskOptions.getController().getLowerBound();
        int maskUb = this.maskOptions.getController().getUpperBound();
        boolean isMasked = (this.cachedImage.getMaxValue() != maskUb || this.cachedImage.getMinValue() != maskLb);

        if (isMasked) {
            DataMasking.maskImage(this.cachedImage, maskLb, maskUb);
            dialog.setInitialFileName(this.cachedImage.getIdentifier());
        } else
            dialog.setInitialFileName(this.cachedImage.getIdentifier());

        File destination = dialog.getSaveDirectory();

        FileSysWriter.writeImageData(destination, this.cachedImage, imageType);
    }

    private Void exportThirtyTwoBitIntImage() {
        this.exportImage(FileTypes.TIFF_32_BIT_INT);
        return null;
    }

    private Void exportThirtyTwoBitFloatImage() {
        this.exportImage(FileTypes.TIFF_32_BIT_FLOAT);
        return null;
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

    private void updateZoomScale(DiffractionFrame image) {
        if (image != null) {
            int size = (image.getHeight() >= image.getWidth()) ? image.getHeight() : image.getWidth();
            double viewportSize = this.imageRender.getController().getScrollViewport().getWidth() - 2;
            double scale = viewportSize / (double) size;
            this.imageZoom.getController().setZoomBounds(scale, DEFAULT_ZOOM_MAX);
            this.imageZoom.getController().setZoomLevel(scale);
        }
    }

    /////////// Protected Methods /////////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {

    }

    @Override
    protected void setBindings() {
        this.imageZoom.getController().maxZoomProperty().bindBidirectional(this.imageRender.getController().maxZoomProperty());
        this.imageZoom.getController().minZoomProperty().bindBidirectional(this.imageRender.getController().minZoomProperty());
        this.imageZoom.getController().zoomLevelProperty().bindBidirectional(this.imageRender.getController().zoomLevelProperty());
    }

    @Override
    protected void setDefaults() {
        this.updatePixelScale(this.cachedImage);
        ArrayList<ActionDelegate<Void>> exportActions;
        exportActions = new ArrayList<>();
        exportActions.add(new ActionDelegate<>(FileTypes.TIFF_32_BIT_INT, this::exportThirtyTwoBitIntImage));
        exportActions.add(new ActionDelegate<>(FileTypes.TIFF_32_BIT_FLOAT, this::exportThirtyTwoBitFloatImage));
        this.exportOptions.getController().updateSelections(exportActions);
        this.selectedRamp = this.renderOptions.getController().getActiveRamp();
    }

    @Override
    protected void setEvents() {

    }

    @Override
    protected void setListeners() {

        ChangeListener<Boolean> onAdaptiveRenderingChange = (observable, oldValue, newValue) -> {
            try {
                if (newValue)
                    this.maskOptions.getController().setMaskHue(this.renderOptions.getController().getActiveRamp().getRampColorValue(0.0));
                this.renderFrameMapping(this.cachedImage, newValue);
            } catch (IOException ex) {
                System.out.println("Image render error!");
            }
        };

        ChangeListener<Color> onHueChange = (observable, oldValue, newValue) -> {
            try {
                this.renderFrameMapping(this.cachedImage, this.renderOptions.getController().getAdaptiveRender());
            } catch (IOException ex) {
                System.out.println("Image render error!");
            }
        };

        ChangeListener<GradientRamp> onRampChange = (observable, oldValue, newValue) -> {
            try {
                this.selectedRamp = newValue;
                this.renderFrameMapping(this.cachedImage, this.renderOptions.getController().getAdaptiveRender());
            } catch (IOException ex) {
                System.out.println("Image render error!");
            }
        };

        ChangeListener<Number> onScaleChange = (observable, oldValue, newValue) -> {
            try {
                this.renderFrameMapping(this.cachedImage, this.renderOptions.getController().getAdaptiveRender());
            } catch (IOException ex) {
                System.out.println("Image render error!");
            }
        };

        ChangeListener<Boolean> onStickyBoundsChange = (observable, oldValue, newValue) -> {
            try {
                this.loadFrameData(this.cachedImage);
            } catch (IOException ex) {
                System.out.println("Image render error!");
            }
        };

        this.renderOptions.getController().activeRampProperty().addListener(onRampChange);
        this.renderOptions.getController().adaptiveRenderProperty().addListener(onAdaptiveRenderingChange);
        this.maskOptions.getController().lowerBoundProperty().addListener(onScaleChange);
        this.maskOptions.getController().upperBoundProperty().addListener(onScaleChange);
        this.maskOptions.getController().maskHueProperty().addListener(onHueChange);
        this.maskOptions.getController().stickyBoundsProperty().addListener(onStickyBoundsChange);
    }

}