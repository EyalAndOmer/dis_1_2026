package uniza.fri.majba.dis1.ui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.stream.Collectors;

public class CityCombinationModel {

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

    public static final String[] AVAILABLE_CITIES = {
            "Višňové", "Bod K", "Strečno", "Rosina"
    };

    private final ObservableList<CombinationRow> combinations;
    private final Set<String> combinationSet;
    private final List<String> currentCombination;

    public CityCombinationModel() {
        combinations = FXCollections.observableArrayList();
        combinationSet = new HashSet<>();
        currentCombination = new ArrayList<>();
        addDefaultCombinations();
    }

    private void addDefaultCombinations() {
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
                combinations.add(new CombinationRow(combo, cityCount));
                combinationSet.add(normalized);
            }
        }
    }

    /**
     * Toggle a city in the current selection.
     * Returns true if city was added, false if it was removed.
     */
    public boolean toggleCity(String cityName) {
        if (currentCombination.contains(cityName)) {
            currentCombination.remove(cityName);
            return false;
        } else {
            currentCombination.add(cityName);
            return true;
        }
    }

    public void clearCurrentCombination() {
        currentCombination.clear();
    }

    public List<String> getCurrentCombination() {
        return Collections.unmodifiableList(currentCombination);
    }

    /**
     * Try to add the current combination to the list.
     *
     * @return null on success, or an error message string on failure.
     */
    public String addCurrentCombination() {
        if (currentCombination.isEmpty()) {
            return "Vyberte mestá kliknutím pre vytvorenie kombinácie.";
        }

        List<String> fullRoute = new ArrayList<>();
        fullRoute.add("Žilina");
        fullRoute.addAll(currentCombination);
        fullRoute.add("Žilina");

        String combinationStr = String.join(", ", fullRoute);
        String normalized = normalizeCombination(combinationStr);

        if (combinationSet.contains(normalized)) {
            return "Táto kombinácia už existuje!";
        }

        combinations.add(new CombinationRow(combinationStr, fullRoute.size()));
        combinationSet.add(normalized);
        currentCombination.clear();
        return null;
    }

    /**
     * Try to remove a combination row.
     *
     * @return null on success, or an error message string if nothing was selected.
     */
    public String removeCombination(CombinationRow row) {
        if (row == null) {
            return "Vyberte kombináciu na odstránenie.";
        }
        String normalized = normalizeCombination(row.getCities());
        combinations.remove(row);
        combinationSet.remove(normalized);
        return null;
    }

    public ObservableList<CombinationRow> getCombinations() {
        return combinations;
    }

    public ObservableList<String> getCombinationsAsStrings() {
        return FXCollections.observableArrayList(
                combinations.stream()
                        .map(CombinationRow::getCities)
                        .collect(Collectors.toList())
        );
    }

    public int getCombinationCount() {
        return combinations.size();
    }

    /**
     * Normalize combination string for comparison (trim, lowercase, preserves order).
     */
    public String normalizeCombination(String combination) {
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
        return String.join(",", cityList);
    }
}

