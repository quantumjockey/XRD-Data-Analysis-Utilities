package app.filesystem;

import filesystembase.paths.PathWrapper;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.filehandling.io.TiffReader;
import java.io.IOException;

public class FileSysReader {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public static MARTiffImage readImageData(PathWrapper imagePath) throws IOException {
        MARTiffImage temp = null;
        if (imagePath != null) {
            TiffReader marImageReader = new TiffReader(imagePath.getInjectedPath());
            marImageReader.readFileData();
            temp = marImageReader.getImageData();
        }
        return temp;
    }

}
