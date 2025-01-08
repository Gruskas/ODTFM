package org.gruskas.odtmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class FilesAndFolders {
    public static ArrayList<String> getFolders(String folderPath) {
        File folder = new File(folderPath);

        ArrayList<String> folderNames = new ArrayList<>();
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        folderNames.add(file.getName());
                    }
                }
            }
        }
        System.out.println(folderNames);
        return folderNames;
    }

    public static ArrayList<File> getFiles(String folderPath) {
        File folder = new File(folderPath);
        ArrayList<File> fileList = new ArrayList<>();

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles(file -> file.isFile() && file.getName().endsWith(".odt"));
            if (files != null) {
                fileList.addAll(Arrays.asList(files));
            }
        }
        return fileList;
    }
}
