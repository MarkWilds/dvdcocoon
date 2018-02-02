package nl.markvanderwal.dvdcocoon;

import javafx.application.*;
import javafx.scene.image.*;
import javafx.stage.*;
import nl.markvanderwal.dvdcocoon.dagger.*;
import nl.markvanderwal.dvdcocoon.views.*;

import java.io.*;

/**
 * author: Mark van der Wal
 * date: 29-12-2017
 */
public class CocoonApplication extends Application {

    public static void main(String[] args) throws ClassNotFoundException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("DVDCocoon");

        ControllerInjector injector = DaggerControllerInjector.create();
        MainController controller = injector.mainController().get();
        Stage mainStage = controller.createStage();
        mainStage.show();
    }
}
