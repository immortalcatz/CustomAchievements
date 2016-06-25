package io.github.mribby.customachievements.trigger;

import io.github.mribby.customachievements.Utils;
import net.minecraft.item.ItemStack;

public class ItemStackTrigger extends Trigger<String> {
    public ItemStackTrigger(String id) {
        super(id);
    }

    @Override
    public Object readData(String s) {
        return Utils.getItemStackByText(s);
    }

    @Override
    public boolean isTriggered(Object data, Object eventData) {
        ItemStack dataStack = (ItemStack) data;
        ItemStack eventStack = (ItemStack) eventData;
        return dataStack.isItemEqual(eventStack) && (dataStack.getTagCompound() == null || dataStack.getTagCompound().equals(eventStack.getTagCompound()));
    }
}
