package org.gruskas.odtmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ArchiveFiles {
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static boolean archiveFiles(File sourceDir, File outputZip) throws IOException {
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(outputZip))) {
            File[] files = sourceDir.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        File[] nestedFiles = file.listFiles();
                        if (nestedFiles != null) {
                            for (File nestedFile : nestedFiles) {
                                addFileToZip(sourceDir, zipOut, nestedFile);
                            }
                        }
                    } else {
                        addFileToZip(sourceDir, zipOut, file);
                    }
                }
            }
            return true;
        }
    }

    private static void addFileToZip(File sourceDir, ZipOutputStream zipOut, File nestedFile) throws IOException {
        String relativePath = sourceDir.toPath().relativize(nestedFile.toPath()).toString();

        try (FileInputStream fis = new FileInputStream(nestedFile)) {
            zipOut.putNextEntry(new ZipEntry(relativePath));
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zipOut.write(buffer, 0, length);
            }
            zipOut.closeEntry();
        }
    }

    public static void scheduleBackup(int months, int weeks, int days) {
        Period period = Period.ofMonths(months).plusDays(days + (int) (weeks * 7));

        LocalDate lastBackupDate = ConfigFileManager.lastBackupDate;
        LocalDate localtime = LocalDate.now();
        LocalDate nextBackupDate = lastBackupDate.plus(period);

        if (localtime.isAfter(nextBackupDate)) {
            System.out.println("It's time for a backup!");
            if (createBackup()) {
                ConfigFileManager.lastBackupDate = localtime;
                ConfigFileManager.saveConfig();
            }
        }
    }

    public static boolean createBackup() {
        File sourceFolder = new File(ConfigFileManager.folderPath);
        File backupFile = new File(Paths.get(System.getProperty("user.home"), "Documents", "ODTFM_" + Controller.getLocalTime().replace(":", "-") + ".zip").toString());

        try {
            return archiveFiles(sourceFolder, backupFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void startBackupScheduler() {
        scheduler.scheduleAtFixedRate(() -> {
            if (ConfigFileManager.archiveFiles) {
                ArchiveFiles.scheduleBackup(ConfigFileManager.months, ConfigFileManager.weeks, ConfigFileManager.days);
            }
        }, 0, 2, TimeUnit.HOURS);
    }
}
