package edu.hipsec.xrddau.app.controls.diffractionframeworkspace.components;

import edu.hipsec.xrddau.app.controls.diffractionframepalette.DiffractionFramePalette;
import edu.hipsec.xrddau.app.filesystem.FileSysReader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import com.quantumjockey.mvvmbase.markup.MarkupControllerBase;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import com.quantumjockey.paths.PathWrapper;
import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;

public class DiffractionFrameWorkspaceController extends MarkupControllerBase {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    @FXML
    private LineChart diffractionPattern;

    @FXML
    private TitledPane viewportTitle;

    @FXML
    private DiffractionFramePalette framePalette;

    private DiffractionFrame cachedImage;

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void renderImageData(DiffractionFrame image) throws IOException {
        this.cachedImage = image;
        this.framePalette.getController().loadFrameData(this.cachedImage);
        this.viewportTitle.setText(this.cachedImage.getIdentifier());
    }

    public void renderImageFromFile(PathWrapper filePath) throws IOException {
        this.renderImageData(FileSysReader.readImageData(filePath));
    }

    public void updateDiffractionPattern() {
        if (!(diffractionPattern.getData().size() > 1)) {
            diffractionPattern.getData().clear();
            diffractionPattern.getData().add(integrateDiffractionPattern());
        }
    }

    public XYChart.Series integrateDiffractionPattern() {
        XYChart.Series dataSet = new XYChart.Series<>();

        cachedImage.cycleFramePixels((x, y) -> {
            if (y != 0 && x % 128 == 0)
                dataSet.getData().add(new XYChart.Data<>(x, cachedImage.getIntensityMapValue(y, x)));
        });

        return dataSet;
    }
    
    /////////// Protected Methods /////////////////////////////////////////////////////////////

    @Override
    protected void createCustomControls() {

    }

    @Override
    protected void setBindings() {

    }

    @Override
    protected void setDefaults() {
        this.viewportTitle.setText("(No Data Selected)");
    }

    @Override
    protected void setEvents() {

    }

    @Override
    protected void setListeners() {

    }

}
