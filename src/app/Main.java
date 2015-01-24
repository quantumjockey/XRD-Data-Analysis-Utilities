package app;

import app.mainwindow.MainWindowController;
import com.quantumjockey.mvvmbase.window.initialization.WindowInitializer;
import javafx.application.Application;
import javafx.stage.Stage;
import com.quantumjockey.paths.SystemAttributes;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        WindowInitializer init = new WindowInitializer("mainwindow/MainWindowView.fxml", new MainWindowController(), this.getClass());
        primaryStage.setTitle("XRD Data Analysis Utilities - " + SystemAttributes.OS_NAME);
        primaryStage.setScene(init.getScene());
        if (SystemAttributes.OS_NAME.contains("Windows")){
            primaryStage.requestFocus();
            primaryStage.setMaximized(true);
        }
        primaryStage.show();
    }

    // Default application entry point
    public static void main(String[] args){
        launch(args);
    }
}
