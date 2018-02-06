package nl.markvanderwal.dvdcocoon.views;

import javafx.event.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.stage.*;
import nl.markvanderwal.dvdcocoon.*;
import nl.markvanderwal.dvdcocoon.models.*;
import nl.markvanderwal.dvdcocoon.services.*;

import java.io.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 3-2-2018
 */
public abstract class CocoonController extends AbstractFXMLViewController {

    protected void showValueForm(Event event, String name,
                                 ObservableService service, Class<? extends IdValueType> clazz) {
        ValueFormController controller = new ValueFormController(service, clazz);
        Stage stage = controller.createStage(injector);
        controller.setValueName(name);
        showModal(name, stage, event);
    }

    protected void showMovieForm(Event event) {
        showMovieForm(event, null);
    }

    protected void showMovieForm(Event event, Movie movie) {
        MovieFormController controller = injector
                .movieFormController().get();
        controller.setMovie(movie);
        Stage stage = controller.createStage(injector);
        showModal("Film", stage, event);
    }

    protected void showModal(String title, Stage controllerStage, Event event) {
        InputStream iconStream = getClass().getResourceAsStream("/icon.png");

        controllerStage.setResizable(false);
        controllerStage.getIcons().add(new Image(iconStream));
        controllerStage.initModality(Modality.APPLICATION_MODAL);
        controllerStage.setTitle(title);
        controllerStage.initOwner(((Node) event.getSource()).getScene().getWindow());
        controllerStage.showAndWait();
    }
}
