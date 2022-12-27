package me.tick.lccmythicmobsutilities.modules;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.placeholders.Placeholder;
import me.tick.lccmythicmobsutilities.models.generic.PlaceholderEntityType;
import org.bukkit.entity.Entity;

import java.util.function.BiFunction;

/**
 * A utility class that makes it easier to create placeholders for MythicMobs.
 * The most notable method is {@link #registerAllEntityPlaceholders}, which allows you to quickly register placeholders for all placeholder entity types (caster, target, etc) without having to duplicate code.
 */
public class PlaceholderManager {

    public static void registerPlaceholder(String name, Placeholder transformer) {
        MythicBukkit.inst().getPlaceholderManager().register(name, transformer);
    }

    public static void registerPlaceholder(String[] names, Placeholder transformer) {
        MythicBukkit.inst().getPlaceholderManager().register(names, transformer);
    }

    public static void registerEntityPlaceholder(PlaceholderEntityType entityType, String name, BiFunction<Entity, String, String> transformer) {
        String newName = entityType.name().toLowerCase() + "." + name;
        switch (entityType) {
            case CASTER -> registerPlaceholder(newName, Placeholder.meta((meta, arg) -> transformer.apply(BukkitAdapter.adapt(meta.getCaster().getEntity()), arg)));
            case TARGET -> registerPlaceholder(newName, Placeholder.entity((entity, arg) -> transformer.apply(BukkitAdapter.adapt(entity), arg)));
            case TRIGGER -> registerPlaceholder(newName, Placeholder.meta((meta, arg) -> transformer.apply(BukkitAdapter.adapt(meta.getTrigger()), arg)));
            case PARENT -> registerPlaceholder(newName, Placeholder.parent((entity, arg) -> transformer.apply(BukkitAdapter.adapt(entity), arg)));
        }
    }

    public static void registerAllEntityPlaceholders(String name, BiFunction<Entity, String, String> transformer) {
        for (PlaceholderEntityType entityType : PlaceholderEntityType.values()) {
            registerEntityPlaceholder(entityType, name, transformer);
        }
    }
}
