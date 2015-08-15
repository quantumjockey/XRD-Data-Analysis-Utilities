package edu.hipsec.xrddau.xrdvisualization.engine.base;

import com.quantumjockey.colorramps.GradientRamp;
import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import edu.hipsec.xrddau.xrdvisualization.masking.BoundedMask;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class DataMappingEngine {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    protected final Color[] DEFAULT_RAMP = {Color.BLACK, Color.VIOLET, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED}; // "Spectrum" Ramp

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public abstract Image renderData(DiffractionFrame data, GradientRamp ramp, BoundedMask mask, boolean adaptive);

}
