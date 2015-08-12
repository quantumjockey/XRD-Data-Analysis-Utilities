package edu.hipsec.xrddau.app;

import edu.hipsec.xrddau.app.mainwindow.MainWindowController;
import com.quantumjockey.melya.window.initialization.WindowInitializer;
import javafx.application.Application;
import javafx.stage.Stage;
import com.quantumjockey.paths.SystemAttributes;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        String resourcesRoot = "mainwindow/MainWindow";
        WindowInitializer init = new WindowInitializer(resourcesRoot + "View.fxml", new MainWindowController(), this.getClass());
        init.appendCssStyles(resourcesRoot + "Styles.css", this.getClass());
        primaryStage.setTitle("XRD Data Analysis Utilities - " + SystemAttributes.OSName());
        primaryStage.setScene(init.getScene());
        if (SystemAttributes.OSName().contains("Windows")) {
            primaryStage.requestFocus();
            primaryStage.setMaximized(true);
        }
        primaryStage.show();
    }

    // Default application entry point
    public static void main(String[] args) {
        launch(args);
    }
    
}
