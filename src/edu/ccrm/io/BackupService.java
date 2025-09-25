package edu.ccrm.io;

import edu.ccrm.config.AppConfig;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public class BackupService {
    private final ImportExportService importExportService;
    
    public BackupService(ImportExportService importExportService) {
        this.importExportService = importExportService;
    }
    
    public Path createBackup() throws IOException {
        Path backupDir = createBackupDirectory();
        
        Path studentsFile = backupDir.resolve("students.csv");
        Path coursesFile = backupDir.resolve("courses.csv");
        
        importExportService.exportStudentsToCSV(studentsFile);
        importExportService.exportCoursesToCSV(coursesFile);
        
        return backupDir;
    }
    
    private Path createBackupDirectory() throws IOException {
        // Fixed: Use AppConfig directly without unused variable
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path backupDir = AppConfig.getInstance().getBackupDirectory().resolve("backup_" + timestamp);
        Files.createDirectories(backupDir);
        return backupDir;
    }
    
    public long calculateBackupSize(Path directory) throws IOException {
        AtomicLong size = new AtomicLong(0);
        Files.walk(directory)
            .filter(Files::isRegularFile)
            .forEach(file -> {
                try { 
                    size.addAndGet(Files.size(file)); 
                } catch (IOException e) { 
                    System.err.println("Error getting size of file: " + file); 
                }
            });
        return size.get();
    }
    
    public void listBackupFiles(Path directory, int maxDepth) throws IOException {
        listFilesRecursive(directory, 0, maxDepth);
    }
    
    private void listFilesRecursive(Path directory, int currentDepth, int maxDepth) throws IOException {
        if (currentDepth > maxDepth) return;
        String indent = "  ".repeat(currentDepth);
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    System.out.println(indent + "[DIR] " + entry.getFileName());
                    listFilesRecursive(entry, currentDepth + 1, maxDepth);
                } else {
                    System.out.println(indent + "[FILE] " + entry.getFileName() + " (" + Files.size(entry) + " bytes)");
                }
            }
        }
    }
}