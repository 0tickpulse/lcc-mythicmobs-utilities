package me.tick.lccmythicmobsutilities;

import me.tick.lccmythicmobsutilities.bridges.McMMOBridge;
import me.tick.lccmythicmobsutilities.commands.LccmmCommand;
import me.tick.lccmythicmobsutilities.events.ConditionsEvent;
import me.tick.lccmythicmobsutilities.events.MechanicsEvent;
import me.tick.lccmythicmobsutilities.events.PlaceholderRegisterer;
import me.tick.lccmythicmobsutilities.models.Bridge;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import me.tick.lccmythicmobsutilities.modules.PlaceholderManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
        registerEvents();
        registerCommands();
        registerBridges();
        PlaceholderRegisterer.register();
    }

    public boolean hasPlugin(String pluginName) {
        return getServer().getPluginManager().getPlugin(pluginName) != null;
    }

    public static Set<ComponentEntry> getComponentAnnotations() {
        Set<ComponentEntry> entries = ConditionsEvent.conditions.stream().map(clazz -> clazz.getAnnotation(ComponentEntry.class)).collect(Collectors.toSet());
        entries.addAll(MechanicsEvent.legacyMechanics.stream().map(clazz -> clazz.getAnnotation(ComponentEntry.class)).collect(Collectors.toSet()));
        entries.addAll(PlaceholderManager.placeholderDataAnnotations);
        return entries;
    }

    public void registerEvents() {
        registerEvents(
                new ConditionsEvent(),
                new MechanicsEvent(),
                new PlaceholderRegisterer()
        );
    }

    public void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    public void registerCommands() {
        registerCommandWithTabComplete("lccmm", new LccmmCommand());
    }

    public void registerCommand(String name, CommandExecutor executor) {
        registerCommand(name, executor, null);
    }

    public <T extends CommandExecutor & TabCompleter> void registerCommandWithTabComplete(String name, T executor) {
        registerCommand(name, executor, executor);
    }

    public void registerCommand(String name, CommandExecutor executor, TabCompleter tabCompleter) {
        PluginCommand cmd = getCommand(name);
        if (cmd == null) {
            return;
        }
        cmd.setExecutor(executor);
        if (tabCompleter != null) {
            cmd.setTabCompleter(tabCompleter);
        }
    }

    public void registerBridges() {
        registerBridge(new McMMOBridge());
    }

    public void registerBridge(Bridge bridge) {
        if (bridge.canEnable()) {
            bridge.start();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
