package pathoperations.filters;

import java.io.File;
import java.io.FilenameFilter;

public class FilterWrapper {

    /////////////////////////////////////////////////////////////////
    ////////////// REQUIRES EDITING FOR GENERALIZATION //////////////
    /////////////////////////////////////////////////////////////////

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    public FilenameFilter filter;

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public FilterWrapper(final String[] filterStrings){
        filter = new FilenameFilter(){
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(filterStrings[0]) || lowercaseName.endsWith(filterStrings[1])) {
                    return true;
                } else {
                    return false;
                }
            }
        };
    }

}
