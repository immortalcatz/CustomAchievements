package io.github.mribby.customachievements.trigger;

import io.github.mribby.customachievements.Utils;
import net.minecraft.item.ItemStack;

public class ItemStackTrigger extends Trigger<String, ItemStack> {
    public ItemStackTrigger(String id) {
        super(id);
    }

    @Override
    public ItemStack loadData(String s) {
        return Utils.getItemStackByText(s);
    }

    @Override
    public boolean isEqual(ItemStack dataStack, ItemStack eventStack) {
        return dataStack.isItemEqual(eventStack) && (dataStack.getTagCompound() == null || dataStack.getTagCompound().equals(eventStack.getTagCompound()));
    }
}
