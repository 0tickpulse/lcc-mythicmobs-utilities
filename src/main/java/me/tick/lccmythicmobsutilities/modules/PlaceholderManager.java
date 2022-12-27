package me.tick.lccmythicmobsutilities.modules;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.placeholders.Placeholder;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import me.tick.lccmythicmobsutilities.models.ComponentType;
import me.tick.lccmythicmobsutilities.models.generic.PlaceholderEntityType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * A utility class that makes it easier to create placeholders for MythicMobs.
 * The most notable method is {@link #registerAllEntityPlaceholders}, which allows you to quickly register placeholders for all placeholder entity types (caster, target, etc) without having to duplicate code.
 * Keep in mind that this also keeps a variable for each placeholder to generate documentation.
 */
public class PlaceholderManager {

    private static BiFunction<Entity, String, String> transformFunction(BiFunction<Player, String, String> function, String fallback) {
        return (entity, s) -> {
            if (entity.getType().equals(EntityType.PLAYER)) {
                return function.apply((Player) entity, s);
            }
            return fallback;
        };
    }

    public static Set<ComponentEntry> placeholderDataAnnotations = new LinkedHashSet<>();

    public static void registerPlaceholder(String[] names, Placeholder transformer, ComponentEntry componentEntry) {
        registerPlaceholder(names, transformer, componentEntry, true);
    }

    public static void registerPlaceholder(String[] names, Placeholder transformer, ComponentEntry componentEntry, boolean generateDocumentation) {
        MythicBukkit.inst().getPlaceholderManager().register(names, transformer);
        if (componentEntry == null) {
            componentEntry = new ComponentEntryGenerator().setName(names[0]).generate();
        }
        if (generateDocumentation) {
            placeholderDataAnnotations.add(new ComponentEntryGenerator(componentEntry).setType(ComponentType.PLACEHOLDER).setAliases(Arrays.copyOfRange(names, 1, names.length)).generate());
        }
    }

    public static void registerEntityPlaceholder(PlaceholderEntityType entityType, String name, BiFunction<Entity, String, String> transformer, ComponentEntry componentEntry) {
        registerEntityPlaceholder(entityType, name, transformer, componentEntry, true);
    }


    public static void registerEntityPlaceholder(PlaceholderEntityType entityType, String name, BiFunction<Entity, String, String> transformer, ComponentEntry componentEntry, boolean generateDocumentation) {
        String newName = entityType.name().toLowerCase() + "." + name;
        switch (entityType) {
            case CASTER ->
                    registerPlaceholder(new String[]{newName}, Placeholder.meta((meta, arg) -> transformer.apply(meta.getCaster().getEntity().getBukkitEntity(), arg)), componentEntry, generateDocumentation);
            case TARGET ->
                    registerPlaceholder(new String[]{newName}, Placeholder.entity((entity, arg) -> transformer.apply(entity.getBukkitEntity(), arg)), componentEntry, generateDocumentation);
            case TRIGGER ->
                    registerPlaceholder(new String[]{newName}, Placeholder.meta((meta, arg) -> transformer.apply(meta.getTrigger().getBukkitEntity(), arg)), componentEntry, generateDocumentation);
            case PARENT ->
                    registerPlaceholder(new String[]{newName}, Placeholder.parent((entity, arg) -> transformer.apply(entity.getBukkitEntity(), arg)), componentEntry, generateDocumentation);
        }
    }

    public static void registerPlayerOnlyEntityPlaceholder(PlaceholderEntityType entityType, String name, BiFunction<Player, String, String> transformer, ComponentEntry componentEntry) {
        registerPlayerOnlyEntityPlaceholder(entityType, name, transformer, componentEntry, "");
    }

    public static void registerPlayerOnlyEntityPlaceholder(PlaceholderEntityType entityType, String name, BiFunction<Player, String, String> transformer, ComponentEntry componentEntry, String fallback) {
        registerEntityPlaceholder(entityType, name, transformFunction(transformer, fallback), componentEntry);
    }

    public static void registerAllEntityPlaceholders(String name, BiFunction<Entity, String, String> transformer, ComponentEntry componentEntry) {
        for (PlaceholderEntityType entityType : PlaceholderEntityType.values()) {
            registerEntityPlaceholder(entityType, name, transformer, null, false);
        }
        String newName = "[" + Arrays.stream(PlaceholderEntityType.values()).map(type -> type.name().toLowerCase()).reduce((a, b) -> a + "|" + b).orElse("") + "]." + name;
        placeholderDataAnnotations.add(new ComponentEntryGenerator(componentEntry).setName(newName).generate());
    }

    public static void registerAllPlayerOnlyEntityPlaceholders(String name, BiFunction<Player, String, String> transformer, ComponentEntry componentEntry) {
        registerAllPlayerOnlyEntityPlaceholders(name, transformer, componentEntry, "");
    }

    public static void registerAllPlayerOnlyEntityPlaceholders(String name, BiFunction<Player, String, String> transformer, ComponentEntry componentEntry, String fallback) {
        registerAllEntityPlaceholders(name, transformFunction(transformer, fallback), componentEntry);
    }
}
