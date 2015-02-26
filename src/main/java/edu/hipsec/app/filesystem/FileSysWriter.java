package edu.hipsec.app.filesystem;

import xrdtiffoperations.data.DiffractionFrame;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.filehandling.io.TiffWriter;
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
