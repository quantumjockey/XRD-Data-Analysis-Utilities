package edu.hipsec.app.workspaces;

import com.quantumjockey.paths.PathWrapper;

import java.util.ArrayList;

public interface WorkspaceController {

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void updateControls(ArrayList<PathWrapper> newItems, String root);

}
