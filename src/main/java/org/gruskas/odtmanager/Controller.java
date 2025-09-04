package org.gruskas.odtmanager;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.awt.*;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

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
    private VBox customVBox;

    @FXML
    public void initialize() {
        setBackGround();

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

        contextMenu();
        folderContextMenu();
    }

    public void setBackGround() {
        if (ConfigFileManager.cutsomBackground && !ConfigFileManager.pathToImage.isEmpty()) {
            try {
                String imagePath = ConfigFileManager.pathToImage;
                File imageFile = new File(imagePath);
                if (imageFile.exists() && imageFile.isFile()) {
                    String imageUrl = imageFile.toURI().toString();
                    customVBox.setStyle(
                            "-fx-background-image: url('" + imageUrl + "'); " +
                                    "-fx-background-size: cover; " +
                                    "-fx-background-position: center;"
                    );
                } else {
                    System.err.println("Image file does not exist: " + imagePath);
                }
            } catch (Exception e) {
                System.err.println("Failed to set background image.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void newFolder() {
        String folderName = ViewLoader.inputDialogView(
                Application.stage,
                "fxml/input-dialog.fxml",
                "modal-style.css",
                "/org/gruskas/odtmanager/logo.png",
                "Create New Folder",
                "Folder name:"
        );
        if (folderName == null || folderName.trim().isEmpty()) {
            System.out.println("Folder name is empty.");
            return;
        }
        File folder = new File(folderPath + File.separator + folderName);
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                refresh(folder);
                System.out.println("Created folder: " + folder.getName());
            } else {
                System.out.println("Failed to create folder.");
            }
        } else {
            System.out.println("Folder exist.");
        }
    }

    private void refresh(File folder) {
        folderListView.getItems().clear();
        initialize();
        currentFolderPath = folder.getAbsolutePath();
        loadFilesFromFolder(currentFolderPath);
    }

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

    private void folderContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem deleteFolder = new MenuItem("Delete Folder");
        deleteFolder.setOnAction(_ -> {
            String selectedFolder = folderListView.getSelectionModel().getSelectedItem();
            if (selectedFolder != null) {
                File folder = new File(folderPath + File.separator + selectedFolder);
                if (folder.exists() && folder.isDirectory()) {
                    if (folder.delete()) {
                        folderListView.getItems().remove(selectedFolder);
                        System.out.println("Deleted folder: " + selectedFolder);
                    } else {
                        System.out.println("Failed to delete folder. Make sure it's empty.");
                    }
                }
            }
        });

        contextMenu.getItems().add(deleteFolder);

        folderListView.setOnContextMenuRequested(event -> {
            if (!folderListView.getSelectionModel().isEmpty()) {
                contextMenu.show(folderListView, event.getScreenX(), event.getScreenY());
            }
        });

        folderListView.sceneProperty().addListener((_, _, scene) -> {
            if (scene != null) {
                scene.addEventFilter(MouseEvent.MOUSE_PRESSED, _ -> {
                    if (contextMenu.isShowing()) {
                        contextMenu.hide();
                    }
                });
            }
        });
    }

    private void contextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem openItem = new MenuItem("Open");
        openItem.setOnAction(_ -> {
            File selectedFile = tableView.getSelectionModel().getSelectedItem();
            if (selectedFile.exists()) {
                openFile(selectedFile);
            }
        });

        MenuItem renameItem = new MenuItem("Rename");
        renameItem.setOnAction(_ -> {
            File selectedFile = tableView.getSelectionModel().getSelectedItem();
            if (selectedFile.exists()) {
                String newName = ViewLoader.inputDialogView(
                        Application.stage,
                        "fxml/input-dialog.fxml",
                        "modal-style.css",
                        "/org/gruskas/odtmanager/logo.png",
                        "Rename File",
                        "New name:"
                );

                if (newName != null && !newName.trim().isEmpty()) {
                    File newFile = new File(selectedFile.getParent(), newName);
                    if (selectedFile.renameTo(newFile)) {
                        loadFilesFromFolder(currentFolderPath);
                    } else {
                        System.err.println("Failed to rename file.");
                    }
                }
            }
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(_ -> {
            File selectedFile = tableView.getSelectionModel().getSelectedItem();
            if (selectedFile.exists() && selectedFile.delete()) {
                loadFilesFromFolder(currentFolderPath);
            } else {
                System.err.println("Failed to delete file.");
            }
        });

        contextMenu.getItems().addAll(openItem, renameItem, deleteItem);

        tableView.setOnContextMenuRequested(event -> {
            if (!tableView.getSelectionModel().isEmpty()) {
                contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
            }
        });

        tableView.sceneProperty().addListener((_, _, scene) -> {
            if (scene != null) {
                scene.addEventFilter(MouseEvent.MOUSE_PRESSED, _ -> {
                    if (contextMenu.isShowing()) {
                        contextMenu.hide();
                    }
                });
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
        ArrayList<File> files = FilesAndFolders.getFiles(folderPath, ConfigFileManager.showAllFiles);
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
                        uri = new URI("https://gruskas.fun");
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

}