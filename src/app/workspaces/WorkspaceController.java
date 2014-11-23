package app.workspaces;

import paths.PathWrapper;

import java.util.ArrayList;

public interface WorkspaceController {

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void updateControls(ArrayList<PathWrapper> newItems, String root);

}
