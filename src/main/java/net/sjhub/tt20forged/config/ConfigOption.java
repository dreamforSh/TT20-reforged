package net.sjhub.tt20forged.config;

public class ConfigOption<T> {
    private final String key;
    private final T defaultValue;
    private final String comment;

    public ConfigOption(String key, T defaultValue, String comment) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.comment = comment;
    }

    public String getKey() {
        return key;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public String getComment() {
        return comment;
    }

    public Class<?> getType() {
        return defaultValue.getClass();
    }
}
