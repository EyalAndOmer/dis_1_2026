package uniza.fri.majba.dis1.controller;

import uniza.fri.majba.dis1.model.AppModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller class for the application.
 * Handles user interactions and updates the model.
 */
public class AppController {
    @FXML
    private Label messageLabel;

    @FXML
    private TextField inputField;

    @FXML
    private Button submitButton;

    private AppModel model;

    public AppController() {
        this.model = new AppModel();
    }

    @FXML
    public void initialize() {
        // Bind the label to the model's message property
        messageLabel.textProperty().bind(model.messageProperty());
    }

    @FXML
    private void handleSubmit() {
        String input = inputField.getText();
        model.updateMessage(input);
        inputField.clear();
    }

    public void setModel(AppModel model) {
        this.model = model;
        if (messageLabel != null) {
            messageLabel.textProperty().bind(model.messageProperty());
        }
    }

    public AppModel getModel() {
        return model;
    }
}
