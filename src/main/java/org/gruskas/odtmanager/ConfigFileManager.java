package org.gruskas.odtmanager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ConfigFileManager {
    static File file = new File(System.getProperty("user.home") + File.separator + ".ODTFM.config");
    static String folderPath;
    static boolean archiveFiles;
    static int months;
    static int weeks;
    static int days;

    public static void createFile() {
        if (file.exists()) {
            System.out.println("Config file already exists!");
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
                Process process = Runtime.getRuntime().exec("attrib +h \"" + file + "\"");
                process.waitFor();
                System.out.println("File hidden!");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("This feature only works on Windows.");
        }
    }

    public static void unhideFile() {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            try {
                Process process = Runtime.getRuntime().exec("attrib -h \"" + file + "\"");
                process.waitFor();
            } catch (IOException | InterruptedException e) {
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
                    if (line.isEmpty() || line.startsWith("#"))
                        continue;
                    System.err.println("Invalid line in configuration: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        folderPath = configMap.get("Path");
        if (folderPath == null || folderPath.isEmpty()) {
            folderPath = System.getProperty("user.home");
        }

        archiveFiles = Boolean.parseBoolean(configMap.get("Archive files"));
        months = parseIntegerOrDefault(configMap.get("Months"));
        weeks = parseIntegerOrDefault(configMap.get("Weeks"));
        days = parseIntegerOrDefault(configMap.get("Days"));
        System.out.println("months: " + months + ", weeks: " + weeks + ", days: " + days);
    }

    private static int parseIntegerOrDefault(String value) {
        if (value != null && !value.isEmpty()) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format: " + value);
            }
        }
        return 0;
    }

    public static boolean saveConfig() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("Path", folderPath);
        configMap.put("Archive files", String.valueOf(archiveFiles));
        configMap.put("Months", String.valueOf(months));
        configMap.put("Weeks", String.valueOf(weeks));
        configMap.put("Days", String.valueOf(days));

        try {
            unhideFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("Path=" + configMap.get("Path"));
                writer.newLine();
                writer.newLine();

                writer.write("#Archive Settings");
                writer.newLine();

                writer.write("Archive files=" + configMap.get("Archive files"));
                writer.newLine();
                writer.write("Months=" + configMap.get("Months"));
                writer.newLine();
                writer.write("Weeks=" + configMap.get("Weeks"));
                writer.newLine();
                writer.write("Days=" + configMap.get("Days"));
                writer.newLine();

                hideFile();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
