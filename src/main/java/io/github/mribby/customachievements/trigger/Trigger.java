package io.github.mribby.customachievements.trigger;

public abstract class Trigger<T> {
    private final String id;

    public Trigger(String id) {
        this.id = id;
        Triggers.TRIGGER_MAP.put(id, this);
    }

    public String getId() {
        return id;
    }

    public abstract Object readData(T t);

    public abstract boolean isTriggered(Object data, Object eventData);
}
