package edu.hipsec.app.controls.diffractionframeworkspace.components;

import edu.hipsec.app.controls.dataexportcontrol.DataExportControl;
import edu.hipsec.app.controls.diffractionframerender.DiffractionFrameRender;
import edu.hipsec.app.filesystem.FileSysReader;
import edu.hipsec.app.filesystem.FileSysWriter;
import edu.hipsec.app.controls.zoomcontrol.ZoomControl;
import com.quantumjockey.dialogs.FileSaveChooserWrapper;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import com.quantumjockey.mvvmbase.action.ActionDelegate;
import com.quantumjockey.mvvmbase.markup.MarkupControllerBase;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import edu.hipsec.app.controls.maskoptionscontrol.MaskOptionsControl;
import edu.hipsec.app.controls.renderoptionscontrol.RenderOptionsControl;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.paint.Color;
import com.quantumjockey.paths.PathWrapper;
import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import edu.hipsec.xrdtiffoperations.imagemodel.FileTypes;
import edu.hipsec.xrdtiffoperations.math.DataMasking;
import com.quantumjockey.colorramps.GradientRamp;
import edu.hipsec.xrdtiffvisualization.masking.BoundedMask;

public class DiffractionFrameWorkspaceController extends MarkupControllerBase {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    private final double DEFAULT_ZOOM_MAX = 6.0;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private LineChart diffractionPattern;

    @FXML
    private TitledPane viewportTitle;

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

    public void renderImageFromFile(PathWrapper filePath) throws IOException {
        cachedImage = FileSysReader.readImageData(filePath);
        renderImageOnLoad(cachedImage);
    }

    public void renderImageOnLoad(DiffractionFrame image) throws IOException {

        int priorLowerBnd = maskOptions.getController().getLowerBound();
        int priorUpperBnd = maskOptions.getController().getUpperBound();

        updateMaskLimiters(image);

        if (cachedImage != null) {
            int priorMax, priorMin;

            priorMax = cachedImage.getMaxValue();
            priorMin = cachedImage.getMinValue();

            if (maskOptions.getController().getStickyBounds()) {
                if (priorLowerBnd > cachedImage.getMinValue() && priorLowerBnd != priorMin)
                    maskOptions.getController().setLowerBound(priorLowerBnd);
                if (priorUpperBnd < cachedImage.getMaxValue() && priorUpperBnd != priorMax)
                    maskOptions.getController().setUpperBound(priorUpperBnd);
            }
        }

        cachedImage = image;
        renderImageWithMask(image, renderOptions.getController().getAdaptiveRender());
        updatePixelScale(image);
        updateZoomScale(image);
        viewportTitle.setText(image.getIdentifier());
    }

    public void renderImageWithMask(DiffractionFrame image, boolean isAdaptive) throws IOException {

        imageRender.getController().renderFrame(image, selectedRamp,
                new BoundedMask(
                        maskOptions.getController().getLowerBound(),
                        maskOptions.getController().getUpperBound(),
                        maskOptions.getController().getMaskHue()),
                isAdaptive
        );
//        if (!(diffractionPattern.getData().size() > 1))
//            updateDiffractionPattern();
    }

    public void updateDiffractionPattern() {
        diffractionPattern.getData().clear();
        diffractionPattern.getData().add(integrateDiffractionPattern());
    }

    public XYChart.Series integrateDiffractionPattern() {
        XYChart.Series dataSet = new XYChart.Series<>();

//        cachedImage.cycleFramePixels((x, y) -> {
//            if (y != 0 && x % 128 == 0)
//                dataSet.getData().add(new XYChart.Data<>(x, cachedImage.getIntensityMapValue(y, x)));
//        });

        return dataSet;
    }

    /////////// Private Methods /////////////////////////////////////////////////////////////

    private void exportImage(String imageType) {

        FileSaveChooserWrapper dialog = new FileSaveChooserWrapper("Save to...");
        int maskLb = maskOptions.getController().getLowerBound();
        int maskUb = maskOptions.getController().getUpperBound();
        boolean isMasked = (cachedImage.getMaxValue() != maskUb || cachedImage.getMinValue() != maskLb);

        if (isMasked) {
            DataMasking.maskImage(cachedImage, maskLb, maskUb);
            dialog.setInitialFileName(cachedImage.getIdentifier());
        } else
            dialog.setInitialFileName(cachedImage.getIdentifier());

        File destination = dialog.getSaveDirectory();

        FileSysWriter.writeImageData(destination, cachedImage, imageType);
    }

