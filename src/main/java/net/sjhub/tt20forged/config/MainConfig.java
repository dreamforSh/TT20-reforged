package net.sjhub.tt20forged.config;

import java.util.*;

public class MainConfig extends JSONConfiguration {

    private static final List<ConfigOption<Boolean>> BOOLEAN_OPTIONS = new ArrayList<>();
    private int tickRepeatCap;

    // Centralized definition of all config options
    static {
        BOOLEAN_OPTIONS.add(new ConfigOption<>("enabled", true));
        BOOLEAN_OPTIONS.add(new ConfigOption<>("block-entity-acceleration", false));
        BOOLEAN_OPTIONS.add(new ConfigOption<>("block-breaking-acceleration", true));
        BOOLEAN_OPTIONS.add(new ConfigOption<>("potion-effect-acceleration", true));
        BOOLEAN_OPTIONS.add(new ConfigOption<>("fluid-acceleration", true));
        BOOLEAN_OPTIONS.add(new ConfigOption<>("pickup-acceleration", true));
        BOOLEAN_OPTIONS.add(new ConfigOption<>("eating-acceleration", true));
        BOOLEAN_OPTIONS.add(new ConfigOption<>("portal-acceleration", true));
        BOOLEAN_OPTIONS.add(new ConfigOption<>("sleeping-acceleration", true));
        BOOLEAN_OPTIONS.add(new ConfigOption<>("server-watchdog", true));
        BOOLEAN_OPTIONS.add(new ConfigOption<>("singleplayer-warning", true));
        BOOLEAN_OPTIONS.add(new ConfigOption<>("time-acceleration", true));
        BOOLEAN_OPTIONS.add(new ConfigOption<>("bow-acceleration", true));
        BOOLEAN_OPTIONS.add(new ConfigOption<>("crossbow-acceleration", true));
        BOOLEAN_OPTIONS.add(new ConfigOption<>("random-tickspeed-acceleration", true));
        BOOLEAN_OPTIONS.add(new ConfigOption<>("automatic-updater", true));
    }

    private final Map<String, Boolean> valueCache = new HashMap<>();

    public MainConfig() {
        super("config.json");

        for (ConfigOption<Boolean> option : BOOLEAN_OPTIONS) {
            putIfEmpty(option.getKey(), option.getDefaultValue());
        }
        putIfEmpty("tick-repeat-cap", 10);

        save();
    }

    @Override
    public void reload() {
        super.reload();

        if (valueCache == null) return;

        for (ConfigOption<Boolean> option : BOOLEAN_OPTIONS) {
            valueCache.put(option.getKey(), getAsBooleanOrDefault(option.getKey(), option.getDefaultValue()));
        }
        this.tickRepeatCap = getAsIntOrDefault("tick-repeat-cap", 10);
    }

    public static List<ConfigOption<Boolean>> getBooleanOptions() {
        return BOOLEAN_OPTIONS;
    }

    public void setBoolean(String key, boolean value) {
        put(key, value);
        valueCache.put(key, value);
    }

    public boolean getBoolean(String key) {
        return valueCache.getOrDefault(key, false);
    }

    public int getTickRepeatCap() {
        return tickRepeatCap;
    }

    // The public API remains the same, but now reads from the cache.
    // This avoids having to refactor all the mixins.

    public void enabled(boolean enabled) { setBoolean("enabled", enabled); }
    public boolean enabled() { return valueCache.getOrDefault("enabled", true); }

    public void serverWatchdog(boolean enabled) { setBoolean("server-watchdog", enabled); }
    public boolean serverWatchdog() { return valueCache.getOrDefault("server-watchdog", true); }

    public void blockEntityAcceleration(boolean enabled) { setBoolean("block-entity-acceleration", enabled); }
    public boolean blockEntityAcceleration() { return valueCache.getOrDefault("block-entity-acceleration", false); }

    public void blockBreakingAcceleration(boolean enabled) { setBoolean("block-breaking-acceleration", enabled); }
    public boolean blockBreakingAcceleration() { return valueCache.getOrDefault("block-breaking-acceleration", true); }

    public void potionEffectAcceleration(boolean enabled) { setBoolean("potion-effect-acceleration", enabled); }
    public boolean potionEffectAcceleration() { return valueCache.getOrDefault("potion-effect-acceleration", true); }

    public void fluidAcceleration(boolean enabled) { setBoolean("fluid-acceleration", enabled); }
    public boolean fluidAcceleration() { return valueCache.getOrDefault("fluid-acceleration", true); }

    public void pickupAcceleration(boolean enabled) { setBoolean("pickup-acceleration", enabled); }
    public boolean pickupAcceleration() { return valueCache.getOrDefault("pickup-acceleration", true); }

    public void eatingAcceleration(boolean enabled) { setBoolean("eating-acceleration", enabled); }
    public boolean eatingAcceleration() { return valueCache.getOrDefault("eating-acceleration", true); }

    public void portalAcceleration(boolean enabled) { setBoolean("portal-acceleration", enabled); }
    public boolean portalAcceleration() { return valueCache.getOrDefault("portal-acceleration", true); }

    public void sleepingAcceleration(boolean enabled) { setBoolean("sleeping-acceleration", enabled); }
    public boolean sleepingAcceleration() { return valueCache.getOrDefault("sleeping-acceleration", true); }

    public void automaticUpdater(boolean enabled) { setBoolean("automatic-updater", enabled); }
    public boolean automaticUpdater() { return valueCache.getOrDefault("automatic-updater", true); }

    public void singlePlayerWarning(boolean enabled) { setBoolean("singleplayer-warning", enabled); }
    public boolean singlePlayerWarning() { return valueCache.getOrDefault("singleplayer-warning", true); }

    public void timeAcceleration(boolean enabled) { setBoolean("time-acceleration", enabled); }
    public boolean timeAcceleration() { return valueCache.getOrDefault("time-acceleration", true); }

    public void bowAcceleration(boolean enabled) { setBoolean("bow-acceleration", enabled); }
    public boolean bowAcceleration() { return valueCache.getOrDefault("bow-acceleration", true); }

    public void crossbowAcceleration(boolean enabled) { setBoolean("crossbow-acceleration", enabled); }
    public boolean crossbowAcceleration() { return valueCache.getOrDefault("crossbow-acceleration", true); }

    public void randomTickSpeedAcceleration(boolean enabled) { setBoolean("random-tickspeed-acceleration", enabled); }
    public boolean randomTickSpeedAcceleration() { return valueCache.getOrDefault("random-tickspeed-acceleration", true); }
}
