package org.gruskas.odtmanager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ViewLoader {

    public static void loadMainView(Stage stage, String fxmlPath, String stylePath, String iconPath, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(Objects.requireNonNull(ViewLoader.class.getResource(stylePath)).toExternalForm());
            stage.setTitle(title);
//        stage.initStyle(StageStyle.UNDECORATED);
            stage.getIcons().add(new Image(Objects.requireNonNull(ViewLoader.class.getResource(iconPath)).toExternalForm()));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadModalView(Stage owner, String fxmlPath, String stylePath, String iconPath, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(Objects.requireNonNull(ViewLoader.class.getResource(stylePath)).toExternalForm());
            Stage modalStage = new Stage();
            modalStage.setTitle(title);
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(owner);
            modalStage.getIcons().add(new Image(Objects.requireNonNull(ViewLoader.class.getResource(iconPath)).toExternalForm()));
            modalStage.setScene(scene);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
