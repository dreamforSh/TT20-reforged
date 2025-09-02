package net.sjhub.tt20forged.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.sjhub.tt20forged.TT20Forged;
import net.sjhub.tt20forged.config.TOMLConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Mask {

    private final TOMLConfiguration file;
    private final MaskType maskType;
    private final IForgeRegistry<?> registry;
    private final RegistryIndex index;
    private final Set<ResourceLocation> entries;

    public Mask(IForgeRegistry<?> registry, TOMLConfiguration file, String maskKey) {
        this.file = file;

        String type = file.getAsString("type");
        if (type == null) {
            TT20Forged.LOGGER.warn("(TT20) Mask type is missing in '{}', defaulting to 'whitelist'.", file.getFileName());
            this.maskType = MaskType.WHITELIST;
        } else {
            this.maskType = MaskType.fromString(type);
        }

        this.registry = registry;
        this.index = RegistryIndex.getIndex(this.registry);
        this.entries = new HashSet<>();

        List<String> maskArray = file.getAsStringList(maskKey);
        if (maskArray == null) {
            TT20Forged.LOGGER.error("(TT20) Mask entry '{}' is missing or not an array in '{}'.", maskKey, file.getFileName());
            return;
        }

        for (String element : maskArray) {
            entries.addAll(manageEntry(element));
        }
    }

    @SuppressWarnings("ConstantConditions")
    public List<ResourceLocation> manageEntry(String entry) {
        String[] split = entry.split(":");

        if (split.length != 2) {
            TT20Forged.LOGGER.error("(TT20) '{}' is not a valid identifier. Correct format is <namespace>:<path>", entry);
            return new ArrayList<>();
        }

        // if *:*
        if (split[0].equals("*") && split[1].equals("*")) {
            return index.getIdentifiers();
        }

        // if <namespace>:<path>
        if (!split[0].equals("*") && !split[1].equals("*")) {
            return List.of(ResourceLocation.tryBuild(split[0], split[1]));
        }

        // if *:<path>
        if (split[0].equals("*") && !split[1].equals("*")) {
            return index.getPathIndex().getOrDefault(split[1], new ArrayList<>());
        }


        // if <namespace>:*
        if (!split[0].equals("*") && split[1].equals("*")) {
            return index.getNamespaceIndex().getOrDefault(split[0], new ArrayList<>());
        }

        return null;
    }

    public IForgeRegistry<?> getRegistry() {
        return registry;
    }

    public TOMLConfiguration getFile() {
        return file;
    }

    public boolean matches(ResourceLocation identifier) {
        return entries.contains(identifier);
    }

    public boolean isOkay(ResourceLocation identifier) {
        if (maskType == MaskType.WHITELIST) {
            return entries.contains(identifier);
        } else {
            return !entries.contains(identifier);
        }
    }
}
