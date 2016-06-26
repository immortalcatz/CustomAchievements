package io.github.mribby.customachievements.trigger;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

public class EntityClassTrigger extends Trigger<String, Class<? extends Entity>> {
    public EntityClassTrigger(String id) {
        super(id);
    }

    @Override
    public Class<? extends Entity> loadData(String s) {
        return (Class<? extends Entity>) EntityList.stringToClassMapping.get(s);
    }

    @Override
    public boolean isEqual(Class<? extends Entity> dataClass, Class<? extends Entity> eventClass) {
        return dataClass.isAssignableFrom(eventClass);
    }
}
