package nl.markvanderwal.dvdcocoon.views;

import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.image.*;
import javafx.util.*;
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

    private static final Logger LOGGER = LogManager.getLogger(MovieFormController.class);

    private MovieService movieService;
    private MediumService mediumService;
    private GenreService genreService;

    private Movie currentMovie;
    private Modes mode = Modes.SAVE;
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
                               GenreService genreService) {
        this.movieService = movieService;
        this.mediumService = mediumService;
        this.genreService = genreService;
    }

    public void setMovie(Movie movie) {
        if (movie != null) {
            mode = Modes.EDIT;
            currentMovie = movie.clone();
        } else {
            mode = Modes.SAVE;
            currentMovie = new Movie();
        }
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
        mediumListView.setItems(mediumService.bind());
        genresListView.setItems(genreService.bind());

        mediumsButton.setOnAction(actionEvent -> {
            showValueForm(actionEvent, "Media", mediumService, Medium.class);
        });

        genresButton.setOnAction(actionEvent -> {
            showValueForm(actionEvent, "Genres", genreService, Genre.class);
        });

        ChangeListener<? super String> onDirtyChanger = (obs, oldValue, newValue) -> {
            updateDirty(oldValue, newValue);
        };

        // set events
        nameTextField.textProperty().addListener(onDirtyChanger);
        labelTextField.textProperty().addListener(onDirtyChanger);
        castTextArea.textProperty().addListener(onDirtyChanger);
        descriptionTextArea.textProperty().addListener(onDirtyChanger);

        // config medium listview
        mediumListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldValue, newValue) -> {
                    if (currentMovie != null) {
                        updateDirty(oldValue, newValue);
                        currentMovie.setMedium(newValue);
                    }
                });

        // config genre listview;
        genresListView.setCellFactory(CheckBoxListCell.forListView(new Callback<Genre, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Genre item) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener((obs, wasSelected, isNowSelected) -> {
                    updateDirty(wasSelected, isNowSelected);
                    onGenrePressed(item, isNowSelected);
                });

                // set default state
                if (currentMovie != null) {
                    for (Genre genre : currentMovie.getGenres()) {
                        if (item.equals(genre)) {
                            observable.setValue(true);
                            break;
                        }
                    }
                }

                return observable;
            }
        }));

        genreService.bind().addListener(new ListChangeListener<Genre>() {
            @Override
            public void onChanged(Change<? extends Genre> c) {
                while (c.next() && currentMovie != null) {
                    if (!c.wasReplaced() && c.wasRemoved()) {
                        Genre genre = c.getRemoved().get(0);
                        if(currentMovie.getGenres().contains(genre)) {
                            currentMovie.getGenres().remove(genre);
                        }
                    }
                }
            }
        });

        setInitialState();
    }

    @Override
    protected void deInitialize() {
        mode = Modes.SAVE;
        currentMovie = null;
    }

    private void setMovieData() {
        if (currentMovie != null) {
            currentMovie.setName(nameTextField.getText());
            currentMovie.setLabel(labelTextField.getText());
            currentMovie.setActors(castTextArea.getText());
            currentMovie.setDescription(descriptionTextArea.getText());
        }
    }

    private void setInitialState() {
        if (mode == Modes.EDIT) {
            saveEditButton.setText("Bewerken");
            saveEditButton.setDisable(false);
            saveEditButton.setOnAction(actionEvent -> onUpdateMoviePressed());

            deleteButton.setVisible(true);
            deleteButton.setDisable(false);
            deleteButton.setOnAction(actionEvent -> onDeleteMoviePressed());

            nameTextField.setText(currentMovie.getName());
            labelTextField.setText(currentMovie.getLabel());
            castTextArea.setText(currentMovie.getActors());
            descriptionTextArea.setText(currentMovie.getDescription());

            int index = mediumListView.getItems().indexOf(currentMovie.getMedium());
            if (index >= 0) {
                mediumListView.getSelectionModel().select(index);
            }

        } else if (mode == Modes.SAVE) {
            saveEditButton.setText("Opslaan");
            saveEditButton.setOnAction(actionEvent -> onCreateMoviePressed());

            deleteButton.setDisable(true);
            deleteButton.setVisible(false);
        }
        updateDirty(false);
    }

    private void onGenrePressed(Genre genre, boolean checked) {
        List<Genre> genres = currentMovie.getGenres();
        if (checked) {
            if (!genres.contains(genre)) {
                genres.add(genre);
            }
        } else {
            genres.remove(genre);
        }
    }

    private void onCreateMoviePressed() {
        setMovieData();
        if (movieService.isMovieValid(currentMovie)) {
            try {
                movieService.create(currentMovie);
                messageLabel.setText("Film opgeslagen!");
                setMovie(currentMovie);
                setInitialState();
                updateDirty(false);
            } catch (ServiceException e) {
                LOGGER.error("Film met label {} kon niet worden aangemaakt", currentMovie.getLabel());
            }
        } else {
            messageLabel.setText("Film mist naam of label");
        }
    }

    private void onUpdateMoviePressed() {
        setMovieData();
        if (movieService.isMovieValid(currentMovie)) {
            try {
                movieService.update(currentMovie);
                messageLabel.setText("");
                setMovie(currentMovie);
                updateDirty(false);
            } catch (ServiceException e) {
                LOGGER.error("Film met label {} kon niet worden bijgewerkt", currentMovie.getLabel());
            }
        } else {
            messageLabel.setText("Film mist naam of label");
        }
    }

    private void onDeleteMoviePressed() {
        showDialog(() -> {
            try {
                stage.close();
                movieService.delete(currentMovie);
            } catch (ServiceException e) {
                LOGGER.error("Film met label {} kon niet worden verwijderd", currentMovie.getLabel());
            }
        });
    }

    private <T> void updateDirty(T left, T right) {
        boolean dirty = !isEquals(left, right);
        updateDirty(dirty);
    }

    private void updateDirty(boolean isDirty) {
        if (isDirty) {
            saveEditButton.setDisable(false);
        } else {
            saveEditButton.setDisable(true);
        }
    }

    private <T> boolean isEquals(T left, T right) {
        if (left == null) {
            return null == right;
        } else {
            return left.equals(right);
        }
    }

    private void showDialog(Runnable action) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmatie");
        alert.setHeaderText("Film verwijderen");
        alert.setContentText("Weet u het zeker??");
        alert.initOwner(stage.getScene().getWindow());

        ImageView image = new ImageView(getClass().getResource("/icon.png").toString());
        image.setFitHeight(32);
        image.setFitWidth(32);
        alert.setGraphic(image);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            action.run();
        }
    }

    private enum Modes {
        SAVE,
        EDIT
    }
}
