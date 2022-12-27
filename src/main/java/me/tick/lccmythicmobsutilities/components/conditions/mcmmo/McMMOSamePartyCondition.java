package me.tick.lccmythicmobsutilities.components.conditions.mcmmo;

import com.gmail.nossr50.datatypes.party.Party;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.util.player.UserManager;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.conditions.ISkillMetaCondition;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import me.tick.lccmythicmobsutilities.models.ComponentType;
import me.tick.lccmythicmobsutilities.models.CustomMythicCondition;
import org.bukkit.OfflinePlayer;

import java.util.Collection;
import java.util.function.Function;

@ComponentEntry(
        type = ComponentType.CONDITION,
        name = "sameparty",
        aliases = {"mcmmosameparty"},
        description = "Checks if the target is in the same party as the caster. Only works for players.",
        examples = {
                """
                        Conditions:
                        - sameparty"""
        },
        author = "0TickPulse"
)
public class McMMOSamePartyCondition extends CustomMythicCondition implements ISkillMetaCondition {
    public McMMOSamePartyCondition(MythicLineConfig mlc) {
        super(mlc);
    }

    public static boolean matchesParty(OfflinePlayer player1, OfflinePlayer player2) {
        McMMOPlayer mcMMOPlayer1 = UserManager.getOfflinePlayer(player1);
        McMMOPlayer mcMMOPlayer2 = UserManager.getOfflinePlayer(player2);
        if (mcMMOPlayer1 == null || mcMMOPlayer2 == null) {
            return false;
        }
        Party party1 = mcMMOPlayer1.getParty();
        Party party2 = mcMMOPlayer2.getParty();
        if (party1 == null || party2 == null) {
            return false;
        }
        return party1.equals(party2);
    }

    @Override
    public boolean check(SkillMetadata skillMetadata) {
        if (!skillMetadata.getCaster().getEntity().isPlayer()) {
            return false;
        }
        Collection<AbstractEntity> targets = skillMetadata.getEntityTargets();
        if (targets.size() < 1) {
            return false;
        }
        for (AbstractEntity target : targets) {
            if (!target.isPlayer()) {
                return false;
            }
            if (!matchesParty((OfflinePlayer) skillMetadata.getCaster().getEntity().getBukkitEntity(), (OfflinePlayer) target.getBukkitEntity())) {
                return false;
            }
        }
        return true;
    }
}
