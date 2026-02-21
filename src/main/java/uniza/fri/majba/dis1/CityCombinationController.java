package uniza.fri.majba.dis1;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.*;
import java.util.stream.Collectors;

public class CityCombinationController {

    public static class CombinationRow {
        private final SimpleStringProperty cities;
        private final SimpleStringProperty count;

        public CombinationRow(String cities, int count) {
            this.cities = new SimpleStringProperty(cities);
            this.count = new SimpleStringProperty(String.valueOf(count));
        }

        public String getCities() {
            return cities.get();
        }

        public SimpleStringProperty citiesProperty() {
            return cities;
        }

        public String getCount() {
            return count.get();
        }

        public SimpleStringProperty countProperty() {
            return count;
        }
    }

    @FXML
    private TableView<CombinationRow> combinationsTable;
    @FXML
    private TableColumn<CombinationRow, String> colCities;
    @FXML
    private TableColumn<CombinationRow, String> colCount;
    @FXML
    private FlowPane availableCitiesPane;
    @FXML
    private FlowPane dropZonePane;
    @FXML
    private Label statusLabel;
    @FXML
    private Button addButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button closeButton;

    private ObservableList<CombinationRow> combinations;
    private Set<String> combinationSet; // For quick duplicate checking
    private List<String> currentCombination; // Cities in the selection zone

    // Available cities to choose from (Žilina is always first and last, so not in selection)
    private static final String[] AVAILABLE_CITIES = {
            "Višňové", "Bod K", "Strečno", "Rosina"
    };

