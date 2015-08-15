package edu.hipsec.xrddau.xrdvisualization;

import edu.hipsec.xrddau.xrdvisualization.engine.FalseColorMappingEngine;
import edu.hipsec.xrddau.xrdvisualization.engine.GradientMappingEngine;
import edu.hipsec.xrddau.xrdvisualization.engine.base.DataMappingEngine;
import edu.hipsec.xrddau.xrdvisualization.masking.BoundedMask;
import javafx.scene.image.*;
import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import com.quantumjockey.colorramps.GradientRamp;
import java.io.IOException;

public class DiffractionFrameVisualizer {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private DiffractionFrame data;
    private String imageType;

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public DiffractionFrameVisualizer(DiffractionFrame imageData, String imageType) {
        this.data = imageData;
        this.imageType = imageType;
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public Image renderDataMapping(GradientRamp _ramp, BoundedMask mask, boolean adaptive) throws IOException {
        DataMappingEngine mappingEngine;
        switch (this.imageType) {

            case ImageTypes.GRADIENT_MAPPING:
                mappingEngine = new GradientMappingEngine();
                break;

            default:
                mappingEngine = new FalseColorMappingEngine();
                // Equivalent to ImageTypes.FALSE_COLOR_MAPPING

        }
        return mappingEngine.renderData(this.data, _ramp, mask, adaptive);
    }

}
