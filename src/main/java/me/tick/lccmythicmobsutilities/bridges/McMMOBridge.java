package me.tick.lccmythicmobsutilities.bridges;

import me.tick.lccmythicmobsutilities.LccMythicmobsUtilities;
import me.tick.lccmythicmobsutilities.components.conditions.mcmmo.McMMOSamePartyCondition;
import me.tick.lccmythicmobsutilities.components.placeholders.mcmmo.McMMOPartyPlaceholder;
import me.tick.lccmythicmobsutilities.events.ConditionsEvent;
import me.tick.lccmythicmobsutilities.events.PlaceholderRegisterer;
import me.tick.lccmythicmobsutilities.models.Bridge;

public class McMMOBridge implements Bridge {
    @Override
    public boolean canEnable() {
        return LccMythicmobsUtilities.getPlugin().hasPlugin("mcMMO");
    }

    @Override
    public void start() {
        ConditionsEvent.conditions.add(McMMOSamePartyCondition.class);
        PlaceholderRegisterer.placeholders.add(new McMMOPartyPlaceholder());
    }
}
