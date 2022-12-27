package me.tick.lccmythicmobsutilities;

import me.tick.lccmythicmobsutilities.commands.LccmmCommand;
import me.tick.lccmythicmobsutilities.events.ConditionsEvent;
import me.tick.lccmythicmobsutilities.events.MechanicsEvent;
import me.tick.lccmythicmobsutilities.events.PlaceholderRegisterer;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class LccMythicmobsUtilities extends JavaPlugin {

    static final boolean debug = true;

    public static LccMythicmobsUtilities getPlugin() {
        return getPlugin(LccMythicmobsUtilities.class);
    }

    public static void devLog(String message) {
        if (debug) {
            getPlugin().getLogger().log(Level.INFO, message);
        }
    }

    public static void error(Throwable message) {
        getPlugin().getLogger().log(Level.SEVERE, message.toString());
    }

    public static void error(String message) {
        getPlugin().getLogger().log(Level.SEVERE, message);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("LccMythicmobsUtilities has been enabled!");
        Objects.requireNonNull(getCommand("lccmm")).setExecutor(new LccmmCommand());
        registerEvents();
    }

    public static Set<ComponentEntry> getComponentAnnotations() {
        Set<ComponentEntry> entries = ConditionsEvent.conditions.stream().map(clazz -> clazz.getAnnotation(ComponentEntry.class)).collect(Collectors.toSet());
        entries.addAll(MechanicsEvent.legacyMechanics.stream().map(clazz -> clazz.getAnnotation(ComponentEntry.class)).collect(Collectors.toSet()));
        return entries;
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new ConditionsEvent(), this);
        getServer().getPluginManager().registerEvents(new MechanicsEvent(), this);
        getServer().getPluginManager().registerEvents(new PlaceholderRegisterer(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
