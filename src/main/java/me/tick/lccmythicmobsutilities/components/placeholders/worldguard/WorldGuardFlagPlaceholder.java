package me.tick.lccmythicmobsutilities.components.placeholders.worldguard;

import me.tick.lccmythicmobsutilities.bridges.WorldGuardBridge;
import me.tick.lccmythicmobsutilities.components.placeholders.Placeholders;
import me.tick.lccmythicmobsutilities.modules.ComponentEntryGenerator;
import me.tick.lccmythicmobsutilities.modules.PlaceholderUtilities;

public class WorldGuardFlagPlaceholder implements Placeholders {
    @Override
    public void register() {
        PlaceholderUtilities.registerLocationPlaceholder(new String[]{"worldguard_flag"}, (location, arg) -> {
            return WorldGuardBridge.getFlagValueString(location, WorldGuardBridge.getFlag(arg));
        }, new ComponentEntryGenerator()
                .setName("worldguard_flag.<flag>")
                .setDescription("Returns the value of the specified WorldGuard flag at the target location.")
                .setAuthor("0TickPulse")
                .generate());
        PlaceholderUtilities.registerAllEntityPlaceholders("worldguard_flag", (entity, arg) -> {
            return WorldGuardBridge.getFlagValueString(entity.getLocation(), WorldGuardBridge.getFlag(arg));
        }, new ComponentEntryGenerator()
                .setName("worldguard_flag.<flag>")
                .setDescription("Returns the value of the specified WorldGuard flag at the entity's location.")
                .setAuthor("0TickPulse")
                .generate());
    }
}
