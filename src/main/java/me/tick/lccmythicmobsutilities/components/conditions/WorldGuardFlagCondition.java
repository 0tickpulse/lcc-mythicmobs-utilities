package me.tick.lccmythicmobsutilities.components.conditions;

import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.skills.conditions.ILocationCondition;
import io.lumine.mythic.core.skills.SkillCondition;
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
public class WorldGuardFlagCondition extends SkillCondition implements ILocationCondition {

    public WorldGuardFlagCondition(String line) {
        super(line);
    }

    @Override
    public boolean check(AbstractLocation abstractLocation) {
        return false;
    }
}
