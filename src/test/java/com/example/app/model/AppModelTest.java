package com.example.app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AppModel
 */
class AppModelTest {

    private AppModel model;

    @BeforeEach
    void setUp() {
        model = new AppModel();
    }

    @Test
    void testDefaultMessage() {
        assertEquals("Hello, JavaFX MVC!", model.getMessage());
    }

    @Test
    void testSetMessage() {
        model.setMessage("New Message");
        assertEquals("New Message", model.getMessage());
    }

    @Test
    void testUpdateMessageWithValidInput() {
        model.updateMessage("Test Input");
        assertEquals("You entered: Test Input", model.getMessage());
    }

    @Test
    void testUpdateMessageWithEmptyInput() {
        model.updateMessage("");
        assertEquals("Please enter some text!", model.getMessage());
    }

    @Test
    void testUpdateMessageWithNullInput() {
        model.updateMessage(null);
        assertEquals("Please enter some text!", model.getMessage());
    }

    @Test
    void testUpdateMessageWithWhitespaceInput() {
        model.updateMessage("   ");
        assertEquals("Please enter some text!", model.getMessage());
    }
}
