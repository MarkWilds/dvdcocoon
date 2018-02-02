package nl.markvanderwal.dvdcocoon.views;

import javafx.fxml.*;
import javafx.scene.control.*;
import nl.markvanderwal.dvdcocoon.models.*;
import nl.markvanderwal.dvdcocoon.services.*;
import org.apache.logging.log4j.*;

import java.net.*;
import java.util.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 2-2-2018
 */
public class ValueFormController extends AbstractFXMLViewController {

    private static final Logger LOGGER = LogManager.getLogger(ValueFormController.class);

    private BaseService<? extends IdValueType, Integer> service;

    @FXML
    private Label valueLabel;

    @FXML
    private TextField nameTextField;

    @FXML
    private ListView<IdValueType> dataListView;

    @FXML
    private Button saveButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    public ValueFormController(BaseService<? extends IdValueType, Integer> service) {
        this.service = service;
    }

    @Override
    protected URL getFxmlResourceUrl() {
        return getClass().getResource("ValueForm.fxml");
    }

    @Override
    protected ResourceBundle getFxmlResourceBundle() {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveButton.setOnAction(actionEvent -> {
            LOGGER.info("Test button pressed");
        });

        editButton.setOnAction(actionEvent -> {

        });

        deleteButton.setOnAction(actionEvent -> {
        });
    }

    public void setValueName(String name) {
        valueLabel.setText(name);
    }
}
