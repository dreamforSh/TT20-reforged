package net.sjhub.tt20forged.config;

public class ConfigOption<T> {
    private final String key;
    private final T defaultValue;

    public ConfigOption(String key, T defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public Class<?> getType() {
        return defaultValue.getClass();
    }
}
