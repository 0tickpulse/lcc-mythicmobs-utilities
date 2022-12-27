package me.tick.lccmythicmobsutilities.components.conditions.worldguard;

import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.ILocationCondition;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import me.tick.lccmythicmobsutilities.models.ComponentType;
import me.tick.lccmythicmobsutilities.models.CustomMythicCondition;

@ComponentEntry(
        type = ComponentType.CONDITION,
        name = "worldguardflag",
        description = "Checks if a WorldGuard flag is set at a location",
        examples = {
                """
                        Conditions:
                        - worldguardflag{flag=build}"""
        }
)
public class WorldGuardFlagCondition extends CustomMythicCondition implements ILocationCondition {

    public WorldGuardFlagCondition(MythicLineConfig mlc) {
        super(mlc);
    }

    @Override
    public boolean check(AbstractLocation abstractLocation) {
        return false;
    }
}