    private Void exportThirtyTwoBitIntImage() {
        exportImage(FileTypes.TIFF_32_BIT_INT);
        return null;
    }

    private Void exportThirtyTwoBitFloatImage() {
        exportImage(FileTypes.TIFF_32_BIT_FLOAT);
        return null;
    }

    private void updateMaskLimiters(DiffractionFrame image) {
        int max = image.getMaxValue();
        int min = image.getMinValue();
        maskOptions.getController().setLimiters(min, max);
    }

    private void updatePixelScale(DiffractionFrame image) {
        if (image == null)
            renderOptions.getController().setOffset(0);
        else
            renderOptions.getController().setOffset(Math.abs(image.getMinValue()));
    }

    private void updateZoomScale(DiffractionFrame image) {
        if (image != null) {
            int size = (image.getHeight() >= image.getWidth()) ? image.getHeight() : image.getWidth();
            double viewportSize = imageRender.getController().getScrollViewport().getWidth() - 2;
            double scale = viewportSize / (double) size;
            imageZoom.getController().setZoomBounds(scale, DEFAULT_ZOOM_MAX);
            imageZoom.getController().setZoomLevel(scale);
        }
    }

    /////////// Protected Methods /////////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {

    }

    @Override
    protected void setBindings() {
        imageZoom.getController().maxZoomProperty().bindBidirectional(imageRender.getController().maxZoomProperty());
        imageZoom.getController().minZoomProperty().bindBidirectional(imageRender.getController().minZoomProperty());
        imageZoom.getController().zoomLevelProperty().bindBidirectional(imageRender.getController().zoomLevelProperty());
    }

    @Override
    protected void setDefaults() {
        updatePixelScale(cachedImage);
        ArrayList<ActionDelegate<Void>> exportActions;
        exportActions = new ArrayList<>();
        exportActions.add(new ActionDelegate<>(FileTypes.TIFF_32_BIT_INT, this::exportThirtyTwoBitIntImage));
        exportActions.add(new ActionDelegate<>(FileTypes.TIFF_32_BIT_FLOAT, this::exportThirtyTwoBitFloatImage));
        exportOptions.getController().updateSelections(exportActions);
        selectedRamp = renderOptions.getController().getActiveRamp();
        viewportTitle.setText("(No Data Selected)");
    }

    @Override
    protected void setEvents() {

    }

    @Override
    protected void setListeners() {

        ChangeListener<Boolean> onAdaptiveRenderingChange = (observable, oldValue, newValue) -> {
            try {
                if (newValue)
                    maskOptions.getController().setMaskHue(renderOptions.getController().getActiveRamp().getRampColorValue(0.0));
                renderImageWithMask(cachedImage, newValue);
            } catch (IOException ex) {
                System.out.println("Image render error!");
            }
        };

        ChangeListener<Color> onHueChange = (observable, oldValue, newValue) -> {
            try {
                renderImageWithMask(cachedImage, renderOptions.getController().getAdaptiveRender());
            } catch (IOException ex) {
                System.out.println("Image render error!");
            }
        };

        ChangeListener<GradientRamp> onRampChange = (observable, oldValue, newValue) -> {
            try {
                selectedRamp = newValue;
                renderImageWithMask(cachedImage, renderOptions.getController().getAdaptiveRender());
            } catch (IOException ex) {
                System.out.println("Image render error!");
            }
        };

        ChangeListener<Number> onScaleChange = (observable, oldValue, newValue) -> {
            try {
                renderImageWithMask(cachedImage, renderOptions.getController().getAdaptiveRender());
            } catch (IOException ex) {
                System.out.println("Image render error!");
            }
        };

        ChangeListener<Boolean> onStickyBoundsChange = (observable, oldValue, newValue) -> {
            try {
                renderImageOnLoad(cachedImage);
            } catch (IOException ex) {
                System.out.println("Image render error!");
            }
        };

        renderOptions.getController().activeRampProperty().addListener(onRampChange);
        renderOptions.getController().adaptiveRenderProperty().addListener(onAdaptiveRenderingChange);
        maskOptions.getController().lowerBoundProperty().addListener(onScaleChange);
        maskOptions.getController().upperBoundProperty().addListener(onScaleChange);
        maskOptions.getController().maskHueProperty().addListener(onHueChange);
        maskOptions.getController().stickyBoundsProperty().addListener(onStickyBoundsChange);
    }

}
