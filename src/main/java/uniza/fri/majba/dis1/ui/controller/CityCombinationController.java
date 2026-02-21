package uniza.fri.majba.dis1.ui.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import uniza.fri.majba.dis1.ui.model.CityCombinationModel;
import uniza.fri.majba.dis1.ui.model.CityCombinationModel.CombinationRow;

public class CityCombinationController {

    @FXML private TableView<CombinationRow> combinationsTable;
    @FXML private TableColumn<CombinationRow, String> colCities;
    @FXML private TableColumn<CombinationRow, String> colCount;
    @FXML private FlowPane availableCitiesPane;
    @FXML private FlowPane dropZonePane;
    @FXML private Label statusLabel;
    @FXML private Button addButton;
    @FXML private Button clearButton;
    @FXML private Button removeButton;
    @FXML private Button closeButton;

    private CityCombinationModel model;

    @FXML
    private void initialize() {
        model = new CityCombinationModel();

        colCities.setCellValueFactory(cellData -> cellData.getValue().citiesProperty());
        colCities.setMinWidth(200);

        colCount.setCellValueFactory(cellData -> cellData.getValue().countProperty());
        colCount.setMinWidth(80);
        colCount.setMaxWidth(120);

        combinationsTable.setItems(model.getCombinations());
        combinationsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        setupAvailableCities();
        updateSelectionZone();
        updateButtonStates();

        combinationsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> updateButtonStates()
        );
    }

    private void setupAvailableCities() {
        availableCitiesPane.getChildren().clear();
        for (String city : CityCombinationModel.AVAILABLE_CITIES) {
            availableCitiesPane.getChildren().add(createClickableCity(city));
        }
    }

    private Label createClickableCity(String cityName) {
        Label label = new Label(cityName);
        label.getStyleClass().add("city-chip");
        label.getStyleClass().add("clickable");
        label.setOnMouseClicked(event -> {
            model.toggleCity(cityName);
            updateCityStates();
            updateSelectionZone();
            statusLabel.setText("");
        });
        return label;
    }

    private void updateCityStates() {
        for (javafx.scene.Node node : availableCitiesPane.getChildren()) {
            Label cityLabel = (Label) node;
            String cityName = cityLabel.getText();
            if (model.getCurrentCombination().contains(cityName)) {
                if (!cityLabel.getStyleClass().contains("selected")) {
                    cityLabel.getStyleClass().add("selected");
                }
            } else {
                cityLabel.getStyleClass().remove("selected");
            }
        }
    }

    private Label createSelectedCityLabel(String cityName) {
        Label label = new Label(cityName);
        label.getStyleClass().add("city-chip");
        label.getStyleClass().add("selected-preview");

        FontIcon removeIcon = new FontIcon("fa-times-circle");
        removeIcon.getStyleClass().add("remove-icon");
        label.setGraphic(removeIcon);

        label.setOnMouseClicked(event -> {
            model.toggleCity(cityName);
            updateCityStates();
            updateSelectionZone();
            statusLabel.setText("");
        });
        return label;
    }

    private void updateSelectionZone() {
        dropZonePane.getChildren().clear();

        if (model.getCurrentCombination().isEmpty()) {
            Label placeholder = new Label("Vyberte mestá na cestu");
            placeholder.getStyleClass().add("drop-placeholder");
            dropZonePane.getChildren().add(placeholder);
        } else {
            for (String city : model.getCurrentCombination()) {
                dropZonePane.getChildren().add(createSelectedCityLabel(city));
            }
        }

        updateButtonStates();
    }

    @FXML
    private void onAddCombination() {
        String error = model.addCurrentCombination();
        if (error != null) {
            showError(error);
            return;
        }
        combinationsTable.refresh();
        showSuccess("Kombinácia bola úspešne pridaná!");
        updateCityStates();
        updateSelectionZone();
        updateButtonStates();
    }

    @FXML
    private void onClearDropZone() {
        model.clearCurrentCombination();
        updateCityStates();
        updateSelectionZone();
        statusLabel.setText("");
    }

    @FXML
    private void onRemoveCombination() {
        CombinationRow selected = combinationsTable.getSelectionModel().getSelectedItem();
        String error = model.removeCombination(selected);
        if (error != null) {
            showError(error);
            return;
        }
        showSuccess("Kombinácia bola odstránená.");
        updateButtonStates();
    }

    @FXML
    private void onClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void updateButtonStates() {
        boolean hasSelection = combinationsTable.getSelectionModel().getSelectedItem() != null;
        boolean hasCities = !model.getCurrentCombination().isEmpty();

        removeButton.setDisable(!hasSelection);
        addButton.setDisable(!hasCities);
        clearButton.setDisable(!hasCities);
    }

    private void showError(String message) {
        statusLabel.setText("❌ " + message);
        statusLabel.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: 600;");
    }

    private void showSuccess(String message) {
        statusLabel.setText("✓ " + message);
        statusLabel.setStyle("-fx-text-fill: #10b981; -fx-font-weight: 600;");
    }

    public ObservableList<String> getCombinationsAsStrings() {
        return model.getCombinationsAsStrings();
    }

    public int getCombinationCount() {
        return model.getCombinationCount();
    }
}
