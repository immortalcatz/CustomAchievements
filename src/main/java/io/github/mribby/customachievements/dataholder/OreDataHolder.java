package io.github.mribby.customachievements.dataholder;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDataHolder extends DataHolder {
    private final String name;

    public OreDataHolder(String name) {
        this.name = name;
    }

    @Override
    public boolean isEqual(Object obj) {
        ItemStack stack = (ItemStack) obj;
        for (ItemStack oreStack : OreDictionary.getOres(name)) {
            if (OreDictionary.itemMatches(oreStack, stack, false)) {
                return true;
            }
        }
        return false;
    }
}
