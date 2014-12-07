package app.filesystem;

import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.filehandling.io.TiffWriter;
import java.io.File;

public class FileSysWriter {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public static void writeImageData(File path, MARTiffImage image, String type){
        if (path != null) {
            TiffWriter writer = new TiffWriter(image);
            writer.write(path.getPath(), type);
        }
    }

}
