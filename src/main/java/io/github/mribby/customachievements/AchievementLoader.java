package io.github.mribby.customachievements;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AchievementLoader {
    private static final List<AchievementHolder> ACHIEVEMENT_HOLDERS = new LinkedList<AchievementHolder>();

    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final Gson GSON = new Gson();

    public static void loadDirectory(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".json");
                }
            });
            for (File file : files) {
                loadFile(file);
            }
        }
    }

    public static void loadFile(File file) {
        if (file.exists()) {
            try {
                loadJson(JSON_PARSER.parse(new FileReader(file)));
            } catch (IOException e) {
                CustomAchievementsMod.logger.error(String.format("Could not read achievement file: %s", file.getName()), e);
            }
        }
    }

    private static void loadJson(JsonElement json) {
        if (json.isJsonObject()) {
            try {
                ACHIEVEMENT_HOLDERS.add(GSON.fromJson(json, AchievementHolder.class));
            } catch (JsonSyntaxException e) {
                CustomAchievementsMod.logger.error(String.format("Invalid achievement: %s", json.toString()), e);
            }
        } else if (json.isJsonArray()) {
            for (JsonElement element : json.getAsJsonArray()) {
                loadJson(element);
            }
        } else {
            CustomAchievementsMod.logger.error(String.format("Invalid achievement: %s", json.toString()));
        }
    }

    public static void registerAchievements() {
        for (AchievementHolder achievementHolder : ACHIEVEMENT_HOLDERS) {
            achievementHolder.registerAchievement();
        }
    }
}
