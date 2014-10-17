package app;

import app.mainwindow.Controller;
import mvvmbase.initialization.WindowInitializer;
import javafx.application.Application;
import javafx.stage.Stage;
import pathoperations.SystemAttributes;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        WindowInitializer init = new WindowInitializer("mainwindow/view.fxml", new Controller(), this.getClass());
        primaryStage.setTitle("XRD Data Analysis Utilities - " + SystemAttributes.OS_NAME);
        primaryStage.setScene(init.GetScene());
        primaryStage.show();
    }

    // Default application entry point
    public static void main(String[] args){
        launch(args);
    }
}
