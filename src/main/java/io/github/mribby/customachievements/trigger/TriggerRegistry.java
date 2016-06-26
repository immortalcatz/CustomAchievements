package io.github.mribby.customachievements.trigger;

import io.github.mribby.customachievements.dataholder.DataHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TriggerRegistry {
    private static final Map<Trigger, List<Pair<Achievement, DataHolder>>> TRIGGER_ACHIEVEMENT_DATA_MAP = new HashMap<Trigger, List<Pair<Achievement, DataHolder>>>();

    private static List<Pair<Achievement, DataHolder>> getAchievementData(Trigger trigger) {
        List<Pair<Achievement, DataHolder>> list = TRIGGER_ACHIEVEMENT_DATA_MAP.get(trigger);
        if (list == null) {
            list = new ArrayList<Pair<Achievement, DataHolder>>();
            TRIGGER_ACHIEVEMENT_DATA_MAP.put(trigger, list);
        }
        return list;
    }

    public static void registerAchievementData(Trigger trigger, Achievement achievement, DataHolder dataHolder) {
        getAchievementData(trigger).add(Pair.of(achievement, dataHolder));
    }

    public static <T, R> void triggerAchievements(Trigger<T, R> trigger, R eventData, EntityPlayer player) {
        for (Pair<Achievement, DataHolder> pair : getAchievementData(trigger)) {
            DataHolder dataHolder = pair.getRight();
            if (dataHolder.isEqual(eventData)) {
                Achievement achievement = pair.getLeft();
                player.addStat(achievement, 1);
            }
        }
    }
}
