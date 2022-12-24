package me.tick.lccmythicmobsutilities.modules;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.toRadians;

public class Slash {
    public static Set<Entity> getEntitiesInPoints(Location casterLocation, Set<Location> locations) {
        Set<Entity> entities = new HashSet<>();
        for (Location location : locations) {
            Set<Location> pointsBetween = LocationUtil.getPointsBetween(location, casterLocation, 0.2);
            for (Location point : pointsBetween) {
                entities.addAll(point.getWorld().getNearbyEntities(point, 0.2, 0.2, 0.2));
            }
        }
        return entities;
    }
    public static List<Location> getSlashLocations(Location origin, double radius, double rotation, int points, double arc) throws IllegalArgumentException {

        if (points < 0) {
            throw new IllegalArgumentException("Points cannot be negative");
        }
        if (arc < 0) {
            throw new IllegalArgumentException("Arc cannot be negative");
        }
        if (arc > 360) {
            throw new IllegalArgumentException("Arc cannot be greater than 360");
        }
        if (rotation < 0) {
            rotation += 180;
        }

        List<Location> locations = new ArrayList<>();

        double i = 90 + (arc / -2);

        while (i <= 90 + (arc / 2)) {
            double relativeHorizontalOffset = radius * sin(toRadians((arc / 2) - i));
            double horizontalOffset = relativeHorizontalOffset * cos(toRadians(rotation));
            double forwardOffset = radius * cos(toRadians(((arc / 2) - i)));
            double verticalOffset = sin(toRadians(rotation)) * relativeHorizontalOffset;
            locations.add(LocationUtil.relativeOffset(origin, forwardOffset, horizontalOffset, verticalOffset));
            i += arc / points;
        }

        return locations;
    }
}
