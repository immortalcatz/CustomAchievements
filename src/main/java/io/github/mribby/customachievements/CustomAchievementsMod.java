package io.github.mribby.customachievements;

import com.google.gson.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import io.github.mribby.customachievements.trigger.Trigger;
import io.github.mribby.customachievements.trigger.TriggerDeserializer;
import io.github.mribby.customachievements.trigger.TriggerHandler;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = "customachievements", name = "Custom Achievements", version = "@VERSION@")
public class CustomAchievementsMod {
    public static Logger logger;

    private final List<AchievementHolder> achievementHolders = new ArrayList<AchievementHolder>();

    private JsonParser jsonParser;
    private Gson gson;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        jsonParser = new JsonParser();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Trigger.class, new TriggerDeserializer());
        gson = gsonBuilder.create();

        File configDir = event.getModConfigurationDirectory();
        File dir = new File(configDir, "CustomAchievements");
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
        } else {
            dir.mkdir();
        }
        loadFile(new File(configDir, "CustomAchievements.json"));
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        TriggerHandler triggerHandler = new TriggerHandler();
        FMLCommonHandler.instance().bus().register(triggerHandler);
        MinecraftForge.EVENT_BUS.register(triggerHandler);

        registration:
        for (AchievementHolder achievementHolder : achievementHolders) {
            try {
                String[] modIds = achievementHolder.getModIds();
                if (modIds != null) {
                    for (String modId : modIds) {
                        if (!Loader.isModLoaded(modId)) {
                            continue registration;
                        }
                    }
                }
                achievementHolder.getAchievement().registerStat();
                achievementHolder.registerTriggers(triggerHandler);
            } catch (Exception e) {
                logger.error(String.format("Could not register achievement! ID: %s", achievementHolder.getId()), e);
            }
        }
    }

    private void loadFile(File file) {
        if (file.exists()) {
            try {
                loadJson(jsonParser.parse(new FileReader(file)));
            } catch (IOException e) {
                logger.error(String.format("Could not read achievement file: %s", file.getName()), e);
            }
        }
    }

    private void loadJson(JsonElement json) {
        if (json.isJsonObject()) {
            try {
                achievementHolders.add(gson.fromJson(json, AchievementHolder.class));
            } catch (JsonSyntaxException e) {
                logger.error(String.format("Invalid achievement: %s", json.toString()), e);
            }
        } else if (json.isJsonArray()) {
            for (JsonElement element : json.getAsJsonArray()) {
                loadJson(element);
            }
        } else {
            logger.error(String.format("Invalid achievement: %s", json.toString()));
        }
    }
}
