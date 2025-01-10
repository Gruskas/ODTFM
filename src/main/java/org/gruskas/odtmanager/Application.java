package org.gruskas.odtmanager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    public static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        Application.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("fxml/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        stage.setTitle("OdtFileManager");
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/org/gruskas/odtmanager/logo.png")).toExternalForm()));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        System.out.println("Wroking...");
        launch();
    }
}
//          java --module-path target/dist/lib --add-modules javafx.controls,javafx.fxml -jar target/OdtManager-1.0-shaded.jar
//          mvn clean package JavaFX:run