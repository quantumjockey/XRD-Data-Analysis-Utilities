package edu.hipsec.concurrency;

import javafx.application.Platform;

public class BackgroundTask {

    public static void execute(Runnable action){
        new Thread(() -> Platform.runLater(action)).start();
    }

}
