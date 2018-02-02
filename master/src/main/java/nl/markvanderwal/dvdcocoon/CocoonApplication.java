package nl.markvanderwal.dvdcocoon;

import javafx.application.*;
import javafx.stage.*;
import nl.markvanderwal.dvdcocoon.dagger.*;
import nl.markvanderwal.dvdcocoon.views.*;
import org.apache.logging.log4j.*;

/**
 * author: Mark van der Wal
 * date: 29-12-2017
 */
public class CocoonApplication extends Application {

    private static final Logger LOGGER = LogManager.getLogger(CocoonApplication.class);

    public static void main(String[] args) throws ClassNotFoundException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ControllerInjector injector = DaggerControllerInjector.create();
        MainController controller = injector.mainController().get();
        Stage mainStage = controller.createStage();
        mainStage.setTitle("DVDCocoon 2.0");
        mainStage.show();
    }
}
