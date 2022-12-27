package me.tick.lccmythicmobsutilities.events;

import io.lumine.mythic.bukkit.events.MythicReloadedEvent;
import me.tick.lccmythicmobsutilities.modules.PlaceholderManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlaceholderRegisterer implements Listener {
    @EventHandler
    public void onMythicReloaded(MythicReloadedEvent event) {
        PlaceholderManager.registerAllEntityPlaceholders("standing_on", (entity, arg) -> {
            return entity.getLocation().add(0, -1, 0).getBlock().getType().name();
        });
    }
}