    @FXML
    private void initialize() {
        // Initialize collections
        combinations = FXCollections.observableArrayList();
        combinationSet = new HashSet<>();
        currentCombination = new ArrayList<>();

        // Setup table columns with explicit settings
        colCities.setCellValueFactory(cellData -> cellData.getValue().citiesProperty());
        colCities.setMinWidth(200);

        colCount.setCellValueFactory(cellData -> cellData.getValue().countProperty());
        colCount.setMinWidth(80);
        colCount.setMaxWidth(120);

        // Set items and policy
        combinationsTable.setItems(combinations);
        combinationsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Add default combinations
        addDefaultCombinations();

        // Setup available cities
        setupAvailableCities();

        // Setup selection zone
        setupSelectionZone();

        // Setup button states
        updateButtonStates();

        // Add listener for table selection
        combinationsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> updateButtonStates()
        );
    }

    private void setupAvailableCities() {
        availableCitiesPane.getChildren().clear();

        for (String city : AVAILABLE_CITIES) {
            Label cityLabel = createClickableCity(city);
            availableCitiesPane.getChildren().add(cityLabel);
        }
    }

    private Label createClickableCity(String cityName) {
        Label label = new Label(cityName);
        label.getStyleClass().add("city-chip");
        label.getStyleClass().add("clickable");

        // Set up click handler
        label.setOnMouseClicked(event -> {
            if (currentCombination.contains(cityName)) {
                // City is already selected, remove it
                currentCombination.remove(cityName);
            } else {
                // City is not selected, add it
                currentCombination.add(cityName);
            }
            updateCityStates();
            updateSelectionZone();
            statusLabel.setText("");
        });

        return label;
    }

    private void updateCityStates() {
        // Update the visual state of all city pills in the available cities pane
        for (int i = 0; i < availableCitiesPane.getChildren().size(); i++) {
            Label cityLabel = (Label) availableCitiesPane.getChildren().get(i);
            String cityName = cityLabel.getText();

            if (currentCombination.contains(cityName)) {
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

        // Add remove icon
        FontIcon removeIcon = new FontIcon("fa-times-circle");
        removeIcon.getStyleClass().add("remove-icon");
        label.setGraphic(removeIcon);

        // Click to remove from selection
        label.setOnMouseClicked(event -> {
            currentCombination.remove(cityName);
            updateCityStates();
            updateSelectionZone();
            statusLabel.setText("");
        });

        return label;
    }

    private void setupSelectionZone() {
        // No special setup needed, just display selected cities
        updateSelectionZone();
    }

    private void updateSelectionZone() {
        dropZonePane.getChildren().clear();

        if (currentCombination.isEmpty()) {
            Label placeholder = new Label("Vyberte mestá na cestu");
            placeholder.getStyleClass().add("drop-placeholder");
            dropZonePane.getChildren().add(placeholder);
        } else {
            for (String city : currentCombination) {
                Label cityLabel = createSelectedCityLabel(city);
                dropZonePane.getChildren().add(cityLabel);
            }
        }

        updateButtonStates();
    }

    private void addDefaultCombinations() {
        // Starting city is always Žilina, add the other cities in various orders, and return to Žilina
        String[] defaultCombos = {
                "Žilina, Višňové, Bod K, Strečno, Rosina, Žilina",
                "Žilina, Rosina, Strečno, Višňové, Bod K, Žilina",
                "Žilina, Bod K, Višňové, Rosina, Strečno, Žilina",
                "Žilina, Strečno, Višňové, Bod K, Rosina, Žilina",
                "Žilina, Višňové, Rosina, Strečno, Bod K, Žilina"
        };

        for (String combo : defaultCombos) {
            String normalized = normalizeCombination(combo);

            if (!combinationSet.contains(normalized)) {
                int cityCount = combo.split(",").length;
                CombinationRow row = new CombinationRow(combo, cityCount);
                combinations.add(row);
                combinationSet.add(normalized);
            }
        }
    }

    @FXML
    private void onAddCombination() {
        if (currentCombination.isEmpty()) {
            showError("Vyberte mestá kliknutím pre vytvorenie kombinácie.");
            return;
        }

        // Create combination string with Žilina at start and end
        List<String> fullRoute = new ArrayList<>();
        fullRoute.add("Žilina");
        fullRoute.addAll(currentCombination);
        fullRoute.add("Žilina");

        String combinationStr = String.join(", ", fullRoute);

        // Normalize and check for duplicates
        String normalized = normalizeCombination(combinationStr);

        if (combinationSet.contains(normalized)) {
            showError("Táto kombinácia už existuje!");
            return;
        }

        // Add the combination (count includes Žilina at start and end)
        CombinationRow newRow = new CombinationRow(combinationStr, fullRoute.size());
        combinations.add(newRow);
        combinationSet.add(normalized);

        // Refresh the table to ensure the new row is displayed
        combinationsTable.refresh();

        showSuccess("Kombinácia bola úspešne pridaná!");

        // Clear the selection
        currentCombination.clear();
        updateCityStates();
        updateSelectionZone();
        updateButtonStates();
    }

    @FXML
    private void onClearDropZone() {
        currentCombination.clear();
        updateCityStates();
        updateSelectionZone();
        statusLabel.setText("");
    }

    @FXML
    private void onRemoveCombination() {
        CombinationRow selected = combinationsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Vyberte kombináciu na odstránenie.");
            return;
        }

        String normalized = normalizeCombination(selected.getCities());
        combinations.remove(selected);
        combinationSet.remove(normalized);

        showSuccess("Kombinácia bola odstránená.");
        updateButtonStates();
    }

    @FXML
    private void onClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Normalize combination string for comparison (trim, lowercase, preserve order)
     * Combinations are equal only if all cities are in the exact same positions
     */
    private String normalizeCombination(String combination) {
        if (combination == null || combination.trim().isEmpty()) {
            return "";
        }

        String[] cities = combination.split(",");
        List<String> cityList = new ArrayList<>();

        for (String city : cities) {
            String trimmed = city.trim();
            if (!trimmed.isEmpty()) {
                cityList.add(trimmed.toLowerCase());
            }
        }

        // Join with comma - preserves order
        return String.join(",", cityList);
    }

    private void updateButtonStates() {
        boolean hasSelection = combinationsTable.getSelectionModel().getSelectedItem() != null;
        boolean hasCities = !currentCombination.isEmpty();

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

    /**
     * Get all combinations as a list of strings
     */
    public ObservableList<String> getCombinationsAsStrings() {
        return FXCollections.observableArrayList(
            combinations.stream()
                .map(CombinationRow::getCities)
                .collect(Collectors.toList())
        );
    }

    /**
     * Get the number of combinations
     */
    public int getCombinationCount() {
        return combinations.size();
    }
}

