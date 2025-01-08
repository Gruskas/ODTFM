package org.gruskas.odtmanager;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    String currentFolderPath = null;

    @FXML
    private ListView<String> folderListView;

    @FXML
    private TableView<File> tableView;

    @FXML
    private TableColumn<File, String> nameColumn;

    @FXML
    private TableColumn<File, Long> sizeColumn;

    @FXML
    private TableColumn<File, String> dateColumn;

    @FXML
    private void newFile() {
        String date = getLocalTime();
        File file = new File(currentFolderPath + "\\" + date + ".odt");
        try {
            if (file.createNewFile()) {
                loadFilesFromFolder(currentFolderPath);
                System.out.println("Created file: " + file.getName());

            } else {
                System.out.println("File exist.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        Scanner scanner = new Scanner(System.in);
        String folderPath = scanner.nextLine();
        ArrayList<String> folders = FilesAndFolders.getFolders(folderPath);
        folderListView.getItems().addAll(folders);
        folderListView.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                currentFolderPath = folderPath + "\\" + newValue;
                loadFilesFromFolder(currentFolderPath);
                System.out.println("Path " + currentFolderPath);
            }
        });

        setCellValue();

        tableView.setOnMouseClicked(this::openFileOnRowClick);
    }

    private void setCellValue() {
        nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        sizeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleLongProperty(cellData.getValue().length()).asObject());
        dateColumn.setCellValueFactory(cellData -> {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return new javafx.beans.property.SimpleStringProperty(sdf.format(new java.util.Date(cellData.getValue().lastModified())));
        });
    }

    private void loadFilesFromFolder(String folderPath) {
        ArrayList<File> files = FilesAndFolders.getFiles(folderPath);
        tableView.getItems().clear();
        tableView.getItems().addAll(files);
    }

    private void openFileOnRowClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Object target = event.getTarget();

            while (target instanceof javafx.scene.Node) {
                if (target instanceof TableRow<?> row) {
                    File selectedFile = (File) row.getItem();
                    if (selectedFile != null && selectedFile.exists()) {
                        openFile(selectedFile);
                    }
                    return;
                }
                target = ((javafx.scene.Node) target).getParent();
            }
        }
    }

    private void openFile(File file) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getLocalTime() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return today.format(formatter);
    }
}