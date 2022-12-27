package me.tick.lccmythicmobsutilities.events;

import com.gmail.nossr50.datatypes.party.Party;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.util.player.UserManager;
import io.lumine.mythic.bukkit.events.MythicReloadedEvent;
import io.lumine.mythic.core.skills.placeholders.Placeholder;
import me.tick.lccmythicmobsutilities.modules.ComponentEntryGenerator;
import me.tick.lccmythicmobsutilities.modules.PlaceholderManager;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.MapContext;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlaceholderRegisterer implements Listener {
    @EventHandler
    public void onMythicReloaded(MythicReloadedEvent event) {
        PlaceholderManager.registerAllEntityPlaceholders("standing_on", (entity, arg) -> {
            return entity.getLocation().add(0, -1, 0).getBlock().getType().name();
        }, new ComponentEntryGenerator()
                .setName("standing_on")
                .setDescription("Returns the block type that the entity is standing on.")
                .setAuthor("0TickPulse")
                .generate());
        PlaceholderManager.registerAllPlayerOnlyEntityPlaceholders("mcmmo.party", (player, arg) -> {
            McMMOPlayer mcMMOPlayer = UserManager.getOfflinePlayer(player);
            if (mcMMOPlayer == null) {
                return "";
            }
            Party party = mcMMOPlayer.getParty();
            if (party == null) {
                return "";
            }
            return party.getName();
        }, new ComponentEntryGenerator()
                .setName("mcmmo.party")
                .setDescription("Returns the McMMO party of the entity. If the entity is not in a party or is not a player, returns an empty string.")
                .setAuthor("0TickPulse")
                .generate());
        PlaceholderManager.registerPlaceholder(new String[]{"jexl"}, Placeholder.meta((skillMetadata, arg) -> {
            JexlEngine jexl = new JexlBuilder().create();
            JexlContext context = new MapContext();
            context.set("skillMetadata", skillMetadata);
            return jexl.createExpression(arg).evaluate(context).toString();
        }), new ComponentEntryGenerator()
                .setName("java")
                .setDescription("Parses a [JEXL](https://commons.apache.org/proper/commons-jexl/) expression and returns the result.")
                .setAuthor("0TickPulse")
                .generate());
    }
}
