package me.tick.lccmythicmobsutilities.components.conditions;

import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.skills.conditions.ILocationCondition;
import io.lumine.mythic.core.utils.annotations.MythicCondition;
import me.tick.lccmythicmobsutilities.models.ConditionEntry;

@ConditionEntry(
        data = @MythicCondition(
                name = "worldguardflag",
                description = "Checks if a WorldGuard flag is set at a location"
        ),
        examples = {
                "Conditions: \n- worldguardflag{flag=build}"
        }
)
public class WorldGuardFlagCondition implements ILocationCondition {

    @Override
    public boolean check(AbstractLocation abstractLocation) {
        return false;
    }
}
