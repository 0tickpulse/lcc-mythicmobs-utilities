package me.tick.lccmythicmobsutilities.components.conditions;

import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.skills.conditions.ILocationCondition;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import me.tick.lccmythicmobsutilities.models.ComponentType;

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
public class WorldGuardFlagCondition implements ILocationCondition {

    @Override
    public boolean check(AbstractLocation abstractLocation) {
        return false;
    }
}
