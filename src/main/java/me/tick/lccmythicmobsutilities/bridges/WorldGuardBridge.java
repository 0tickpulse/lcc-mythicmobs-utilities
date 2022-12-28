package me.tick.lccmythicmobsutilities.bridges;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.tick.lccmythicmobsutilities.LccMythicmobsUtilities;
import me.tick.lccmythicmobsutilities.components.placeholders.worldguard.WorldGuardFlagPlaceholder;
import me.tick.lccmythicmobsutilities.events.PlaceholderRegisterer;
import me.tick.lccmythicmobsutilities.models.Bridge;
import org.bukkit.Location;

import java.util.Set;

public class WorldGuardBridge implements Bridge {
    @Override
    public boolean canEnable() {
        return LccMythicmobsUtilities.getPlugin().hasPlugin("WorldGuard");
    }

    @Override
    public void start() {
        PlaceholderRegisterer.placeholders.add(new WorldGuardFlagPlaceholder());
    }

    public static Set<ProtectedRegion> getRegions(Location location) {
        ApplicableRegionSet regions = getApplicableRegions(location);
        return regions != null ? regions.getRegions() : null;
    }

    public static ApplicableRegionSet getApplicableRegions(Location location) {
        RegionManager manager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(location.getWorld()));
        if (manager != null) {
            return manager.getApplicableRegions(BukkitAdapter.asBlockVector(location));
        }
        return null;
    }

    public static Flag<?> getFlag(String flag) {
        return WorldGuard.getInstance().getFlagRegistry().get(flag);
    }

    public static Object getFlagValue(Location location, Flag<?> flag) {
        ApplicableRegionSet regions = getApplicableRegions(location);
        if (regions == null || regions.size() == 0 || flag == null) {
            return null;
        }
        for (ProtectedRegion region : regions) {
            Object value = region.getFlag(flag);
            if (value != null) {
                return value.toString();
            }
        }
        return flag.getDefault();
    }

    /**
     * @implNote Will return "null" instead of null.
     */
    public static String getFlagValueString(Location location, Flag<?> flag) {
        Object value = getFlagValue(location, flag);
        return value != null ? value.toString() : "null";
    }
}
