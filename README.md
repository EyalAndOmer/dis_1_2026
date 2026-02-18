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

Run the application using Maven:

```bash
mvn javafx:run
```

Or run it directly using Java (after compiling):

```bash
mvn clean package
java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -jar target/javafx-mvc-app-1.0-SNAPSHOT.jar
```

## Packaging the Application

To create a JAR file:

```bash
mvn clean package
```

This will create an executable JAR file in the `target/` directory:
- `javafx-mvc-app-1.0-SNAPSHOT.jar` - Regular JAR
- `javafx-mvc-app-1.0-SNAPSHOT-shaded.jar` - Fat JAR with all dependencies (if using shade plugin)

## Running Tests

To run the tests:

```bash
mvn test
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