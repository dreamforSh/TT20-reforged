package net.sjhub.tt20forged.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TOMLConfiguration {
    private final String fileName;
    protected final CommentedFileConfig config;

    public TOMLConfiguration(String fileName) {
        this.fileName = fileName;
        this.config = CommentedFileConfig.of(getAbsolutePath() + fileName);
        // DO NOT call reload() here, it must be called by the subclass constructor
    }

    public void save() {
        config.save();
    }

    public void reload() {
        File file = new File(getAbsolutePath() + fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        config.load();
    }

    public boolean has(String key) {
        return config.contains(key);
    }

    public void setComment(String key, String comment) {
        config.setComment(key, comment);
    }

    public void put(String key, @Nullable String value) {
        config.set(key, value);
    }

    public void put(String key, @Nullable Object[] value) {
        if (value == null) {
            config.remove(key);
            return;
        }
        config.set(key, Arrays.asList(value));
    }

    public void put(String key, @Nullable Boolean value) {
        config.set(key, value);
    }

    public void put(String key, @Nullable Integer value) {
        config.set(key, value);
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
        return config.get(key);
    }

    public Boolean getAsBoolean(String key) {
        return config.get(key);
    }

    public Integer getAsInt(String key) {
        return config.get(key);
    }

    public List<String> getAsStringList(String key) {
        return config.get(key);
    }

    public String getAsStringOrDefault(String key, String def) {
        return config.getOrElse(key, def);
    }

    public Boolean getAsBooleanOrDefault(String key, Boolean def) {
        return config.getOrElse(key, def);
    }

    public Integer getAsIntOrDefault(String key, Integer def) {
        return config.getOrElse(key, def);
    }

    public String getFileName() {
        return fileName;
    }

    public String[] keys(String key) {
        if (key.isEmpty()) {
            return config.valueMap().keySet().toArray(new String[0]);
        }
        Object value = config.get(key);
        if (value instanceof com.electronwill.nightconfig.core.Config) {
            return ((com.electronwill.nightconfig.core.Config) value).valueMap().keySet().toArray(new String[0]);
        }
        return new String[0];
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
}
