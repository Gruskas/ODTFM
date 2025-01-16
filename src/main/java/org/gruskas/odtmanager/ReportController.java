package org.gruskas.odtmanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static org.gruskas.odtmanager.Application.stage;

public class ReportController {
    public void saveButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("fxml/thankyou-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("SettingsStyle.css")).toExternalForm());

            Stage settingsStage = new Stage();
            settingsStage.setTitle("OdtFileManager - Thank you");
            settingsStage.initModality(Modality.WINDOW_MODAL);
            settingsStage.initOwner(stage);
            settingsStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/org/gruskas/odtmanager/logo.png")).toExternalForm()));
            settingsStage.setScene(scene);

            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            settingsStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
