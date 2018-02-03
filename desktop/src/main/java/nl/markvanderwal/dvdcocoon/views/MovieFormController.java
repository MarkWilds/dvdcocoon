package nl.markvanderwal.dvdcocoon.views;

import javafx.fxml.*;
import javafx.scene.control.*;
import nl.markvanderwal.dvdcocoon.exceptions.*;
import nl.markvanderwal.dvdcocoon.models.*;
import nl.markvanderwal.dvdcocoon.services.*;
import org.apache.logging.log4j.*;

import javax.inject.*;
import java.net.*;
import java.util.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 3-2-2018
 */
public class MovieFormController extends CocoonController {

    private static final Logger LOGGER = LogManager.getLogger(ValueFormController.class);

    private MovieService movieService;
    private MediumService mediumService;
    private GenreService genreService;
    private MovieGenreService movieGenreService;

    private Movie currentMovie;
    private String saveEditText;

    @FXML
    private Label messageLabel;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField labelTextField;

    @FXML
    private TextArea castTextArea;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button mediumsButton;

    @FXML
    private Button genresButton;

    @FXML
    private Button saveEditButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ListView<Medium> mediumListView;

    @FXML
    private ListView<Genre> genresListView;

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
        initializeMode();
        mediumListView.setItems(mediumService.bind());
        genresListView.setItems(genreService.bind());

        mediumsButton.setOnAction(actionEvent -> {
            showValueForm(actionEvent, "Media", mediumService, Medium.class);
        });

        genresButton.setOnAction(actionEvent -> {
            showValueForm(actionEvent, "Genres", genreService, Genre.class);
        });

        try {
            mediumService.fetch();
        } catch (ServiceException ex) {
            LOGGER.error("Kon media data niet ophalen!");
        }

        try {
            genreService.fetch();
        } catch (ServiceException ex) {
            LOGGER.error("Kon media data niet ophalen!");
        }

        // set events
        nameTextField.textProperty().addListener( (obs, oldValue, newValue) -> {
            updateDirty(!oldValue.equals(newValue));
            currentMovie.setName(newValue);
        });

        labelTextField.textProperty().addListener( (obs, oldValue, newValue) -> {
            updateDirty(!oldValue.equals(newValue));
            currentMovie.setLabel(newValue);
        });

        castTextArea.textProperty().addListener( (obs, oldValue, newValue) -> {
            updateDirty(!oldValue.equals(newValue));
            currentMovie.setActors(newValue);
        });

        descriptionTextArea.textProperty().addListener( (obs, oldValue, newValue) -> {
            updateDirty(!oldValue.equals(newValue));
            currentMovie.setDescription(newValue);
        });
    }

    private void updateDirty(boolean dirty) {
        if(dirty) {
            saveEditButton.setText(saveEditText + "*");
        } else {
            saveEditButton.setText(saveEditText);
        }
    }

    private void initializeMode() {
        if(currentMovie != null) {
            saveEditText = "Bewerken";
            saveEditButton.setText(saveEditText);
            saveEditButton.setDisable(false);
            saveEditButton.setOnAction( actionEvent -> onUpdateMoviePressed());

            deleteButton.setVisible(true);
            deleteButton.setDisable(false);
            deleteButton.setOnAction( actionEvent -> onDeleteMoviePressed());

            nameTextField.setText(currentMovie.getName());
            labelTextField.setText(currentMovie.getLabel());
            castTextArea.setText(currentMovie.getActors());
            descriptionTextArea.setText(currentMovie.getDescription());
        } else {
            currentMovie = new Movie();
            saveEditText = "Opslaan";
            saveEditButton.setText(saveEditText);
            saveEditButton.setOnAction( actionEvent -> onCreateMoviePressed());

            deleteButton.setDisable(true);
            deleteButton.setVisible(false);
        }
    }

    private void onCreateMoviePressed() {
        if(movieService.isMovieValid(currentMovie)) {
            try {
                movieService.create(currentMovie);
                messageLabel.setText("");
                initializeMode();
                updateDirty(false);
            } catch (ServiceException e) {
                LOGGER.error(String.format("Film met label %s kon niet worden aangemaakt", currentMovie.getLabel()));
            }
        } else {
            messageLabel.setText("Film mist naam of label");
        }
    }

    private void onUpdateMoviePressed() {
        if(movieService.isMovieValid(currentMovie)) {
            try {
                movieService.update(currentMovie);
                messageLabel.setText("");
                updateDirty(false);
            } catch (ServiceException e) {
                LOGGER.error(String.format("Film met label %s kon niet worden bijgewerkt", currentMovie.getLabel()));
            }
        } else {
            messageLabel.setText("Film mist naam of label");
        }
    }

    private void onDeleteMoviePressed() {
        try {
            movieService.delete(currentMovie);
            stage.close();
        } catch (ServiceException e) {
            LOGGER.error(String.format("Film met label %s kon niet worden verwijderd", currentMovie.getLabel()));
        }
    }

    public void setMovie(Movie movie) {
        currentMovie = movie;
    }
}
