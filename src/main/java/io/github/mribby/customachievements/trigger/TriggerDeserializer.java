package io.github.mribby.customachievements.trigger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class TriggerDeserializer implements JsonDeserializer<Trigger> {
    @Override
    public Trigger deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return Triggers.TRIGGER_MAP.get(json.getAsString());
    }
}
