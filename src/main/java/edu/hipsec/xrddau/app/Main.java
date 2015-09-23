package edu.hipsec.xrddau.app;

import com.quantumjockey.melya.application.ApplicationScaffold;
import com.quantumjockey.system.SystemAttributes;
import edu.hipsec.xrddau.app.mainwindow.MainWindowController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        String resourcesRoot = "mainwindow/MainWindow";

        ApplicationScaffold.createWithResourcesRoot(
                primaryStage,
                resourcesRoot,
                "XRD Data Analysis Utilities - " + SystemAttributes.OSName(),
                null,
                new MainWindowController(),
                this.getClass(),
                () -> {
                    if (SystemAttributes.OSName().contains("Windows")) {
                        primaryStage.requestFocus();
                        primaryStage.setMaximized(true);
                    }
                });

        primaryStage.show();
    }

    // Default application entry point
    public static void main(String[] args) {
        launch(args);
    }

}
