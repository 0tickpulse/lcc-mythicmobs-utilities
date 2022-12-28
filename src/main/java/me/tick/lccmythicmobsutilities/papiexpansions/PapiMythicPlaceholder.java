package me.tick.lccmythicmobsutilities.papiexpansions;

import io.lumine.mythic.api.skills.placeholders.PlaceholderString;
import io.lumine.mythic.bukkit.BukkitAdapter;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.tick.lccmythicmobsutilities.LccMythicmobsUtilities;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class PapiMythicPlaceholder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "mythic";
    }

    @Override
    public @NotNull String getAuthor() {
        return "0TickPulse";
    }

    @Override
    public @NotNull String getVersion() {
        return LccMythicmobsUtilities.getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        PlaceholderString placeholderString = PlaceholderString.of(params);
        return placeholderString.get(BukkitAdapter.adapt((Entity) player));
    }
}
