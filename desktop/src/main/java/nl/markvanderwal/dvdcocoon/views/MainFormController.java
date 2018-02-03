package nl.markvanderwal.dvdcocoon.views;

import javafx.beans.property.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;
import nl.markvanderwal.dvdcocoon.*;
import nl.markvanderwal.dvdcocoon.exceptions.*;
import nl.markvanderwal.dvdcocoon.models.*;
import nl.markvanderwal.dvdcocoon.services.*;
import org.apache.logging.log4j.*;

import javax.inject.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
public class MainFormController extends AbstractFXMLViewController {

    private static final Logger LOGGER = LogManager.getLogger(MainFormController.class);

    private final MovieService movieService;
    private final MediumService mediumService;
    private final GenreService genreService;

    @FXML
    private TableView movieTable;

    @FXML
    private TableColumn<Movie, String> labelColumn;

    @FXML
    private TableColumn<Movie, String> mediumColumn;

    @FXML
    private TableColumn<Movie, String> nameColumn;

    @FXML
    private TableColumn<Movie, String> genresColumn;

    @FXML
    private Label movieToolbar;

    @FXML
    private Button newMovieButton;

    @FXML
    private Button mediumsButton;

    @FXML
    private Button genresButton;

    @Inject
    public MainFormController(MovieService movieService, MediumService mediumService, GenreService genreService) {
        this.movieService = movieService;
        this.mediumService = mediumService;
        this.genreService = genreService;
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
        initializeButtonIcons();
        initializeTableView();

        newMovieButton.setOnAction(actionEvent -> {
            LOGGER.info("Test button pressed");
        });

        mediumsButton.setOnAction(actionEvent -> {
            showValueForm(actionEvent,"Media", mediumService, (id, name) -> {
                Medium medium = new Medium();
                medium.setName(name);
                return medium;
            });
        });

        genresButton.setOnAction(actionEvent -> {
            showValueForm(actionEvent,"Genres", genreService, (id, name) -> {
                Genre genre = new Genre();
                genre.setName(name);
                return genre;
            });
        });

        movieToolbar.setText(String.format("Films geladen: %s", movieTable.getItems().size()));
    }

    private void showValueForm(ActionEvent event, String name,
                               BaseService service, IdValueTypeFactory factory) {
        ValueFormController controller = new ValueFormController(service, factory);
        Stage stage = controller.createStage();
        controller.setValueName(name);

        InputStream iconStream = getClass().getResourceAsStream("/icon.png");

        stage.setResizable(false);
        stage.getIcons().add(new Image(iconStream));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(name);
        stage.initOwner(((Node)event.getSource()).getScene().getWindow() );
        stage.showAndWait();
    }

    private void initializeButtonIcons() {

    }

    private void initializeTableView() {
        labelColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getLabel());
        });

        mediumColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getMedium().getName());
        });

        nameColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });

        genresColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty("Nog geen genres!");
        });

        movieTable.setItems(movieService.bind());
        try {
            movieService.fetch();
        } catch (ServiceException e) {
            LOGGER.error("Gefaald om de film data op te halen!");
        }
    }
}
