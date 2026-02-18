package com.example.app.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for the application.
 * Holds the application data and business logic.
 */
public class AppModel {
    private final StringProperty message;

    public AppModel() {
        this.message = new SimpleStringProperty("Hello, JavaFX MVC!");
    }

    public String getMessage() {
        return message.get();
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public StringProperty messageProperty() {
        return message;
    }

    /**
     * Example business logic method
     */
    public void updateMessage(String input) {
        if (input != null && !input.trim().isEmpty()) {
            setMessage("You entered: " + input);
        } else {
            setMessage("Please enter some text!");
        }
    }
}
