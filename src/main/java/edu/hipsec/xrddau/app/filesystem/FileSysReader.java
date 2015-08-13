package edu.hipsec.xrddau.app.filesystem;

import com.quantumjockey.filesystem.paths.PathWrapper;
import edu.hipsec.xrdtiffoperations.data.DiffractionFrame;
import edu.hipsec.xrdtiffoperations.file.martiff.MARTiffImage;
import edu.hipsec.xrdtiffoperations.file.martiff.io.TiffReader;
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
