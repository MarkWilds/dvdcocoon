package nl.markvanderwal.dvdcocoon.views;

import javafx.fxml.*;
import javafx.scene.control.*;
import nl.markvanderwal.dvdcocoon.*;
import nl.markvanderwal.dvdcocoon.models.*;
import nl.markvanderwal.dvdcocoon.services.*;

import javax.inject.*;
import java.net.*;
import java.util.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 3-2-2018
 */
public class MovieFormController extends AbstractFXMLViewController {
    private MovieService movieService;
    private MediumService mediumService;
    private GenreService genreService;
    private MovieGenreService movieGenreService;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField labelTextField;

    @FXML
    private TextArea castTextArea;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button mediumButton;

    @FXML
    private Button genresButton;

    @FXML
    private Button saveEditButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ListView<Medium> mediumListView;

    @FXML
    private ListView<Genre> genreListView;

    @Inject
    public MovieFormController(MovieService movieService, MediumService mediumService,
                               GenreService genreService, MovieGenreService movieGenreService) {
        this.movieService = movieService;
        this.mediumService = mediumService;
        this.genreService = genreService;
        this.movieGenreService = movieGenreService;
    }

    @Override
    protected URL getFxmlResourceUrl() {
        return getClass().getResource("MovieForm.fxml");
    }

    @Override
    protected ResourceBundle getFxmlResourceBundle() {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
