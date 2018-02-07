package nl.markvanderwal.dvdcocoon.views;

import com.google.common.base.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import nl.markvanderwal.dvdcocoon.exceptions.*;
import nl.markvanderwal.dvdcocoon.models.*;
import nl.markvanderwal.dvdcocoon.services.*;
import org.apache.logging.log4j.*;

import javax.inject.*;
import java.net.*;
import java.util.*;
import java.util.stream.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
public class MainFormController extends CocoonController {

    private static final Logger LOGGER = LogManager.getLogger(MainFormController.class);

    private final MovieService movieService;
    private final MediumService mediumService;
    private final GenreService genreService;

    private FilteredList<Movie> filteredMovies;

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

    /**************** FILTER *****/

    @FXML
    private CheckBox labelFilter;

    @FXML
    private CheckBox mediumFilter;

    @FXML
    private CheckBox genreFilter;

    @FXML
    private CheckBox actorFilter;

    @FXML
    private Button searchButton;

    @FXML
    private Button clearButton;

    @FXML
    private TextField searchTextInput;

    @Inject
    public MainFormController(MovieService movieService, MediumService mediumService,
                              GenreService genreService) {
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
        try {
            movieService.fetch();

            initializeButtonIcons();
            initializeTableView();
            initializeFilterView();
            initializeActions();

        } catch (ServiceException e) {
            LOGGER.error("Gefaald om de film data op te halen!");
        }
    }

    private void initializeButtonIcons() {
//        Medium m = new Medium();
//        m.setId(1);
//        m.setName("DVD");
//
//        try {
//            mediumService.create(m);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }
//
//        Genre genre1 = new Genre();
//        genre1.setId(1);
//        genre1.setName("Fantasie");
//
//        Genre genre2 = new Genre();
//        genre2.setId(2);
//        genre2.setName("Drama");
//
//        try {
//            genreService.create(genre1);
//            genreService.create(genre2);
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }
//
//        IntStream.range(0, 5000).forEach(i -> {
//            Movie movie = new Movie();
//            movie.setName("Test");
//            movie.setLabel("LBL");
//            movie.setName(movie.getName() + i);
//            movie.setLabel(movie.getLabel() + i);
//
//            try {
//                movie.setMedium(m);
//                movie.setGenres(Arrays.asList(genre1, genre2));
//                movieService.create(movie);
//            } catch (ServiceException e) {
//                e.printStackTrace();
//            }
//        });
    }

    private void initializeTableView() {
        movieTable.setEditable(false);
        movieTable.setPlaceholder(new Label("Geen films gevonden"));

        labelColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getLabel());
        });

        mediumColumn.setCellValueFactory(cellData -> {
            return new SimpleObjectProperty<>(cellData.getValue().getMedium().toString());
        });

        nameColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });

        genresColumn.setCellValueFactory(cellData -> {
            List<Genre> genres = cellData.getValue().getGenres();
            String genreText = "N.V.T";
            if (genres.size() > 0) {
                genreText = genres.stream().map(Genre::getName).collect(Collectors.joining(", "));
            }

            return new SimpleStringProperty(genreText);
        });

        filteredMovies = new FilteredList<>(movieService.bind(), this::movieFilter);
        SortedList<Movie> sortedList = new SortedList<>(filteredMovies);
        sortedList.comparatorProperty().bind(movieTable.comparatorProperty());
        movieTable.setItems(sortedList);
    }

    private void initializeFilterView() {
        searchButton.setOnAction(actionEvent -> {
            filteredMovies.setPredicate(this::movieFilter);
        });

        clearButton.setOnAction(actionEvent -> {
            searchTextInput.setText("");
            filteredMovies.setPredicate(movie -> true);
        });
    }

    private void initializeActions() {
        newMovieButton.setOnAction(this::showMovieForm);

        mediumsButton.setOnAction(actionEvent -> {
            showValueForm(actionEvent, "Media", mediumService, Medium.class);
        });

        genresButton.setOnAction(actionEvent -> {
            showValueForm(actionEvent, "Genres", genreService, Genre.class);
        });

        // should be handled inside movie service
        // this handles the removing of a medium and updates all movies to reflect this change
        mediumService.bind().addListener(new ListChangeListener<Medium>() {
            @Override
            public void onChanged(Change<? extends Medium> c) {
                while (c.next()) {
                    if (!c.wasReplaced() && c.wasRemoved()) {
                        Medium medium = c.getRemoved().get(0);

                        // get all movies that have this medium
                        movieService.bind().stream().forEach(movie -> {
                            if (medium.equals(movie.getMedium())) {
                                try {
                                    movie.setMedium(null);
                                    movieService.update(movie);
                                } catch (ServiceException e) {
                                    LOGGER.error(e.getMessage());
                                }
                            }
                        });
                    }
                }
                movieTable.refresh();
            }
        });

        // should be handled inside movie service
        // this handles the removing of a genre and updates all movies to reflect this change
        genreService.bind().addListener(new ListChangeListener<Genre>() {
            @Override
            public void onChanged(Change<? extends Genre> c) {
                while (c.next()) {
                    if (!c.wasReplaced() && c.wasRemoved()) {
                        Genre genre = c.getRemoved().get(0);

                        movieService.bind().stream().forEach(movie -> {
                            List<Genre> movieGenres = movie.getGenres();
                            if (movieGenres.contains(genre)) {
                                try {
                                    movieGenres.remove(genre);
                                    movieService.update(movie);
                                } catch (ServiceException e) {
                                    LOGGER.error(e.getMessage());
                                }
                            }
                        });
                    }
                }
                movieTable.refresh();
            }
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

    private boolean movieFilter(Movie movie) {
        boolean anySelected = labelFilter.isSelected() ||
                actorFilter.isSelected() ||
                mediumFilter.isSelected();

        String searchText = searchTextInput.getText();
        if (Strings.isNullOrEmpty(searchText))
            return true;

        boolean filter = false;

        if (!anySelected) {
            filter = movie.getName().contains(searchText);
        }

        if (labelFilter.isSelected()) {
            filter = filter || movie.getLabel().contains(searchText);
        }

        if (actorFilter.isSelected()
                && !Strings.isNullOrEmpty(movie.getActors())) {
            filter = filter || movie.getActors().contains(searchText);
        }

        Medium medium = movie.getMedium();
        if (mediumFilter.isSelected()
                && medium != null && !Strings.isNullOrEmpty(medium.getName())) {
            filter = filter || medium.getName().contains(searchText);
        }

        if (genreFilter.isSelected()) {
            filter = filter || movie.getGenres().stream()
                    .filter(genre -> genre.getName()
                            .contains(searchText))
                    .count() > 0;
        }

        return filter;
    }
}
