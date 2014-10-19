package mvvmbase.action;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.concurrent.Callable;

/*
 Generic typing syntax interpreted from the tutorial article:
 http://docs.oracle.com/javase/tutorial/java/generics/types.html
 as accessed on 10/19/2014.
 */

public class ActionDelegate<T> {

    /////////// Fields //////////////////////////////////////////////////////////////////////

    private Callable<T> action;

    /////////// Properties //////////////////////////////////////////////////////////////////

    private StringProperty identifier = new SimpleStringProperty();
    public final String getIdentifier(){ return this.identifier.get(); }
    public final void setIdentifier(String identifier){ this.identifier.set(identifier); }
    public StringProperty identifierProperty(){ return this.identifier; }

    /////////// Constructors ////////////////////////////////////////////////////////////////

    public ActionDelegate(String _id, Callable<T> _action){
        action = _action;
        setIdentifier(_id);
    }

    /////////// Public Methods //////////////////////////////////////////////////////////////

    public void invoke(){
        try {
            action.call();
        }
        catch (Exception e){
            System.out.println("Action could not be invoked.");
        }
    }

}
