package io.github.mribby.customachievements.trigger;

import io.github.mribby.customachievements.AchievementHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TriggerRegistry {
    private static final Map<Trigger, List<AchievementHolder>> TRIGGER_ACHIEVEMENT_MAP = new HashMap<Trigger, List<AchievementHolder>>();

    private static List<AchievementHolder> getAchievementHolders(Trigger trigger) {
        List<AchievementHolder> achievementHolders = TRIGGER_ACHIEVEMENT_MAP.get(trigger);
        if (achievementHolders == null) {
            achievementHolders = new ArrayList<AchievementHolder>();
            TRIGGER_ACHIEVEMENT_MAP.put(trigger, achievementHolders);
        }
        return achievementHolders;
    }

    public static void registerAchievementTrigger(Trigger trigger, AchievementHolder achievementHolder) {
        getAchievementHolders(trigger).add(achievementHolder);
    }

    public static void triggerAchievements(Trigger trigger, Object eventData, EntityPlayer player) {
        for (AchievementHolder achievementHolder : getAchievementHolders(trigger)) {
            Object data = achievementHolder.getTriggerData(trigger);
            if (isTriggered(trigger, data, eventData)) {
                Achievement achievement = achievementHolder.getAchievement();
                player.addStat(achievement, 1);
            }
        }
    }

    private static boolean isTriggered(Trigger trigger, Object data, Object eventData) {
        if (data instanceof List) {
            List dataList = (List) data;
            for (int i = 0; i < dataList.size(); i++) {
                if (isTriggered(trigger, dataList.get(i), eventData)) {
                    return true;
                }
            }
            return false;
        } else {
            return data.equals(eventData) || trigger.isTriggered(data, eventData);
        }
    }
}
