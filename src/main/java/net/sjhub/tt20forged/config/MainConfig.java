package net.sjhub.tt20forged.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainConfig extends TOMLConfiguration {

    // Centralized definition of all config options
    private static final List<ConfigOption<Boolean>> BOOLEAN_OPTIONS = Arrays.asList(
            new ConfigOption<>("enabled", true, "Enables or disables the mod. (Default: true)"),
            new ConfigOption<>("block-entity-acceleration", false, "Accelerates block entities. (Default: false)"),
            new ConfigOption<>("block-breaking-acceleration", true, "Accelerates block breaking. (Default: true)"),
            new ConfigOption<>("potion-effect-acceleration", true, "Accelerates potion effects. (Default: true)"),
            new ConfigOption<>("fluid-acceleration", true, "Accelerates fluid flow. (Default: true)"),
            new ConfigOption<>("pickup-acceleration", true, "Accelerates item pickup. (Default: true)"),
            new ConfigOption<>("eating-acceleration", true, "Accelerates eating and drinking. (Default: true)"),
            new ConfigOption<>("portal-acceleration", true, "Accelerates portal travel. (Default: true)"),
            new ConfigOption<>("sleeping-acceleration", true, "Accelerates sleeping. (Default: true)"),
            new ConfigOption<>("server-watchdog", true, "Enables the server watchdog. (Default: true)"),
            new ConfigOption<>("singleplayer-warning", true, "Shows a warning in singleplayer. (Default: true)"),
            new ConfigOption<>("time-acceleration", true, "Accelerates the passage of time. (Default: true)"),
            new ConfigOption<>("bow-acceleration", true, "Accelerates bow drawing. (Default: true)"),
            new ConfigOption<>("crossbow-acceleration", true, "Accelerates crossbow loading. (Default: true)"),
            new ConfigOption<>("random-tickspeed-acceleration", true, "Accelerates random block ticks. (Default: true)"),
            new ConfigOption<>("automatic-updater", true, "Enables the automatic updater. (Default: true)")
    );

    private final Map<String, Boolean> valueCache = new HashMap<>();
    private int tickRepeatCap;

    public MainConfig() {
        super("config.toml");

        // First, load existing config from disk to populate the config object.
        // Then, populate the cache from the config object.
        reload();

        // Now, set defaults and comments for any options that may have been missing from the file.
        for (ConfigOption<Boolean> option : BOOLEAN_OPTIONS) {
            putIfEmpty(option.getKey(), option.getDefaultValue());
            setComment(option.getKey(), option.getComment());
        }
        putIfEmpty("tick-repeat-cap", 10);
        setComment("tick-repeat-cap", "The maximum number of times a tick can be repeated. (Default: 10)");

        // Save the config to disk, which now includes any newly added defaults.
        save();
    }

    @Override
    public void reload() {
        super.reload(); // Loads the file into the config object

        // (Re)populate the cache from the configuration file, using defaults if values are missing.
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
        // The cache is the single source of truth for the current value.
        // It's populated on load and updated by setters.
        return valueCache.getOrDefault(key, false); // Fallback for safety, though key should always exist.
    }

    public int getTickRepeatCap() {
        return tickRepeatCap;
    }

    public void setTickRepeatCap(int cap) {
        this.tickRepeatCap = cap;
        put("tick-repeat-cap", cap);
    }

    // The public API remains the same, but now reads from the cache and is DRY.
    public void enabled(boolean enabled) { setBoolean("enabled", enabled); }
    public boolean enabled() { return getBoolean("enabled"); }

    public void serverWatchdog(boolean enabled) { setBoolean("server-watchdog", enabled); }
    public boolean serverWatchdog() { return getBoolean("server-watchdog"); }

    public void blockEntityAcceleration(boolean enabled) { setBoolean("block-entity-acceleration", enabled); }
    public boolean blockEntityAcceleration() { return getBoolean("block-entity-acceleration"); }

    public void blockBreakingAcceleration(boolean enabled) { setBoolean("block-breaking-acceleration", enabled); }
    public boolean blockBreakingAcceleration() { return getBoolean("block-breaking-acceleration"); }

    public void potionEffectAcceleration(boolean enabled) { setBoolean("potion-effect-acceleration", enabled); }
    public boolean potionEffectAcceleration() { return getBoolean("potion-effect-acceleration"); }

    public void fluidAcceleration(boolean enabled) { setBoolean("fluid-acceleration", enabled); }
    public boolean fluidAcceleration() { return getBoolean("fluid-acceleration"); }

    public void pickupAcceleration(boolean enabled) { setBoolean("pickup-acceleration", enabled); }
    public boolean pickupAcceleration() { return getBoolean("pickup-acceleration"); }

    public void eatingAcceleration(boolean enabled) { setBoolean("eating-acceleration", enabled); }
    public boolean eatingAcceleration() { return getBoolean("eating-acceleration"); }

    public void portalAcceleration(boolean enabled) { setBoolean("portal-acceleration", enabled); }
    public boolean portalAcceleration() { return getBoolean("portal-acceleration"); }

    public void sleepingAcceleration(boolean enabled) { setBoolean("sleeping-acceleration", enabled); }
    public boolean sleepingAcceleration() { return getBoolean("sleeping-acceleration"); }

    public void automaticUpdater(boolean enabled) { setBoolean("automatic-updater", enabled); }
    public boolean automaticUpdater() { return getBoolean("automatic-updater"); }

    public void singlePlayerWarning(boolean enabled) { setBoolean("singleplayer-warning", enabled); }
    public boolean singlePlayerWarning() { return getBoolean("singleplayer-warning"); }

    public void timeAcceleration(boolean enabled) { setBoolean("time-acceleration", enabled); }
    public boolean timeAcceleration() { return getBoolean("time-acceleration"); }

    public void bowAcceleration(boolean enabled) { setBoolean("bow-acceleration", enabled); }
    public boolean bowAcceleration() { return getBoolean("bow-acceleration"); }

    public void crossbowAcceleration(boolean enabled) { setBoolean("crossbow-acceleration", enabled); }
    public boolean crossbowAcceleration() { return getBoolean("crossbow-acceleration"); }

    public void randomTickSpeedAcceleration(boolean enabled) { setBoolean("random-tickspeed-acceleration", enabled); }
    public boolean randomTickSpeedAcceleration() { return getBoolean("random-tickspeed-acceleration"); }
}
