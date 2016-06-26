package io.github.mribby.customachievements.dataholder;

import io.github.mribby.customachievements.trigger.Trigger;

public abstract class DataHolder {
    private static final String WILDCARD = "*";
    private static final String ORE_PREFIX = "*:";

    public static DataHolder of(Trigger trigger, Object obj) {
        if (obj instanceof String) {
            String s = (String) obj;
            if (s.equals(WILDCARD)) {
                return WildcardDataHolder.INSTANCE;
            } else if (s.startsWith(ORE_PREFIX)) {
                return new OreDataHolder(s.substring(ORE_PREFIX.length()));
            }
        }
        return new TriggerDataHolder(trigger, obj);
    }

    public abstract boolean isEqual(Object eventData);
}
