package org.gruskas.odtmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;

import java.io.IOException;
import java.util.Objects;


public class SettingsController {

    @FXML
    private TextField inputTextField;

    @FXML
    private Pane dateInput;

    @FXML
    private Spinner<Integer> monthsSpinner;

    @FXML
    private Spinner<Integer> weeksSpinner;

    @FXML
    private Spinner<Integer> daysSpinner;

    @FXML
    private CheckBox archiveFiles;

    @FXML
    private Label saveConfirm;

    @FXML
    private CheckBox showAllFiles;

    @FXML
    public void saveButton() {
        ConfigFileManager.folderPath = inputTextField.getText();
        ConfigFileManager.archiveFiles = archiveFiles.isSelected();
        ConfigFileManager.showAllFiles = showAllFiles.isSelected();
        ConfigFileManager.months = monthsSpinner.getValue();
        ConfigFileManager.weeks = weeksSpinner.getValue();
        ConfigFileManager.days = daysSpinner.getValue();

        System.out.println(
                "TextField value: " + ConfigFileManager.folderPath + ", \n" +
                        "archiveFiles: " + ConfigFileManager.archiveFiles + ", \n" +
                        "months: " + ConfigFileManager.months + ", \n" +
                        "weeks: " + ConfigFileManager.weeks + ", \n" +
                        "days: " + ConfigFileManager.days
        );

        if (ConfigFileManager.saveConfig()) {
            System.out.println("Configuration saved successfully!");
            saveConfirm.setText("Config has been saved!");
            refreshMainView();
        }

    }

    public void backupNow() {
        if (ArchiveFiles.createBackup()) {
            System.out.println("Backup completed successfully!");
        }
    }

    public void initialize() {
        inputTextField.setText(ConfigFileManager.folderPath);
        archiveFiles.setSelected(ConfigFileManager.archiveFiles);
        showAllFiles.setSelected(ConfigFileManager.showAllFiles);
        dateInput.setVisible(archiveFiles.isSelected());
        archiveFiles.selectedProperty().addListener((_, _, selected) -> dateInput.setVisible(selected));

        monthsSpinner.setValueFactory(new IntegerSpinnerValueFactory(0, 12, ConfigFileManager.months));
        weeksSpinner.setValueFactory(new IntegerSpinnerValueFactory(0, 52, ConfigFileManager.weeks));
        daysSpinner.setValueFactory(new IntegerSpinnerValueFactory(0, 365, ConfigFileManager.days));

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