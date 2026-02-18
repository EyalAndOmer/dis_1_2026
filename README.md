# JavaFX MVC Application

A JavaFX application built with Maven using the Model-View-Controller (MVC) pattern.

## Project Structure

```
├── pom.xml                          # Maven configuration file
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/app/
│   │   │       ├── Main.java                # Application entry point
│   │   │       ├── model/
│   │   │       │   └── AppModel.java        # Model layer
│   │   │       ├── view/                    # View layer (FXML files)
│   │   │       └── controller/
│   │   │           └── AppController.java   # Controller layer
│   │   └── resources/
│   │       └── view.fxml                    # UI layout definition
│   └── test/
│       └── java/
│           └── com/example/app/             # Test classes
```

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Building the Project

To compile the project:

```bash
mvn clean compile
```

## Running the Application

### Option 1: Using Maven JavaFX Plugin (Recommended)

Run the application using the JavaFX Maven plugin:

```bash
mvn javafx:run
```

### Option 2: Using Maven Exec Plugin

```bash
mvn exec:java
```

### Option 3: Running from IntelliJ IDEA

If you're running directly from IntelliJ IDEA, you need to add VM options to your run configuration:

1. Open Run → Edit Configurations
2. Select your Main class configuration
3. Add the following to VM options:
   ```
   --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
   ```

   Or use Maven to handle the modules automatically:
   - Instead of running Main.java directly, use the Maven JavaFX plugin
   - Right-click on pom.xml → Maven → Run Maven Build
   - Set the command line as: `javafx:run`

**Easier Solution for IntelliJ:**
1. Right-click on `pom.xml`
2. Select "Add as Maven Project" (if not already done)
3. Open the Maven tool window (View → Tool Windows → Maven)
4. Navigate to: Plugins → javafx → javafx:run
5. Double-click to run

### Option 4: Running the Packaged JAR

**Note:** Running JavaFX as a fat JAR has limitations. The recommended approach is using the JavaFX Maven plugin.

For advanced packaging, consider using jlink or jpackage for creating native installers.

## Packaging the Application

To compile and package the application:

```bash
mvn clean package
```

This will create a JAR file in the `target/` directory. However, for JavaFX applications, it's recommended to use:
- The JavaFX Maven plugin for running during development
- jlink or jpackage for distribution

## Running Tests

To run the tests:

```bash
mvn test
```

## Maven Commands Summary

```bash
# Compile the project
mvn compile

# Run tests
mvn test

# Package into JAR
mvn package

# Clean and rebuild
mvn clean install

# Run the application
mvn javafx:run

# Skip tests during build
mvn package -DskipTests
```

## MVC Architecture

This application follows the Model-View-Controller pattern:

- **Model** (`AppModel.java`): Contains the application data and business logic
- **View** (`view.fxml`): Defines the user interface layout
- **Controller** (`AppController.java`): Handles user interactions and updates the model

## Features

- Clean MVC architecture
- JavaFX FXML for UI design
- Property binding between model and view
- Easy to extend and maintain
- Maven build system for dependency management
- Simple packaging into executable JAR
