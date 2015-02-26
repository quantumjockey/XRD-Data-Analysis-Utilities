package edu.hipsec.xrddau.app.filesystem;

import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import edu.hipsec.xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import edu.hipsec.xrdtiffoperations.filehandling.io.TiffWriter;
import java.io.File;

public class FileSysWriter {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public static void writeImageData(File path, DiffractionFrame image, String type) {
        if (path != null) {
            TiffWriter writer = new TiffWriter(new MARTiffImage(image), type);
            writer.write(path.getPath());
        }
    }

}
