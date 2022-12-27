package me.tick.lccmythicmobsutilities.components.placeholders;

import com.gmail.nossr50.datatypes.party.Party;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.util.player.UserManager;
import me.tick.lccmythicmobsutilities.modules.ComponentEntryGenerator;
import me.tick.lccmythicmobsutilities.modules.PlaceholderManager;

public class EntityPlaceholders implements Placeholders {
    @Override
    public void register() {
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

    }
}
