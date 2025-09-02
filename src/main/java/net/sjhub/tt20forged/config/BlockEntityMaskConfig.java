package net.sjhub.tt20forged.config;

import net.minecraftforge.registries.ForgeRegistries;
import net.sjhub.tt20forged.util.Mask;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityMaskConfig extends TOMLConfiguration {

    private Mask mask;

    public BlockEntityMaskConfig() {
        super("block_entity_mask.toml");

        // First, load existing config from disk.
        reload();

        // Now, set defaults and comments for any options that may have been missing from the file.
        String[] defaultMask = new String[]{"*:*"};
        this.putIfEmpty("type", "whitelist");
        this.putIfEmpty("blocks", defaultMask);

        this.setComment("type", "The type of mask to use. Can be 'whitelist' or 'blacklist'. (Default: whitelist)");
        this.setComment("blocks", "A list of blocks to whitelist or blacklist. (Default: [\"*:*\"])");

        // Save the config to disk, which now includes any newly added defaults.
        this.save();
    }

    public Mask getMask() {
        return this.mask;
    }

    public List<String> getBlocks() {
        return this.getAsStringList("blocks");
    }

    public String getType() {
        return this.getAsString("type");
    }

    public boolean addBlock(String blockId) {
        List<String> blocks = new ArrayList<>(getBlocks());
        if (!blocks.contains(blockId)) {
            blocks.add(blockId);
            this.put("blocks", blocks.toArray(new Object[0]));
            return true;
        }
        return false;
    }

    public boolean removeBlock(String blockId) {
        List<String> blocks = new ArrayList<>(getBlocks());
        if (blocks.remove(blockId)) {
            this.put("blocks", blocks.toArray(new Object[0]));
            return true;
        }
        return false;
    }

    public void setType(String type) {
        this.put("type", type);
    }

    @Override
    public void reload() {
        super.reload();
        // The Mask object is the "cache" for this config, so we recreate it on reload.
        this.mask = new Mask(ForgeRegistries.BLOCKS, this, "blocks");
    }
}