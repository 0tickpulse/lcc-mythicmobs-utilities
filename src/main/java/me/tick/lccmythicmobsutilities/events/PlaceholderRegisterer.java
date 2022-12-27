package me.tick.lccmythicmobsutilities.events;

import io.lumine.mythic.bukkit.events.MythicReloadedEvent;
import me.tick.lccmythicmobsutilities.components.placeholders.EntityPlaceholders;
import me.tick.lccmythicmobsutilities.components.placeholders.MetaPlaceholders;
import me.tick.lccmythicmobsutilities.modules.PlaceholderManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.LinkedHashSet;

public class PlaceholderRegisterer implements Listener {
    @EventHandler
    public void onMythicReloaded(MythicReloadedEvent event) {
        PlaceholderManager.placeholderDataAnnotations = new LinkedHashSet<>();
        new EntityPlaceholders().register();
        new MetaPlaceholders().register();
    }
}
