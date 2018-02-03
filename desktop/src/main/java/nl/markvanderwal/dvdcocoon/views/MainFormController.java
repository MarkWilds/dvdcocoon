package nl.markvanderwal.dvdcocoon.views;

import javafx.beans.property.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;
import nl.markvanderwal.dvdcocoon.exceptions.*;
import nl.markvanderwal.dvdcocoon.models.*;
import nl.markvanderwal.dvdcocoon.services.*;
import org.apache.logging.log4j.*;

import javax.inject.*;
import java.net.*;
import java.util.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
public class MainFormController extends CocoonController {

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

        newMovieButton.setOnAction(this::showMovieForm);

        mediumsButton.setOnAction(actionEvent -> {
            showValueForm(actionEvent, "Media", mediumService, Medium.class);
        });

        genresButton.setOnAction(actionEvent -> {
            showValueForm(actionEvent, "Genres", genreService, Genre.class);
        });

        movieToolbar.setText(String.format("%s films geladen", movieTable.getItems().size()));

        movieTable.setRowFactory(tv -> {
            TableRow<Movie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Movie rowData = row.getItem();
                    showMovieForm(event, rowData);
                }
            });
            return row;
        });
    }

    private void initializeButtonIcons() {

    }

    private void initializeTableView() {
        labelColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getLabel());
        });

        mediumColumn.setCellValueFactory(cellData -> {
            Medium medium = cellData.getValue().getMedium();
            String mediumText = "N.V.T";
            if(medium != null ) {
                mediumText = medium.getName();
            }
            return new SimpleStringProperty(mediumText);
        });

        nameColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });

        genresColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty("N.V.T");
        });

        movieTable.setItems(movieService.bind());
        try {
            movieService.fetch();
        } catch (ServiceException e) {
            LOGGER.error("Gefaald om de film data op te halen!");
        }
    }

}
