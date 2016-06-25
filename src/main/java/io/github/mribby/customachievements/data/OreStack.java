package io.github.mribby.customachievements.data;

import io.github.mribby.customachievements.Utils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreStack {
    private final String name;

    public OreStack(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemStack) {
            ItemStack input = (ItemStack) obj;
            for (ItemStack target : OreDictionary.getOres(name)) {
                if (target.getItem() == input.getItem() && (target.getItemDamage() == OreDictionary.WILDCARD_VALUE || target.getItemDamage() == input.getItemDamage())) {
                    return true;
                }
            }
        }
        return false;
    }
}
