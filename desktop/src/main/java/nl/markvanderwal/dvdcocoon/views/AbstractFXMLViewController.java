package nl.markvanderwal.dvdcocoon.views;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import nl.markvanderwal.dvdcocoon.dagger.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 2-2-2018
 */
abstract class AbstractFXMLViewController implements Initializable {

    protected DesktopInjector injector;
    private Parent fxmlRoot;

    /**
     * Gets the URL to the FXML file describing the view presented by this controller.<br/>
     * <p>
     * A default implementation would look like this:<br/>
     * <code>
     * return getClass().getResource("/myView.fxml");
     * </code>
     *
     * @return FXML resource URL
     */
    protected abstract URL getFxmlResourceUrl();

    /**
     * @return Localization bundle for the FXML labels or <code>null</code>.
     */
    protected abstract ResourceBundle getFxmlResourceBundle();

    /**
     * Creates a FXML loader used in {@link #loadFxml()}. This method can be overwritten for further loader customization.
     *
     * @return Configured loader ready to load.
     */
    protected FXMLLoader createFxmlLoader() {
        final URL fxmlUrl = getFxmlResourceUrl();
        final ResourceBundle rb = getFxmlResourceBundle();
        final FXMLLoader loader = new FXMLLoader(fxmlUrl, rb);
        loader.setController(this);
        return loader;
    }

    /**
     * Loads the view presented by this controller from the FXML file return by {@link #getFxmlResourceUrl()}. This method can only be invoked once.
     *
     * @return Parent view element.
     */
    protected final synchronized Parent loadFxml() {
        if (fxmlRoot == null) {
            final FXMLLoader loader = createFxmlLoader();
            try {
                fxmlRoot = loader.load();
            } catch (IOException e) {
                throw new IllegalStateException("Could not load FXML file from location: " + loader.getLocation(), e);
            }
        }
        return fxmlRoot;
    }

    /**
     * @return New stage using a scene with the root node from the FXML file.
     */
    public Stage createStage(final DesktopInjector injector) {
        return createStage(null, injector);
    }

    /**
     * @return New stage using a scene with the root node from the FXML file.
     */
    public Stage createStage(final Stage parent, final DesktopInjector injector) {
        this.injector = injector;
        final Parent root = loadFxml();

        Stage stage = parent;
        if (stage == null) {
            stage = new Stage();
        }

        stage.setScene(new Scene(root));
        stage.sizeToScene();
        return stage;
    }

}
