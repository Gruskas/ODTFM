package org.gruskas.odtmanager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InputDialogController {

    @FXML
    private Label label;

    @FXML
    private TextField inputField;

    @FXML
    private Button confirmButton;

    private String inputText;

    @FXML
    public void initialize() {
        confirmButton.setOnAction(_ -> {
            inputText = inputField.getText();
            ((Stage) confirmButton.getScene().getWindow()).close();
        });
    }

    public void setLabelText(String text) {
        label.setText(text);
    }

    public String getInputText() {
        return inputText;
    }
}