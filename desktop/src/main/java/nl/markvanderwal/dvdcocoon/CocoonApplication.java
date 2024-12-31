package nl.markvanderwal.dvdcocoon;

import javafx.application.*;
import javafx.scene.image.*;
import javafx.stage.*;
import nl.markvanderwal.dvdcocoon.dagger.*;
import nl.markvanderwal.dvdcocoon.views.*;
import org.apache.logging.log4j.*;

import java.io.*;

/**
 * author: Mark van der Wal
 * date: 29-12-2017
 */
public class CocoonApplication extends Application {

    private static final Logger LOGGER = LogManager.getLogger(CocoonApplication.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DesktopInjector injector = DaggerDesktopInjector.create();
        MainFormController controller = injector.mainFormController().get();
        Stage mainStage = controller.createStage(primaryStage, injector);

        InputStream iconStream = getClass().getResourceAsStream("/icon.ico");
        mainStage.getIcons().add(new Image(iconStream));

        mainStage.setTitle("DVDCocoon 2.0");
        mainStage.show();
    }
}
