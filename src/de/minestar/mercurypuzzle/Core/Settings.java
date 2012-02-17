package de.minestar.mercurypuzzle.Core;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class Settings {
    private static int MAX_BLOCKS_REPLACE_AT_ONCE = 50000;
    private static int TICKS_BETWEEN_REPLACE = 10 * 20;

    public static void init(File dataFolder) {
        try {
            File file = new File(dataFolder, "config.yml");
            if (!file.exists()) {
                saveSettings(dataFolder);
                return;
            }

            YamlConfiguration config = new YamlConfiguration();
            config.load(file);

            MAX_BLOCKS_REPLACE_AT_ONCE = config.getInt("Threads.Structures.MaxReplaceAtOnce", MAX_BLOCKS_REPLACE_AT_ONCE);
            TICKS_BETWEEN_REPLACE = config.getInt("Threads.Structures.ticksBetweenReplace", TICKS_BETWEEN_REPLACE);

        } catch (Exception e) {
            e.printStackTrace();
            saveSettings(dataFolder);
        }
    }

    public static void saveSettings(File dataFolder) {
        try {
            File file = new File(dataFolder, "config.yml");
            boolean fileExists = file.exists();

            YamlConfiguration config = new YamlConfiguration();
            if (fileExists)
                config.load(file);

            config.set("Threads.Structures.MaxReplaceAtOnce", MAX_BLOCKS_REPLACE_AT_ONCE);
            config.set("Threads.Structures.ticksBetweenReplace", TICKS_BETWEEN_REPLACE);

            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getMaxBlockxReplaceAtOnce() {
        return MAX_BLOCKS_REPLACE_AT_ONCE;
    }

    public static int getTicksBetweenReplace() {
        return TICKS_BETWEEN_REPLACE;
    }
}
