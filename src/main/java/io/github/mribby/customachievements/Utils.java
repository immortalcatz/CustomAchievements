package io.github.mribby.customachievements;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatList;
import net.minecraft.util.StringUtils;
import net.minecraftforge.oredict.OreDictionary;

public class Utils {
    private static final String WILDCARD = "*";

    public static ItemStack getItemStackByText(String text) {
        if (StringUtils.isNullOrEmpty(text)) {
            return null;
        }

        String[] args = text.split(" ");
        Item item = getItemByText(args[0]);
        int amount = 1;
        int meta = 0;

        if (args.length > 1) {
            if (WILDCARD.equals(args[1])) {
                meta = OreDictionary.WILDCARD_VALUE;
            } else {
                try {
                    meta = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    CustomAchievementsMod.logger.error(String.format("Invalid item metadata: %s", args[2]));
                }
            }
        }

        ItemStack stack = new ItemStack(item, amount, meta);

        if (args.length > 2) {
            String nbtString = getStringFromNthArg(args, 2);
            try {
                NBTBase nbt = JsonToNBT.func_150315_a(nbtString);
                if (nbt instanceof NBTTagCompound) {
                    stack.setTagCompound((NBTTagCompound) nbt);
                } else {
                    CustomAchievementsMod.logger.error(String.format("Incorrect item NBT tag: %s", nbtString));
                }
            } catch (NBTException e) {
                CustomAchievementsMod.logger.error(String.format("Invalid item NBT tag: %s", nbtString), e);
            }
        }

        return stack;
    }

    public static Item getItemByText(String text) {
        Item item = (Item) Item.itemRegistry.getObject(text);

        if (item == null) {
            try {
                item = Item.getItemById(Integer.parseInt(text));
            } catch (NumberFormatException e) {
                CustomAchievementsMod.logger.error(String.format("Invalid item: %s", text), e);
            }
        }

        return item;
    }

    public static String getStringFromNthArg(String[] args, int index) {
        StringBuilder builder = new StringBuilder();

        for (int i = index; i < args.length; ++i) {
            if (i > index) {
                builder.append(" ");
            }

            builder.append(args[i]);
        }

        return builder.toString();
    }

    public static Achievement getAchievementById(String id) {
        if (StringUtils.isNullOrEmpty(id)) {
            return null;
        } else {
            return (Achievement) StatList.func_151177_a(String.format("achievement.%s", id));
        }
    }
}
