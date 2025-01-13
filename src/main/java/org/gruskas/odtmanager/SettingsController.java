package org.gruskas.odtmanager;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


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
        }

    }

    public void initialize() {
        String content = ConfigFileManager.folderPath;
        inputTextField.setText(content);
    }
}

