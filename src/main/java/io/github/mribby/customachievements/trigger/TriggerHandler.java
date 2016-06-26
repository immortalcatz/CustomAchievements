package io.github.mribby.customachievements.trigger;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class TriggerHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        TriggerRegistry.triggerAchievements(Triggers.PICKUP, event.pickedUp.getEntityItem(), event.player);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemCraft(PlayerEvent.ItemCraftedEvent event) {
        TriggerRegistry.triggerAchievements(Triggers.CRAFT, event.crafting, event.player);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemSmelt(PlayerEvent.ItemSmeltedEvent event) {
        TriggerRegistry.triggerAchievements(Triggers.SMELT, event.smelting, event.player);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLivingDeath(LivingDeathEvent event) {
        EntityLivingBase defender = event.entityLiving;
        Entity attacker = event.source.getSourceOfDamage();
        if (defender != null && attacker != null) {
            if (attacker instanceof EntityPlayer) {
                TriggerRegistry.triggerAchievements(Triggers.KILL, defender.getClass(), (EntityPlayer) attacker);
            } else if (defender instanceof EntityPlayer) {
                TriggerRegistry.triggerAchievements(Triggers.DIE, attacker.getClass(), (EntityPlayer) defender);
            }
        }
    }
}
