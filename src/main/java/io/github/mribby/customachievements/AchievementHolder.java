package io.github.mribby.customachievements;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.github.mribby.customachievements.trigger.Trigger;
import io.github.mribby.customachievements.trigger.TriggerHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AchievementHolder {
    private String mods;
    private String id;
    private String name;
    private String description;
    private int[] position;
    private String icon;
    private String parent;
    private boolean special;
    private Map<Trigger, Object> triggers;

    private transient String[] modIds;
    private transient ItemStack itemStack;
    private transient Achievement parentAchievement;
    private transient Achievement achievement;
    private transient Map<Trigger, Object> triggerDataMap;

    public String[] getModIds() {
        if (modIds == null && mods != null) {
            modIds = mods.split(" ");
        }
        return modIds;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getColumn() {
        return position[0];
    }

    public int getRow() {
        return position[1];
    }

    public ItemStack getItemStack() {
        if (itemStack == null && icon != null) {
            itemStack = Utils.getItemStackByText(icon);
        }
        return itemStack;
    }

    public Achievement getParent() {
        if (parentAchievement == null && parent != null) {
            parentAchievement = (Achievement) StatList.func_151177_a(String.format("achievement.%s", parent));
        }
        return parentAchievement;
    }

    public boolean isSpecial() {
        return special;
    }

    public Achievement getAchievement() {
        if (achievement == null) {
            achievement = new CustomAchievement();
        }
        return achievement;
    }

    public void registerTriggers(TriggerHandler triggerHandler) {
        if (triggers != null) {
            triggerDataMap = new HashMap<Trigger, Object>();
            for (Map.Entry<Trigger, Object> entry : triggers.entrySet()) {
                Trigger trigger = entry.getKey();
                Object data = readTriggerData(trigger, entry.getValue());
                triggerDataMap.put(trigger, data);
                triggerHandler.registerAchievementTrigger(trigger, this);
            }
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

    public Object getTriggerData(Trigger trigger) {
        return triggerDataMap != null ? triggerDataMap.get(trigger) : null;
    }

    private class CustomAchievement extends Achievement {
        public CustomAchievement() {
            super("achievement." + getId(), getId(), getColumn(), getRow(), getItemStack(), getParent());
            if (getParent() == null) {
                initIndependentStat();
            }
            if (isSpecial()) {
                setSpecial();
            }
            if (getName() != null) {
                statName = new ChatComponentText(getName());
            }
        }

        @Override
        @SideOnly(Side.CLIENT)
        public String getDescription() {
            String description = AchievementHolder.this.getDescription();
            return description != null ? description : super.getDescription();
        }
    }
}
