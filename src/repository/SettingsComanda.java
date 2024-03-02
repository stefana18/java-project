package repository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class SettingsComanda {


    private static SettingsComanda instance;

    private final String repoType;

    private final String repoFile;

    private SettingsComanda(String repoType, String repoFile) {
        this.repoType = repoType;
        this.repoFile = repoFile;
    }

    public String getRepoFile() {
        return repoFile;
    }

    public String getRepoType() {
        return repoType;
    }

    private static Properties loadSettings() {
        try (FileReader fr = new FileReader("C:\\Users\\Stefana\\IdeaProjects\\lab4MAP\\src\\repository\\settingsComanda.properties")) {
            Properties settings = new Properties();
            settings.load(fr);
            return settings;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized SettingsComanda getInstance() {
        Properties properties = loadSettings();
        instance = new SettingsComanda(properties.getProperty("repoType"), properties.getProperty("repoFile"));
        return instance;
    }


}