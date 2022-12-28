package me.tick.lccmythicmobsutilities.events;

import io.lumine.mythic.bukkit.events.MythicReloadedEvent;
import me.tick.lccmythicmobsutilities.components.placeholders.EntityPlaceholders;
import me.tick.lccmythicmobsutilities.components.placeholders.MetaPlaceholders;
import me.tick.lccmythicmobsutilities.components.placeholders.Placeholders;
import me.tick.lccmythicmobsutilities.modules.PlaceholderUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.LinkedHashSet;
import java.util.Set;

public class PlaceholderRegisterer implements Listener {

    public static Set<Placeholders> placeholders = new LinkedHashSet<>();
    static {
        placeholders.add(new EntityPlaceholders());
        placeholders.add(new MetaPlaceholders());
    }

    @EventHandler
    public void onMythicReloaded(MythicReloadedEvent event) {
        register();
    }

    public static void register() {
        PlaceholderUtilities.placeholderDataAnnotations = new LinkedHashSet<>();
        for (Placeholders placeholder : placeholders) {
            placeholder.register();
        }
    }
}
