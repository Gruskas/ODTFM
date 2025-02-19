package org.gruskas.odtmanager;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class Tray {

    private final Stage stage;
    private TrayIcon trayIcon;

    public Tray(Stage stage) {
        this.stage = stage;

        if (!SystemTray.isSupported()) {
            System.err.println("System tray is not supported");
            return;
        }

        try {
            SystemTray systemTray = SystemTray.getSystemTray();
            Image trayImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/org/gruskas/odtmanager/logo.png")));
            trayIcon = new TrayIcon(trayImage, "ODTFM", createPopupMenu());
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(_ -> {
                System.out.println("Tray icon clicked");
                showStage();
            });
            systemTray.add(trayIcon);

            if (ConfigFileManager.exitOnClose) {
                stage.setOnCloseRequest(_ -> exitApplication());
            } else {
                Platform.setImplicitExit(false);
                stage.setOnCloseRequest(_ -> hideToSystemTray());
            }
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
    }

    private PopupMenu createPopupMenu() {
        PopupMenu popupMenu = new PopupMenu();

        MenuItem openItem = new MenuItem("Open");
        openItem.addActionListener(_ -> showStage());
        popupMenu.add(openItem);

        MenuItem exitItem = new MenuItem("Quit");
        exitItem.addActionListener(_ -> exitApplication());
        popupMenu.add(exitItem);

        return popupMenu;
    }

    public void hideToSystemTray() {
        Platform.runLater(() -> {
            if (stage != null) {
                System.out.println("Hiding stage to system tray");
                stage.hide();
            } else {
                System.err.println("Stage is null, cannot hide to system tray");
            }
        });
    }

    private void showStage() {
        if (stage != null) {
            Platform.runLater(() -> {
                System.out.println("Showing stage from system tray");
                stage.show();
                stage.toFront();
            });
        } else {
            System.err.println("Stage is null, cannot show stage");
        }
    }

    private void exitApplication() {
        SystemTray.getSystemTray().remove(trayIcon);
        Platform.exit();
        System.exit(0);
    }
}