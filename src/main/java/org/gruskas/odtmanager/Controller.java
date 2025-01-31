package org.gruskas.odtmanager;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Controller {
    String currentFolderPath = null;
    String folderPath = ConfigFileManager.folderPath;

    @FXML
    private ListView<String> folderListView;

    @FXML
    private TableView<File> tableView;

    @FXML
    private TableColumn<File, String> nameColumn;

    @FXML
    TableColumn<File, String> sizeColumn;

    @FXML
    private TableColumn<File, String> dateColumn;

    @FXML
    private void newFile() {
        String date = getLocalTime();
        File file = new File(currentFolderPath + File.separator + date + ".odt");
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
        ArrayList<String> folders = FilesAndFolders.getFolders(folderPath);
        folderListView.getItems().addAll(folders);
        folderListView.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> {
            if (newValue != null) {
                currentFolderPath = folderPath + File.separator + newValue;
                loadFilesFromFolder(currentFolderPath);
                System.out.println("Path " + currentFolderPath);
            }
        });

        setCellValue();

        tableView.setOnMouseClicked(this::openFileOnRowClick);
        tableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                File selectedFile = tableView.getSelectionModel().getSelectedItem();
                if (selectedFile != null && selectedFile.exists()) {
                    openFile(selectedFile);
                }
            }
        });
    }

    private void setCellValue() {
        nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        sizeColumn.setCellValueFactory(cellData -> {
            String readableSize = formatSize(cellData.getValue().length());
            return new javafx.beans.property.SimpleStringProperty(readableSize);
        });

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

    public static String getLocalTime() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return today.format(formatter);
    }

    public void startBrowser() {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    URI uri;
                    try {
                        uri = new URI("https://gruskas.lol");
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                    desktop.browse(uri);
                } else {
                    System.out.println("Error while starting browser.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startFiles() {
        try {
            File file = new File(System.getProperty("user.home"));

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(file);
                } else {
                    System.out.println("Error while starting files.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return (size / 1024) + " KB";
        } else if (size < 1024 * 1024 * 1024) {
            return (size / (1024 * 1024)) + " MB";
        } else {
            return (size / (1024 * 1024 * 1024)) + " GB";
        }
    }

    public void viewSettings() {
        ViewLoader.loadModalView(
                Application.stage,
                "fxml/settings-view.fxml",
                "modal-style.css",
                "/org/gruskas/odtmanager/logo.png",
                "OdtFileManager - Settings"
        );
    }

    public void viewReport() {
        ViewLoader.loadModalView(
                Application.stage,
                "fxml/report-view.fxml",
                "modal-style.css",
                "/org/gruskas/odtmanager/logo.png",
                "OdtFileManager - Report a bug"
        );
    }

    public void startSpotify() {
        try {
            ProcessBuilder processBuilder;
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("windows")) {
                // Windows
                processBuilder = new ProcessBuilder(System.getProperty("user.home") + "\\AppData\\Roaming\\Spotify\\Spotify.exe");
            } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                // Linux
                processBuilder = new ProcessBuilder("spotify");
            } else if (os.contains("mac")) {
                // MacOS
                processBuilder = new ProcessBuilder("/Applications/Spotify.app/Contents/MacOS/Spotify");
            } else {
                throw new UnsupportedOperationException("Unsupported operating system: " + System.getProperty("os.name"));
            }

            processBuilder.start();
            System.out.println("Spotify launched!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}