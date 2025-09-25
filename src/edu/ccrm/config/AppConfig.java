// AppConfig.java
package edu.ccrm.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AppConfig {
    private static AppConfig instance;
    private final Path dataDirectory;
    private final Path backupDirectory;
    private final int maxCreditsPerSemester;
    
    private AppConfig() {
        this.dataDirectory = Paths.get("data");
        this.backupDirectory = Paths.get("backups");
        this.maxCreditsPerSemester = 18;
    }
    
    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }
    
    public Path getDataDirectory() { return dataDirectory; }
    public Path getBackupDirectory() { return backupDirectory; }
    public int getMaxCreditsPerSemester() { return maxCreditsPerSemester; }
}