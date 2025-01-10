package org.gruskas.odtmanager;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;


public class SettingsController {

    @FXML
    private TextField inputTextField;

    @FXML
    private CheckBox checkBox;

    @FXML
    public void saveButton() {
        String textFieldValue = inputTextField.getText();
        boolean isChecked = checkBox.isSelected();

        System.out.println("TextField value: " + textFieldValue);
        System.out.println("CheckBox selected: " + isChecked);
    }
}

