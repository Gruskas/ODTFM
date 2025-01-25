package org.gruskas.odtmanager;

import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    public static Stage stage;

    @Override
    public void start(Stage stage) {
        ConfigFileManager.createFile();
        ConfigFileManager.readConfig();

        Application.stage = stage;

        ViewLoader.loadMainView(
                stage,
                "fxml/main-view.fxml",
                "style.css",
                "/org/gruskas/odtmanager/logo.png",
                "OdtFileManager"
        );
    }

    public static void main(String[] args) {
        System.out.println("Wroking...");
        launch();
    }
}
//          java --module-path target/dist/lib --add-modules javafx.controls,javafx.fxml -jar target/OdtManager-1.0-shaded.jar
//          java -jar ./target/OdtManager-1.0-shaded.jar
//          mvn clean package JavaFX:run