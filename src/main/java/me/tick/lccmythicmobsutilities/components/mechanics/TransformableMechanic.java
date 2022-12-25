package me.tick.lccmythicmobsutilities.components.mechanics;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.ITargetedLocationSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;
import me.tick.lccmythicmobsutilities.modules.LocationUtil;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.List;

/**
 * A class that represents a mechanic that does certain operations to certain points in the world.
 * It adds a variety of fields in mechanics that are able to transform these points, allowing for greater freedom for server developers using mechanics:
 * <ul>
 *     <li>{@link TransformableMechanic#xOffset} Additional offset in the X-axis.</li>
 *     <li>{@link TransformableMechanic#yOffset} Additional offset in the Y-axis.</li>
 *     <li>{@link TransformableMechanic#zOffset} Additional offset in the Z-axis.</li>
 *     <li>{@link TransformableMechanic#forwardOffset} Additional forward offset. This is based on the caster's yaw and pitch.</li>
 *     <li>{@link TransformableMechanic#rightOffset} Additional right offset. This is based on the caster's yaw and pitch. This is equivalent to <code>forwardOffset</code> but with pitch set to 0 and yaw rotated by -90.</li>
 *     <li>{@link TransformableMechanic#upOffset} Additional up offset. This is based on the caster's yaw and pitch. This is equivalent to <code>forwardOffset</code> but with pitch rotated by +90.</li>
 *     <li>{@link TransformableMechanic#size} This mechanic takes the points and calculates a center point. Then, for each of the points, it calculates a vector from the center to that point. The size field simply multiplies this vector.</li>
 * </ul>
 * To use this mechanic, simply extend it in your own mechanic class and do the following:
 * <ol>
 *     <li>Override the {@link TransformableMechanic#getPoints} method. This method is called when the skill is being cast, and is expected to return a list of points that are going to be transformed.</li>
 *     <li>Override the {@link TransformableMechanic#cast} method. This method should contain what the skill actually executes. This method will be called with the skill metadata and transformed points as a parameter.</li>
 * </ol>
 *
 * @author 0TickPulse
 * @see LocationUtil
 * @see SkillMechanic
 */
public abstract class TransformableMechanic extends SkillMechanic implements ITargetedLocationSkill, ITargetedEntitySkill {
    private final double xOffset;
    private final double yOffset;
    private final double zOffset;
    private final double forwardOffset;
    private final double rightOffset;
    private final double upOffset;
    private final double size;

    public TransformableMechanic(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
        super(manager, file, line, mlc);
        this.xOffset = mlc.getDouble(new String[]{"xoffset", "xo"}, 0);
        this.yOffset = mlc.getDouble(new String[]{"yoffset", "yo"}, 0);
        this.zOffset = mlc.getDouble(new String[]{"zoffset", "zo"}, 0);
        this.forwardOffset = mlc.getDouble(new String[]{"forwardoffset", "fo"}, 0);
        this.rightOffset = mlc.getDouble(new String[]{"rightoffset", "ro"}, 0);
        this.upOffset = mlc.getDouble(new String[]{"upoffset", "uo"}, 0);
        this.size = mlc.getDouble(new String[]{"size", "s"}, 1);
    }

    public List<Location> transform(SkillMetadata skillMetadata, List<Location> locations) {
        double averageX = locations.stream().map(Location::getX).reduce(0.0, Double::sum) / locations.size();
        double averageY = locations.stream().map(Location::getY).reduce(0.0, Double::sum) / locations.size();
        double averageZ = locations.stream().map(Location::getZ).reduce(0.0, Double::sum) / locations.size();
        float averageYaw = locations.stream().map(Location::getYaw).reduce(0.0F, Float::sum) / locations.size();
        float averagePitch = locations.stream().map(Location::getPitch).reduce(0.0F, Float::sum) / locations.size();
        Location center = new Location(locations.get(0).getWorld(), averageX, averageY, averageZ, averageYaw, averagePitch);
        return locations.stream().map(location -> {
            location.add(xOffset, yOffset, zOffset);
            location = LocationUtil.relativeOffset(
                    location.clone().setDirection(
                            BukkitAdapter.adapt(skillMetadata.getCaster().getLocation().getDirection())
                    ),
                    forwardOffset,
                    rightOffset,
                    upOffset
            );
            // size transformation
            Vector vector = location.clone().subtract(center).toVector();
            return center.clone().add(vector.multiply(size));
        }).toList();
    }

    public abstract List<Location> getPoints(SkillMetadata data, Location target);

    public abstract SkillResult cast(SkillMetadata data, List<Location> locations);

    @Override
    public SkillResult castAtLocation(SkillMetadata skillMetadata, AbstractLocation location) {
        return this.cast(skillMetadata, this.transform(skillMetadata, this.getPoints(skillMetadata, BukkitAdapter.adapt(location))));
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity entity) {
        return this.cast(skillMetadata, this.transform(skillMetadata, this.getPoints(skillMetadata, BukkitAdapter.adapt(entity.getLocation()))));
    }
}
