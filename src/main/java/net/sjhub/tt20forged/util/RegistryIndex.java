package net.sjhub.tt20forged.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegistryIndex {

    private static final HashMap<IForgeRegistry<?>, RegistryIndex> indexes = new HashMap<>();

    private final IForgeRegistry<?> registry;
    private final List<ResourceLocation> identifiers;
    private final List<String> namespaces;
    private final List<String> paths;
    private final HashMap<String, List<ResourceLocation>> namespaceIndex;
    private final HashMap<String, List<ResourceLocation>> pathIndex;

    private RegistryIndex(IForgeRegistry<?> registry) {
        this.registry = registry;

        this.identifiers = new ArrayList<>();
        this.namespaces = new ArrayList<>();
        this.paths = new ArrayList<>();
        this.namespaceIndex = new HashMap<>();
        this.pathIndex = new HashMap<>();

        for (ResourceLocation key : registry.getKeys()) {
            String namespace = key.getNamespace();
            String path = key.getPath();

            if (!namespaces.contains(namespace)) {
                namespaces.add(namespace);
                namespaceIndex.put(namespace, new ArrayList<>());
            }

            if (!paths.contains(path)) {
                paths.add(path);
                pathIndex.put(path, new ArrayList<>());
            }

            this.identifiers.add(key);
            namespaceIndex.get(namespace).add(key);
            pathIndex.get(path).add(key);
        }
    }

    public IForgeRegistry<?> getRegistry() {
        return registry;
    }

    public List<ResourceLocation> getIdentifiers() {
        return identifiers;
    }

    public HashMap<String, List<ResourceLocation>> getNamespaceIndex() {
        return new HashMap<>(namespaceIndex);
    }

    public HashMap<String, List<ResourceLocation>> getPathIndex() {
        return new HashMap<>(pathIndex);
    }

    public List<String> getNamespaces() {
        return new ArrayList<>(namespaces);
    }

    public List<String> getPaths() {
        return new ArrayList<>(paths);
    }

    public static RegistryIndex getIndex(IForgeRegistry<?> registry) {
        if (indexes.containsKey(registry)) {
            return indexes.get(registry);
        }

        indexes.put(registry, new RegistryIndex(registry));
        return indexes.get(registry);
    }
}
