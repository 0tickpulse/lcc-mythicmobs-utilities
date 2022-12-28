package me.tick.lccmythicmobsutilities.modules;

import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.placeholders.Placeholder;
import io.lumine.mythic.core.skills.variables.VariableScope;
import me.tick.lccmythicmobsutilities.LccMythicmobsUtilities;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import me.tick.lccmythicmobsutilities.models.ComponentType;
import me.tick.lccmythicmobsutilities.models.generic.PlaceholderEntityType;
import me.tick.lccmythicmobsutilities.models.generic.PlaceholderType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A utility class that makes it easier to create placeholders for MythicMobs.
 * The most notable method is {@link #registerAllEntityPlaceholders}, which allows you to quickly register placeholders for all placeholder entity types (caster, target, etc) without having to duplicate code.
 * Keep in mind that this also keeps a variable for each placeholder to generate documentation.
 *
 * @author 0TickPulse
 */
public class PlaceholderUtilities {

    private static BiFunction<Entity, String, String> transformFunction(BiFunction<Player, String, String> function, String fallback) {
        return (entity, s) -> {
            if (entity.getType().equals(EntityType.PLAYER)) {
                return function.apply((Player) entity, s);
            }
            return fallback;
        };
    }

    public static Set<ComponentEntry> placeholderDataAnnotations = new LinkedHashSet<>();

    /**
     * Runs {@link #registerPlaceholder(String[], Placeholder, ComponentEntry, boolean)} but with {@code generateDocumentation} set to {@code true}.
     */
    public static void registerPlaceholder(String[] names, Placeholder transformer, ComponentEntry componentEntry) {
        registerPlaceholder(names, transformer, componentEntry, true);
    }

    public static void registerLocationPlaceholder(String[] names, BiFunction<Location, String, String> transformer, ComponentEntry componentEntry) {
        registerLocationPlaceholder(names, transformer, componentEntry, true);
    }

    /**
     * Registers a placeholder for locations. This will automatically add the <code>target.</code> prefix to the placeholder name.
     */
    public static void registerLocationPlaceholder(String[] names, BiFunction<Location, String, String> transformer, ComponentEntry componentEntry, boolean generateDocumentation) {
        String[] newNames = Arrays.stream(names).map(s -> "target." + s).toArray(String[]::new);
        registerPlaceholder(
                newNames,
                Placeholder.location((abstractLocation, arg) -> transformer.apply(BukkitAdapter.adapt(abstractLocation), arg)),
                new ComponentEntryGenerator(componentEntry).transformNames((string) -> "target." + string).generate(),
                generateDocumentation);
    }

    /**
     * Registers a placeholder.
     *
     * @param names                 The names of the placeholder.
     * @param transformer           The transformer function. Most of the time, the object for this parameter can be constructed from {@link Placeholder}'s methods.
     * @param componentEntry        The component entry for the placeholder. This will be used to generate documentation.
     * @param generateDocumentation Whether to generate documentation for this placeholder. If set to false, the {@code componentEntry} param will be ignored.
     */
    public static void registerPlaceholder(String[] names, Placeholder transformer, ComponentEntry componentEntry, boolean generateDocumentation) {
        MythicBukkit.inst().getPlaceholderManager().register(names, transformer);
        if (componentEntry == null) {
            componentEntry = new ComponentEntryGenerator().setName(names[0]).generate();
        }
        if (generateDocumentation) {
            placeholderDataAnnotations.add(
                    new ComponentEntryGenerator(componentEntry)
                            .setType(ComponentType.PLACEHOLDER)
                            .setAliases(Arrays.copyOfRange(names, 1, names.length))
                            .generate()
            );
        }
        LccMythicmobsUtilities.devLog("Registering placeholder" + (generateDocumentation ? " and documentation" : "") + ": " + names[0]);
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
        String typesString = "[" + Arrays.stream(PlaceholderEntityType.values()).map(type -> type.name().toLowerCase()).reduce((a, b) -> a + "|" + b).orElse("") + "].";
        placeholderDataAnnotations.add(new ComponentEntryGenerator(componentEntry).transformNames((string) -> typesString + string).generate());
    }

    public static void registerAllPlayerOnlyEntityPlaceholders(String name, BiFunction<Player, String, String> transformer, ComponentEntry componentEntry) {
        registerAllPlayerOnlyEntityPlaceholders(name, transformer, componentEntry, "");
    }

    public static void registerAllPlayerOnlyEntityPlaceholders(String name, BiFunction<Player, String, String> transformer, ComponentEntry componentEntry, String fallback) {
        registerAllEntityPlaceholders(name, transformFunction(transformer, fallback), componentEntry);
    }

    public static void registerVariableScopePlaceholder(VariableScope scope, String name, Placeholder transformer, ComponentEntry componentEntry) {
        registerVariableScopePlaceholder(scope, name, transformer, componentEntry, true);
    }

    public static void registerVariableScopePlaceholder(VariableScope scope, String name, Placeholder transformer, ComponentEntry componentEntry, boolean generateDocumentation) {
        registerPlaceholder(new String[]{switch (scope) {
            case WORLD -> "world.";
            case GLOBAL -> "global.";
            case SKILL -> "skill.";
            case TARGET -> "target.";
            case CASTER -> "caster.";
        } + name}, transformer, componentEntry, generateDocumentation);
    }

    public static void registerAllVariableScopePlaceholders(String name, Function<VariableScope, Placeholder> transformer, ComponentEntry componentEntry) {
        for (VariableScope scope : VariableScope.values()) {
            registerVariableScopePlaceholder(scope, name, transformer.apply(scope), null, false);
        }
        String newName = "[" + Arrays.stream(VariableScope.values()).map(type -> type.name().toLowerCase()).reduce((a, b) -> a + "|" + b).orElse("") + "]." + name;
        placeholderDataAnnotations.add(new ComponentEntryGenerator(componentEntry).setName(newName).generate());
    }

    public static void registerTypePlaceholder(PlaceholderType type, String name, Placeholder transformer, ComponentEntry componentEntry) {
        registerTypePlaceholder(type, name, transformer, componentEntry, true);
    }

    public static void registerTypePlaceholder(PlaceholderType type, String name, Placeholder transformer, ComponentEntry componentEntry, boolean generateDocumentation) {
        registerPlaceholder(new String[]{switch (type) {
            case SKILL -> "skill.";
            case WORLD -> "world.";
            case GLOBAL -> "global.";
            case TARGET -> "target.";
            case CASTER -> "caster.";
            case PARENT -> "parent.";
            case TRIGGER -> "trigger.";
        } + name}, transformer, componentEntry, generateDocumentation);
    }

    public static void registerAllTypePlaceholders(String name, Function<PlaceholderType, Placeholder> transformer, ComponentEntry componentEntry) {
        for (PlaceholderType type : PlaceholderType.values()) {
            registerTypePlaceholder(type, name, transformer.apply(type), null, false);
        }
        String newName = "[" + Arrays.stream(PlaceholderType.values()).map(type -> type.name().toLowerCase()).reduce((a, b) -> a + "|" + b).orElse("") + "]." + name;
        placeholderDataAnnotations.add(new ComponentEntryGenerator(componentEntry).setName(newName).generate());
    }
}
