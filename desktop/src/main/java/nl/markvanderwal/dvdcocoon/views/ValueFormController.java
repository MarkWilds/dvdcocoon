package nl.markvanderwal.dvdcocoon.views;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.util.*;
import nl.markvanderwal.dvdcocoon.*;
import nl.markvanderwal.dvdcocoon.exceptions.*;
import nl.markvanderwal.dvdcocoon.services.*;
import org.apache.logging.log4j.*;

import java.net.*;
import java.util.*;
import java.util.stream.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 2-2-2018
 */
public class ValueFormController extends AbstractFXMLViewController {

    private static final Logger LOGGER = LogManager.getLogger(ValueFormController.class);

    private BaseService<IdValueType, Integer> service;
    private IdValueTypeFactory factory;

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

    public ValueFormController(BaseService service, IdValueTypeFactory factory) {
        this.service = service;
        this.factory = factory;
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
        dataListView.setItems(service.bind());
        dataListView.setCellFactory(new Callback<ListView<IdValueType>, ListCell<IdValueType>>() {
            @Override
            public ListCell<IdValueType> call(ListView<IdValueType> param) {
                return new ValueStringCell();
            }
        });

        try {
            service.fetch();
        } catch (ServiceException e) {
            LOGGER.error("Kon de waardes niet ophalen!");
        }

        saveButton.setOnAction(actionEvent -> {
            saveValue();
        });

        editButton.setOnAction(actionEvent -> {

        });

        deleteButton.setOnAction(actionEvent -> {
        });
    }

    private void saveValue() {
        String value = nameTextField.getText();
        if(!"".equals(value)) {
            IdValueType valueType = factory.createValue(0, value);
            try {
                service.create(valueType);
                nameTextField.setText("");
            } catch (ServiceException e) {
                LOGGER.error("Kon de waarde niet opslaan!");
            }
        }
    }

    public void setValueName(String name) {
        valueLabel.setText(name);
    }

    private class ValueStringCell extends ListCell<IdValueType> {
        @Override public void updateItem(IdValueType item, boolean empty) {
            super.updateItem(item, empty);
            if(item != null) {
                setText(item.getName());
            }
        }
    }
}
