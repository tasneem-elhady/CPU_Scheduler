package com.cpuscheduler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class scheduler_app extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(scheduler_app.class.getResource("mainFXML.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("CPU Scheduler Simulator");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("ICON.jpg")));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}