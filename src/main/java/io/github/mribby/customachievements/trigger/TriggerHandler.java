package io.github.mribby.customachievements.trigger;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import io.github.mribby.customachievements.AchievementHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TriggerHandler {
    private final Map<Trigger, List<AchievementHolder>> ACHIEVEMENT_TRIGGER_MAP = new HashMap<Trigger, List<AchievementHolder>>();

    public TriggerHandler() {
        for (Trigger trigger : Triggers.TRIGGER_MAP.values()) {
            ACHIEVEMENT_TRIGGER_MAP.put(trigger, new ArrayList<AchievementHolder>());
        }
    }

    public void registerAchievementTrigger(Trigger trigger, AchievementHolder achievementHolder) {
        ACHIEVEMENT_TRIGGER_MAP.get(trigger).add(achievementHolder);
    }

    public void triggerAchievements(Trigger trigger, Object eventData, EntityPlayer player) {
        for (AchievementHolder achievementHolder : ACHIEVEMENT_TRIGGER_MAP.get(trigger)) {
            Object data = achievementHolder.getTriggerData(trigger);
            Achievement achievement = achievementHolder.getAchievement();
            if (isTriggered(trigger, data, eventData)) {
                player.addStat(achievement, 1);
            }
        }
    }

    private boolean isTriggered(Trigger trigger, Object data, Object eventData) {
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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        triggerAchievements(Triggers.PICKUP, event.pickedUp.getEntityItem(), event.player);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemCraft(PlayerEvent.ItemCraftedEvent event) {
        triggerAchievements(Triggers.CRAFT, event.crafting, event.player);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemSmelt(PlayerEvent.ItemSmeltedEvent event) {
        triggerAchievements(Triggers.SMELT, event.smelting, event.player);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDeath(LivingDeathEvent event) {
        EntityLivingBase defender = event.entityLiving;
        Entity attacker = event.source.getEntity();
        if (attacker instanceof EntityPlayer) {
            triggerAchievements(Triggers.KILL, defender.getClass(), (EntityPlayer) attacker);
        }
    }
}
