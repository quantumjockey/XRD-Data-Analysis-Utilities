package pathoperations;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PathWrapper {

    /////////// Properties //////////////////////////////////////////////////////////////////

    protected StringProperty injectedPath = new SimpleStringProperty();
    public final String getInjectedPath() {
        return injectedPath.get();
    }
    public final void setInjectedPath(String value){
        injectedPath.set(value);
    }
    public StringProperty injectedPathProperty() {
        return injectedPath;
    }

    protected StringProperty pathHead = new SimpleStringProperty();
    public final String getPathHead() {
        return pathHead.get();
    }
    public final void setPathHead(String value){
        pathHead.set(value);
    }
    public StringProperty pathHeadProperty() {
        return pathHead;
    }

    protected StringProperty pathTail = new SimpleStringProperty();
    public final String getPathTail() {
        return pathTail.get();
    }
    public final void setPathTail(String value){
        pathTail.set(value);
    }
    public StringProperty pathTailProperty() {
        return pathTail;
    }

    /////////// Constructor(s) ////////////////////////////////////////////////////////////////

    public PathWrapper(String _injectedPath){
        this.setInjectedPath(_injectedPath);
        ParsePath(_injectedPath);
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private void ParsePath(String path){
        String[] pathTemp = path.split(SystemAttributes.FILE_SEPARATOR);
        int numComponents = pathTemp.length;
        this.setPathTail(pathTemp[numComponents - 1]);
        for (int i = 0; i < numComponents - 2; i++){
            this.setPathHead(SystemAttributes.FILE_SEPARATOR + pathTemp[i]);
        }
    }

}
