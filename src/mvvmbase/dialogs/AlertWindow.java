package mvvmbase.dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AlertWindow {

    /////////// Constants /////////////////////////////////////////////////////////////////////

    private final int BOX_PADDING = 20;
    private final int VERTICAL_SPACING = 15;

    /////////// Fields ////////////////////////////////////////////////////////////////////////

    private Stage alertDialog;

    /////////// Constructors //////////////////////////////////////////////////////////////////

    public AlertWindow(String title, String message){
        alertDialog = new Stage();
        alertDialog.setTitle(title);
        setStyles();
        generateLayout(message);
    }

    /////////// Public Methods ////////////////////////////////////////////////////////////////

    public void show(){
        alertDialog.show();
    }

    /////////// Private Methods ///////////////////////////////////////////////////////////////

    private void generateLayout(String text){
        VBox box = new VBox(VERTICAL_SPACING);
        Label messageText = new Label(text);
        Button response = new Button("Ok");
        response.setOnAction((event) -> alertDialog.close());
        box.getChildren().addAll(messageText, response);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(BOX_PADDING));
        alertDialog.setScene(new Scene(box));
    }

    private void setStyles(){
        alertDialog.initStyle(StageStyle.UNDECORATED);
        alertDialog.initModality(Modality.WINDOW_MODAL);
    }

}
