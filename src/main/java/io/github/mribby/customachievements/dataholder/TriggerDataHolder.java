package io.github.mribby.customachievements.dataholder;

import io.github.mribby.customachievements.trigger.Trigger;

import java.util.List;

public class TriggerDataHolder extends DataHolder {
    private final Trigger trigger;
    private final Object data;

    public TriggerDataHolder(Trigger trigger, Object obj) {
        this.trigger = trigger;
        this.data = loadData(trigger, obj);
    }

    private Object loadData(Trigger trigger, Object obj) {
        if (obj instanceof List) {
            List list = (List) obj;
            for (int i = 0; i < list.size(); i++) {
                list.set(i, loadData(trigger, list.get(i)));
            }
            return list;
        } else {
            return trigger.loadData(obj);
        }
    }

    @Override
    public boolean isEqual(Object obj) {
        if (data instanceof List) {
            List list = (List) data;
            for (int i = 0; i < list.size(); i++) {
                if (isEqual(obj)) {
                    return true;
                }
            }
            return false;
        } else {
            return trigger.isEqual(data, obj);
        }
    }
}
