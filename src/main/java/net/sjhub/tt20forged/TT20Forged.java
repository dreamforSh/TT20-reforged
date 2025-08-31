package net.sjhub.tt20forged;

import net.minecraftforge.fml.common.Mod;
import net.sjhub.tt20forged.command.CommandRegistry;
import net.sjhub.tt20forged.config.BlockEntityMaskConfig;
import net.sjhub.tt20forged.config.MainConfig;
import net.sjhub.tt20forged.util.TPSCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

@Mod(TT20Forged.MOD_ID)
public class TT20Forged {

    public static final String MOD_ID = "tt20forged";
    public static final Logger LOGGER = LoggerFactory.getLogger(TT20Forged.MOD_ID);
    public static final String VERSION = "0.7.4";
    public static final TPSCalculator TPS_CALCULATOR = new TPSCalculator();
    public static final MainConfig config = new MainConfig();
    public static final BlockEntityMaskConfig blockEntityMaskConfig = new BlockEntityMaskConfig();
    public static boolean warned = false;

    public TT20Forged() {
        LOGGER.info("Starting TT20...");
        CompletableFuture.runAsync(() -> {
            try {
                // ModUpdater.check(); // TODO: update check
            } catch (RuntimeException e) {
                LOGGER.error("Failed to check for updates.");
                e.printStackTrace();
            }

        });
        CommandRegistry.registerCommands();
    }
}
