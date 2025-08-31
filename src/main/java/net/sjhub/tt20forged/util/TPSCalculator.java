package net.sjhub.tt20forged.util;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TPSCalculator {

    public Long lastTick;
    public Long currentTick;

    private double allMissedTicks = 0;
    private final List<Double> tpsHistory = new CopyOnWriteArrayList<>();
    private static final int historyLimit = 40;

    public static final int MAX_TPS = 20;
    public static final int FULL_TICK = 50;

    private static final double SMOOTHING_FACTOR = 0.1;
    private double smoothedMSPT = 50.0;

    public TPSCalculator() {
        MinecraftForge.EVENT_BUS.addListener(this::onTick);
    }

    private void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }

        if (currentTick != null) {
            lastTick = currentTick;
        }

        currentTick = System.currentTimeMillis();

        // Update smoothed MSPT using EMA
        long currentMSPT = getMSPT();
        smoothedMSPT = (currentMSPT * SMOOTHING_FACTOR) + (smoothedMSPT * (1 - SMOOTHING_FACTOR));

        addToHistory(getTPS());
        clearMissedTicks();
        missedTick();
    }

    private void addToHistory(double tps) {
        if (tpsHistory.size() >= historyLimit) {
            tpsHistory.remove(0);
        }

        tpsHistory.add(tps);
    }

    public long getMSPT() {
        if (lastTick == null || currentTick == null) return 0;
        return currentTick - lastTick;
    }

    public double getSmoothedMSPT() {
        return smoothedMSPT;
    }

    public double getAverageTPS() {
        return tpsHistory.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(MAX_TPS);
    }

    public double getTPS() {
        if (smoothedMSPT <= 0) return MAX_TPS;
        double tps = 1000 / smoothedMSPT;
        return Math.min(tps, MAX_TPS);
    }

    public void missedTick() {
        double currentSmoothedMSPT = getSmoothedMSPT();
        if (currentSmoothedMSPT <= 0) return;

        double missed = (currentSmoothedMSPT / (double) FULL_TICK) - 1;
        allMissedTicks += Math.max(0, missed);
    }

    public double getMostAccurateTPS() {
        return Math.min(getTPS(), getAverageTPS());
    }

    public double getAllMissedTicks() {
        return allMissedTicks;
    }

    public int applicableMissedTicks() {
        return (int) Math.floor(allMissedTicks);
    }

    public void clearMissedTicks() {
        allMissedTicks -= applicableMissedTicks();
    }

    public void resetMissedTicks() {
        allMissedTicks = 0;
    }
}