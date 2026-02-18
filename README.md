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

### Option 1: Using Maven (Recommended for Development)

Run the application using Maven:

```bash
mvn javafx:run
```

### Option 2: Running the Packaged JAR

After packaging (see below), run the JAR directly:

```bash
java -jar target/javafx-mvc-app-1.0-SNAPSHOT.jar
```

This works because the JAR includes all necessary JavaFX dependencies (fat JAR).

## Packaging the Application

To create an executable JAR file with all dependencies:

```bash
mvn clean package
```

This will create a fat JAR file in the `target/` directory:
- `javafx-mvc-app-1.0-SNAPSHOT.jar` - Shaded JAR with all dependencies (8+ MB)
- `original-javafx-mvc-app-1.0-SNAPSHOT.jar` - Original JAR without dependencies (6 KB)

The shaded JAR can be run on any system with Java 17+ installed using:

```bash
java -jar target/javafx-mvc-app-1.0-SNAPSHOT.jar
```

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