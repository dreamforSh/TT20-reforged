package net.sjhub.tt20forged.config;

import net.minecraftforge.registries.ForgeRegistries;
import net.sjhub.tt20forged.util.Mask;

public class BlockEntityMaskConfig extends JSONConfiguration {

    private Mask mask;

    public BlockEntityMaskConfig() {
        super("block_entity_mask.json");
        String[] defaultMask = new String[]{"*:*"};
        this.putIfEmpty("type", "whitelist");
        this.putIfEmpty("blocks", defaultMask);
        this.save();
        this.reload();
    }

    public Mask getMask() {
        return this.mask;
    }

    public void reload() {
        super.reload();
        this.mask = new Mask(ForgeRegistries.BLOCKS, this, "blocks");
    }
}