package app;

import app.mainwindow.MainWindowController;
import mvvmbase.window.initialization.WindowInitializer;
import javafx.application.Application;
import javafx.stage.Stage;
import filesystembase.paths.SystemAttributes;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        WindowInitializer init = new WindowInitializer("mainwindow/MainWindowView.fxml", new MainWindowController(), this.getClass());
        primaryStage.setTitle("XRD Data Analysis Utilities - " + SystemAttributes.OS_NAME);
        primaryStage.setScene(init.getScene());
        primaryStage.show();
    }

    // Default application entry point
    public static void main(String[] args){
        launch(args);
    }
}
