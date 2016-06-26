package io.github.mribby.customachievements;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.mribby.customachievements.trigger.Trigger;
import io.github.mribby.customachievements.trigger.TriggerHandler;
import io.github.mribby.customachievements.trigger.TriggerRegistry;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.AchievementPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AchievementHolder {
    private String[] mods;
    private String page;
    private String id;
    private String name;
    private String description;
    private int[] position;
    private String icon;
    private String parent;
    private boolean special;
    private Map<Trigger, Object> triggers;

    private transient Achievement achievement;
    private transient Map<Trigger, Object> triggerDataMap;

    public void registerAchievement() {
        try {
            if (mods != null) {
                for (String modId : mods) {
                    if (!Loader.isModLoaded(modId)) {
                        return;
                    }
                }
            }
            achievement = new CustomAchievement().registerStat();
            if (page != null) {
                AchievementPage achievementPage = AchievementPage.getAchievementPage(page);
                if (achievementPage != null) {
                    achievementPage.getAchievements().add(achievement);
                } else {
                    achievementPage = new AchievementPage(page, achievement);
                    AchievementPage.registerAchievementPage(achievementPage);
                }
            }
            if (triggers != null) {
                triggerDataMap = new HashMap<Trigger, Object>();
                for (Map.Entry<Trigger, Object> entry : triggers.entrySet()) {
                    Trigger trigger = entry.getKey();
                    Object data = readTriggerData(trigger, entry.getValue());
                    triggerDataMap.put(trigger, data);
                    TriggerRegistry.registerAchievementTrigger(trigger, this);
                }
            }
        } catch (Exception e) {
            CustomAchievementsMod.logger.error(String.format("Could not register achievement (ID: %s)", id), e);
        }
    }

    private Object readTriggerData(Trigger trigger, Object obj) {
        if (obj instanceof List) {
            List dataList = (List) obj;
            for (int i = 0; i < dataList.size(); i++) {
                dataList.set(i, readTriggerData(trigger, dataList.get(i)));
            }
            return dataList;
        } else {
            Object data = Utils.getDataByObject(obj);
            return data != null ? data : trigger.readData(obj);
        }
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public Object getTriggerData(Trigger trigger) {
        return triggerDataMap != null ? triggerDataMap.get(trigger) : null;
    }

    private class CustomAchievement extends Achievement {
        public CustomAchievement() {
            super("achievement." + id, id, position[0], position[1], Utils.getItemStackByText(icon), Utils.getAchievementById(parent));
            if (parentAchievement == null) {
                initIndependentStat();
            }
            if (special) {
                setSpecial();
            }
            if (name != null) {
                statName = new ChatComponentText(name);
            }
        }

        @Override
        @SideOnly(Side.CLIENT)
        public String getDescription() {
            return description != null ? description : super.getDescription();
        }
    }
}
