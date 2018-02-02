package nl.markvanderwal.dvdcocoon.dagger;

import dagger.*;
import javafx.fxml.*;
import nl.markvanderwal.dvdcocoon.views.*;

import javax.inject.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
@Singleton
@Component(modules = DatabaseModule.class)
public interface ControllerInjector {

    Provider<MainController> mainController();
}
