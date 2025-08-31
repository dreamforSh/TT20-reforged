package net.sjhub.tt20forged.config;

import com.google.gson.*;
import net.minecraftforge.fml.loading.FMLPaths;
import net.sjhub.tt20forged.TT20Forged;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class JSONConfiguration {
    private final String fileName;

    private JsonObject json;
    private final Gson gson;

    public JSONConfiguration(String fileName) {
        this.fileName = fileName;
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        // load file into configuration
        reload();
    }

    public void save() {
        try {
            File file = new File(getAbsolutePath() + fileName);
            file.getParentFile().mkdirs();

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(gson.toJson(json));
            }

            reload();
        } catch (IOException e) {
            TT20Forged.LOGGER.error("(TT20) Failed to save JSONConfiguration '" + fileName + "'", e);
        }
    }

    public void reload() {
        File file = new File(getAbsolutePath() + fileName);
        try {
            file.getParentFile().mkdirs();

            if (!file.exists()) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("{}");
                }
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                JsonElement parsed = gson.fromJson(reader, JsonElement.class);
                if (parsed == null || !parsed.isJsonObject()) {
                    this.json = new JsonObject();
                } else {
                    this.json = parsed.getAsJsonObject();
                }
            }
        } catch (IOException | JsonSyntaxException e) {
            TT20Forged.LOGGER.error("(TT20) Failed to reload JSONConfiguration '" + fileName + "'", e);
            this.json = new JsonObject();
        }
    }

    public boolean has(String key) {
        JsonElement element = this.json;

        for (String part : splitKey(key)) {
            if (element == null || !element.isJsonObject() || !element.getAsJsonObject().has(part)) {
                return false;
            }
            element = element.getAsJsonObject().get(part);
        }
        return element != null;
    }

    private @Nullable Object[] preparePut(String key, @Nullable Object value) {
        String[] parts = splitKey(key);
        JsonObject current = json;

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];

            if (!current.has(part) || !current.get(part).isJsonObject()) {
                current.add(part, new JsonObject());
            }

            current = current.getAsJsonObject(part);
        }

        if (value == null) {
            current.remove(parts[parts.length - 1]);
            return null;
        }

        return new Object[]{current, parts[parts.length - 1]};
    }

    public void put(String key, @Nullable String value) {
        Object[] values = preparePut(key, value);
        if (values == null) return;

        JsonObject current = (JsonObject) values[0];
        current.addProperty((String) values[1], value);
    }

    public void put(String key, @Nullable Object[] value) {
        Object[] values = preparePut(key, value);
        if (values == null) return;

        JsonObject current = (JsonObject) values[0];

        JsonArray array = new JsonArray();
        for (Object object : value) {
            if (object instanceof String) {
                array.add((String) object);
            } else if (object instanceof Boolean) {
                array.add((Boolean) object);
            } else if (object instanceof Number) {
                array.add((Number) object);
            } else {
                throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
            }
        }

        current.add((String) values[1], array);
    }

    public void put(String key, @Nullable Boolean value) {
        Object[] values = preparePut(key, value);
        if (values == null) return;

        JsonObject current = (JsonObject) values[0];
        current.addProperty((String) values[1], value);
    }

    public void put(String key, @Nullable Integer value) {
        Object[] values = preparePut(key, value);
        if (values == null) return;

        JsonObject current = (JsonObject) values[0];
        current.addProperty((String) values[1], value);
    }

    public void putIfEmpty(String key, @NotNull String value) {
        Objects.requireNonNull(value);

        if (!has(key)) {
            put(key, value);
        }
    }

    public void putIfEmpty(String key, @NotNull Object[] value) {
        Objects.requireNonNull(value);

        if (!has(key)) {
            put(key, value);
        }
    }

    public void putIfEmpty(String key, @NotNull Boolean value) {
        Objects.requireNonNull(value);

        if (!has(key)) {
            put(key, value);
        }
    }

    public void putIfEmpty(String key, @NotNull Integer value) {
        Objects.requireNonNull(value);

        if (!has(key)) {
            put(key, value);
        }
    }

    public String getAsString(String key) {
        JsonElement element = json;

        for (String part : splitKey(key)) {
            if (element == null || !element.isJsonObject() || !element.getAsJsonObject().has(part)) {
                return null;
            }
            element = element.getAsJsonObject().get(part);
        }

        return element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()
                ? element.getAsString()
                : null;
    }

    public Boolean getAsBoolean(String key) {
        JsonElement element = json;

        for (String part : splitKey(key)) {
            if (element == null || !element.isJsonObject() || !element.getAsJsonObject().has(part)) {
                return null;
            }
            element = element.getAsJsonObject().get(part);
        }

        return element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()
                ? element.getAsBoolean()
                : null;
    }

    public Integer getAsInt(String key) {
        JsonElement element = json;

        for (String part : splitKey(key)) {
            if (element == null || !element.isJsonObject() || !element.getAsJsonObject().has(part)) {
                return null;
            }
            element = element.getAsJsonObject().get(part);
        }

        return element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()
                ? element.getAsJsonPrimitive().getAsInt()
                : null;
    }

    public JsonArray getAsArray(String key) {
        JsonElement element = json;

        for (String part : splitKey(key)) {
            if (element == null || !element.isJsonObject() || !element.getAsJsonObject().has(part)) {
                return null;
            }
            element = element.getAsJsonObject().get(part);
        }

        return element != null && element.isJsonArray()
                ? element.getAsJsonArray()
                : null;
    }

    public String getAsStringOrDefault(String key, String def) {
        if (!has(key)) {
            return def;
        }
        return getAsString(key);
    }

    public Boolean getAsBooleanOrDefault(String key, Boolean def) {
        if (!has(key)) {
            return def;
        }
        return getAsBoolean(key);
    }

    public Integer getAsIntOrDefault(String key, Integer def) {
        if (!has(key)) {
            return def;
        }
        return getAsInt(key);
    }

    public String getFileName() {
        return fileName;
    }

    public String[] keys(String key) {
        // If the key is empty, return all string keys in the root JSON object
        if (key.isEmpty()) {
            Set<String> keys = new HashSet<>();
            collectStringKeys(json, "", keys);
            return keys.toArray(new String[0]);
        }

        // Traverse the JSON structure to the specified key
        JsonElement element = json;
        for (String part : splitKey(key)) {
            if (element == null || !element.isJsonObject() || !element.getAsJsonObject().has(part)) {
                return new String[0];
            }
            element = element.getAsJsonObject().get(part);
        }

        // If the element is a JSON object, collect its string keys
        if (element != null && element.isJsonObject()) {
            Set<String> keys = new HashSet<>();
            collectStringKeys(element.getAsJsonObject(), "", keys);
            return keys.toArray(new String[0]);
        } else {
            return new String[0];
        }
    }

    public String[] keys() {
        return keys("");
    }

    public String[] splitKey(String key) {
        return key.split("\\.");
    }

    public String getAbsolutePath() {
        return FMLPaths.CONFIGDIR.get().toAbsolutePath() + "/tt20/";
    }

    private void collectStringKeys(JsonObject jsonObject, String prefix, Set<String> keys) {
        for (String key : jsonObject.keySet()) {
            JsonElement element = jsonObject.get(key);
            String fullKey = prefix.isEmpty() ? key : prefix + "." + key;

            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                keys.add(fullKey);
            } else if (element.isJsonObject()) {
                collectStringKeys(element.getAsJsonObject(), fullKey, keys);
            }
        }
    }
}