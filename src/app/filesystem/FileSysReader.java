package app.filesystem;

import com.quantumjockey.paths.PathWrapper;
import xrdtiffoperations.data.DiffractionFrame;
import xrdtiffoperations.imagemodel.martiff.MARTiffImage;
import xrdtiffoperations.filehandling.io.TiffReader;
import java.io.IOException;
import java.nio.file.Paths;

public class FileSysReader {

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public static DiffractionFrame readImageData(PathWrapper imagePath) throws IOException {
        MARTiffImage temp = null;
        if (imagePath != null) {
            TiffReader marImageReader = new TiffReader(Paths.get(imagePath.getInjectedPath()));
            marImageReader.readFileData();
            temp = marImageReader.getImageData();
        }
        return temp.getDiffractionData();
    }

}
