package me.tick.lccmythicmobsutilities.bridges;

import me.tick.lccmythicmobsutilities.LccMythicmobsUtilities;
import me.tick.lccmythicmobsutilities.models.Bridge;
import me.tick.lccmythicmobsutilities.papi.PapiMythicPlaceholder;

public class PlaceholderAPIBridge implements Bridge {
    @Override
    public boolean canEnable() {
        return LccMythicmobsUtilities.getPlugin().hasPlugin("PlaceholderAPI");
    }

    public void registerExpansions() {
        new PapiMythicPlaceholder().register();
    }

    @Override
    public void start() {
        registerExpansions();
    }
}
