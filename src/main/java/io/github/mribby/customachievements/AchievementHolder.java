package io.github.mribby.customachievements;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.mribby.customachievements.dataholder.DataHolder;
import io.github.mribby.customachievements.trigger.Trigger;
import io.github.mribby.customachievements.trigger.TriggerRegistry;
import io.github.mribby.customachievements.trigger.Triggers;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.AchievementPage;

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
    private Map<String, Object> triggers;

    private transient Achievement achievement;

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
                for (Map.Entry<String, Object> entry : triggers.entrySet()) {
                    Trigger trigger = Triggers.TRIGGER_MAP.get(entry.getKey());
                    DataHolder dataHolder = DataHolder.of(trigger, entry.getValue());
                    TriggerRegistry.registerAchievementData(trigger, achievement, dataHolder);
                }
            }
        } catch (Exception e) {
            CustomAchievementsMod.logger.error(String.format("Could not register achievement (ID: %s)", id), e);
        }
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
