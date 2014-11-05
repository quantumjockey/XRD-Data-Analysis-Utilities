package app.filesystem;

import filesystembase.paths.PathWrapper;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.filehandling.io.TiffReader;
import java.io.IOException;
import java.nio.file.Paths;

public class FileSysReader {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public static MARTiffImage readImageData(PathWrapper imagePath) throws IOException {
        MARTiffImage temp = null;
        if (imagePath != null) {
            TiffReader marImageReader = new TiffReader(Paths.get(imagePath.getInjectedPath()));
            marImageReader.readFileData();
            temp = marImageReader.getImageData();
        }
        return temp;
    }

}
