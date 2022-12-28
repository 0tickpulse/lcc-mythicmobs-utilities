package me.tick.lccmythicmobsutilities.components.placeholders;

import me.tick.lccmythicmobsutilities.modules.ComponentEntryGenerator;
import me.tick.lccmythicmobsutilities.modules.PlaceholderUtilities;

public class EntityPlaceholders implements Placeholders {
    @Override
    public void register() {
        PlaceholderUtilities.registerAllEntityPlaceholders(new String[]{"standing_on"}, (entity, arg) -> {
            return entity.getLocation().add(0, -1, 0).getBlock().getType().name();
        }, new ComponentEntryGenerator()
                .setName("standing_on")
                .setDescription("Returns the block type that the entity is standing on.")
                .setAuthor("0TickPulse")
                .generate());
    }
}
