package nl.markvanderwal.dvdcocoon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.*;

/**
 * author: Mark van der Wal
 * date: 29-12-2017
 */
public class CocoonApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("DVDCocoon");

        initRootLayout(primaryStage);
    }

    private void initRootLayout(final Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("views/MainForm.fxml"));
            BorderPane rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            InputStream stream = getClass().getResourceAsStream("/icon.ico");
            primaryStage.getIcons().add(new Image(stream));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
