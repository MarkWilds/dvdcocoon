package nl.markvanderwal.dvdcocoon.views;

import javafx.fxml.*;
import javafx.scene.control.*;
import nl.markvanderwal.dvdcocoon.services.*;

import javax.inject.*;
import java.net.*;
import java.util.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
public class MainController extends AbstractFXMLViewController {

    private final MovieService movieService;

    @FXML
    private Button button;

    @Inject
    public MainController(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    protected URL getFxmlResourceUrl() {
        return getClass().getResource("MainForm.fxml");
    }

    @Override
    protected ResourceBundle getFxmlResourceBundle() {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
