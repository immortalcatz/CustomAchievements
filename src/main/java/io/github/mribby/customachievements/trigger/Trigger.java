package io.github.mribby.customachievements.trigger;

public abstract class Trigger<T, R> {
    public Trigger(String id) {
        Triggers.TRIGGER_MAP.put(id, this);
    }

    public abstract R loadData(T t);

    public abstract boolean isEqual(R data, R eventData);
}
