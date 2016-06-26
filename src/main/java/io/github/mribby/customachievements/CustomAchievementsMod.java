package io.github.mribby.customachievements;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import io.github.mribby.customachievements.trigger.TriggerHandler;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = "customachievements", name = "Custom Achievements", version = "@VERSION@")
public class CustomAchievementsMod {
    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        File configDir = event.getModConfigurationDirectory();
        File file = new File(configDir, "CustomAchievements.json");
        File dir = new File(configDir, "CustomAchievements");
        AchievementLoader.loadFile(file);
        AchievementLoader.loadDirectory(dir);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        TriggerHandler triggerHandler = new TriggerHandler();
        FMLCommonHandler.instance().bus().register(triggerHandler);
        MinecraftForge.EVENT_BUS.register(triggerHandler);

        AchievementLoader.registerAchievements();
    }
}
