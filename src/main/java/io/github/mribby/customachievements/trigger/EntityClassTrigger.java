package io.github.mribby.customachievements.trigger;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

public class EntityClassTrigger extends Trigger<String> {
    public EntityClassTrigger(String id) {
        super(id);
    }

    @Override
    public Object readData(String s) {
        return EntityList.stringToClassMapping.get(s);
    }

    @Override
    public boolean isTriggered(Object data, Object eventData) {
        Class dataClass = (Class) data;
        Class eventClass = (Class) eventData;
        return dataClass.isAssignableFrom(eventClass);
    }
}
