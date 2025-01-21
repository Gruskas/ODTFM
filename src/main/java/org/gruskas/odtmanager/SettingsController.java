package org.gruskas.odtmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;


public class SettingsController {

    @FXML
    private TextField inputTextField;

    @FXML
    private CheckBox checkBox;

    @FXML
    private Label saveConfirm;

    @FXML
    public void saveButton() {
        ConfigFileManager.folderPath = inputTextField.getText();
        boolean isChecked = checkBox.isSelected();

        System.out.println("TextField value: " + ConfigFileManager.folderPath);
        System.out.println("checkBox: " + isChecked);
        if (ConfigFileManager.saveConfig()) {
            System.out.println("Configuration saved successfully!");
            saveConfirm.setText("Config has been saved!");
            refreshMainView();
        }

    }

    public void initialize() {
        String content = ConfigFileManager.folderPath;
        inputTextField.setText(content);
    }

    private void refreshMainView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("fxml/main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            Application.stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}