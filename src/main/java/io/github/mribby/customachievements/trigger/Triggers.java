package io.github.mribby.customachievements.trigger;

import java.util.Map;
import java.util.TreeMap;

public class Triggers {
    public static final Map<String, Trigger> TRIGGER_MAP = new TreeMap<String, Trigger>(String.CASE_INSENSITIVE_ORDER);
    public static final ItemStackTrigger PICKUP = new ItemStackTrigger("pickup");
    public static final ItemStackTrigger CRAFT = new ItemStackTrigger("craft");
    public static final ItemStackTrigger SMELT = new ItemStackTrigger("smelt");
    //TODO public static final ItemStackTrigger BREW = new ItemStackTrigger("brew");
    public static final EntityClassTrigger KILL = new EntityClassTrigger("kill");
}
