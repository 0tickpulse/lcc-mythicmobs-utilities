package me.tick.lccmythicmobsutilities.components.mechanics;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.skills.SkillExecutor;
import me.tick.lccmythicmobsutilities.LccMythicmobsUtilities;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import me.tick.lccmythicmobsutilities.models.ComponentType;
import me.tick.lccmythicmobsutilities.models.generic.GenericMechanic;
import me.tick.lccmythicmobsutilities.models.generic.Targeted;
import me.tick.lccmythicmobsutilities.models.generic.fields.FieldAliases;
import me.tick.lccmythicmobsutilities.models.generic.fields.FieldDefaultString;
import me.tick.lccmythicmobsutilities.models.generic.fields.FieldDescription;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.io.File;

@ComponentEntry(
        type = ComponentType.MECHANIC,
        name = "testmsg",
        description = "test"
)
public class MessageMechanic extends GenericMechanic {

    public MessageMechanic(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
        super(manager, file, line, mlc);
    }

    public SkillResult smartExecute(
            SkillMetadata skillMetadata,
            Targeted targeted,
            @FieldAliases({"msg"}) @FieldDescription("test") @FieldDefaultString("lol") String message
    ) {
        Entity entity = BukkitAdapter.adapt(skillMetadata.getCaster().getEntity());
        LccMythicmobsUtilities.devLog("TEST: " + message);
        entity.sendMessage(message);
        entity.setVelocity(new Vector(0, 1, 0));
        return SkillResult.SUCCESS;
    }
}
