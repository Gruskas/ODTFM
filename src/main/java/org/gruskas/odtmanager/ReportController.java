package org.gruskas.odtmanager;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class ReportController {
    public void submitButton(ActionEvent event) {
        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        ViewLoader.loadModalView(
                Application.stage,
                "fxml/thankyou-view.fxml",
                "SettingsStyle.css",
                "/org/gruskas/odtmanager/logo.png",
                "OdtFileManager - Thank you"
        );
    }
}
