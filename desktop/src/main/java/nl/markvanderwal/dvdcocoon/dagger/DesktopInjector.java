package nl.markvanderwal.dvdcocoon.dagger;

import dagger.*;
import nl.markvanderwal.dvdcocoon.views.*;

import javax.inject.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
@Singleton
@Component(modules = {DesktopDatabaseModule.class, ServiceModule.class})
public interface DesktopInjector {

    Provider<MainFormController> mainFormController();
}
