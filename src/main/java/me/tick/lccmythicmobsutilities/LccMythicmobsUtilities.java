package me.tick.lccmythicmobsutilities;

import me.tick.lccmythicmobsutilities.commands.LccmmCommand;
import me.tick.lccmythicmobsutilities.events.ConditionsEvent;
import me.tick.lccmythicmobsutilities.events.MechanicsEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class LccMythicmobsUtilities extends JavaPlugin {

    public static Plugin getPlugin() {
        return getPlugin(LccMythicmobsUtilities.class);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("LccMythicmobsUtilities has been enabled!");
        Objects.requireNonNull(getCommand("lccmm")).setExecutor(new LccmmCommand());
        registerEvents();
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new ConditionsEvent(), this);
        getServer().getPluginManager().registerEvents(new MechanicsEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
