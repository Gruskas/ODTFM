package org.gruskas.odtmanager;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

public class Tray {

    public Tray() {
        if (!SystemTray.isSupported()) {
            System.err.println("System tray is not supported");
            return;
        }

        SystemTray systemTray = SystemTray.getSystemTray();
        Image trayImage;
        try {
            trayImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/org/gruskas/odtmanager/trayLogo.png")));
        } catch (IOException e) {
            System.err.println("Could not load tray icon");
            e.printStackTrace();
            return;
        }

        TrayIcon trayIcon = new TrayIcon(trayImage, "ODTFM");
        trayIcon.setPopupMenu(createPopupMenu());

        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private PopupMenu createPopupMenu() {
        PopupMenu popupMenu = new PopupMenu();

        MenuItem exitItem = new MenuItem("Quit");
        exitItem.addActionListener(_ -> System.exit(0));
        popupMenu.add(exitItem);

        return popupMenu;
    }
}