package org.gruskas.odtmanager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ConfigFileManager {
    static File file = new File(System.getProperty("user.home") + File.separator + ".ODTFM.config");
    static String folderPath;

    public static void createFile() {
        if (file.exists()) {
            System.out.println("File already exists!");
        } else {
            try (BufferedWriter _ = new BufferedWriter(new FileWriter(file))) {
                System.out.println("The config file was successfully created!.");
                hideFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void hideFile() {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            try {
                Runtime.getRuntime().exec("attrib +h \"" + file + "\"");
                System.out.println("File hidden!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("This feature only works on Windows.");
        }
    }

    public static void readConfig() {
        Map<String, String> configMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    configMap.put(parts[0], parts[1]);
                } else {
                    System.err.println("Invalid line in configuration: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        folderPath = configMap.get("Path");
    }
}
