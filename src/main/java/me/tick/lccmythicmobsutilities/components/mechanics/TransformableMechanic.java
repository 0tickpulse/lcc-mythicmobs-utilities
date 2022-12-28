package me.tick.lccmythicmobsutilities.components.mechanics;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.ITargetedLocationSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;
import io.lumine.mythic.core.utils.annotations.MythicField;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import me.tick.lccmythicmobsutilities.models.ComponentType;
import me.tick.lccmythicmobsutilities.models.CustomMythicMechanic;
import me.tick.lccmythicmobsutilities.modules.LocationUtilities;
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
 *     <li>{@link TransformableMechanic#verticalOffset} Additional up offset. This is based on the caster's yaw and pitch. This is equivalent to <code>forwardOffset</code> but with pitch rotated by +90.</li>
 *     <li>{@link TransformableMechanic#scale} This mechanic takes the points and calculates a center point. Then, for each of the points, it calculates a vector from the center to that point. The size field simply multiplies this vector.</li>
 * </ul>
 * To use this mechanic, simply extend it in your own mechanic class and do the following:
 * <ol>
 *     <li>Override the {@link TransformableMechanic#getPoints} method. This method is called when the skill is being cast, and is expected to return a list of points that are going to be transformed.</li>
 *     <li>Override the {@link TransformableMechanic#cast} method. This method should contain what the skill actually executes. This method will be called with the skill metadata and transformed points as a parameter.</li>
 * </ol>
 *
 * @author 0TickPulse
 * @see LocationUtilities
 * @see SkillMechanic
 */
@ComponentEntry(
        type = ComponentType.MECHANIC,
        name = "transformable",
        aliases = {"transformablemechanic"},
        fields = {
                @MythicField(
                        name = "xoffset",
                        description = "Additional offset in the X-axis.",
                        aliases = {"xo", "ox", "xoff"},
                        defValue = "0"
                ),
                @MythicField(
                        name = "yoffset",
                        description = "Additional offset in the Y-axis.",
                        aliases = {"yo", "oy", "yoff"},
                        defValue = "0"
                ),
                @MythicField(
                        name = "zoffset",
                        description = "Additional offset in the Z-axis.",
                        aliases = {"zo", "oz", "zoff"},
                        defValue = "0"
                ),
                @MythicField(
                        name = "forwardoffset",
                        description = "Additional forward offset. This is based on the caster's yaw and pitch.",
                        aliases = {"fo", "of", "foff"},
                        defValue = "0"
                ),
                @MythicField(
                        name = "rightoffset",
                        description = "Additional right offset. This is based on the caster's yaw and pitch. This is equivalent to `forwardoffset` but with pitch set to 0 and yaw rotated by -90.",
                        aliases = {"ro", "or", "roff"},
                        defValue = "0"
                ),
                @MythicField(
                        name = "verticalOffset",
                        description = "Additional vertical offset. This is based on the caster's yaw and pitch. This is equivalent to `forwardoffset` but with pitch rotated by +90.",
                        aliases = {"vo", "ov", "voff"},
                        defValue = "0"
                ),
                @MythicField(
                        name = "scale",
                        description = "This mechanic takes the points and calculates a center point. Then, for each of the points, it calculates a vector from the center to that point. The size field simply multiplies this vector.",
                        defValue = "1"
                ),
                @MythicField(
                        name = "rotation",
                        aliases = {"rot"},
                        description = "The rotation of the slash in degrees.",
                        defValue = "0"
                ),
                @MythicField(
                        name = "radians",
                        aliases = {"rad", "useradians", "ur"},
                        description = "Whether to use radians instead of degrees for the rotation.",
                        defValue = "false"
                ),
        }
)
public abstract class TransformableMechanic extends CustomMythicMechanic implements ITargetedLocationSkill, ITargetedEntitySkill {
    protected final PlaceholderDouble xOffset;
    protected final PlaceholderDouble yOffset;
    protected final PlaceholderDouble zOffset;
    protected final PlaceholderDouble forwardOffset;
    protected final PlaceholderDouble rightOffset;
    protected final PlaceholderDouble verticalOffset;
    protected final PlaceholderDouble scale;
    protected final PlaceholderDouble rotation;
    protected final boolean radians;

    public TransformableMechanic(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
        super(manager, file, line, mlc);
        this.xOffset = mlc.getPlaceholderDouble(new String[]{"xoffset", "xo", "ox", "xoff"}, 0);
        this.yOffset = mlc.getPlaceholderDouble(new String[]{"yoffset", "yo", "oy", "yoff"}, 0);
        this.zOffset = mlc.getPlaceholderDouble(new String[]{"zoffset", "zo", "oz", "zoff"}, 0);
        this.forwardOffset = mlc.getPlaceholderDouble(new String[]{"forwardoffset", "fo", "of", "foff"}, 0);
        this.rightOffset = mlc.getPlaceholderDouble(new String[]{"rightoffset", "ro", "or", "roff"}, 0);
        this.verticalOffset = mlc.getPlaceholderDouble(new String[]{"verticaloffset", "vo", "ov", "voff"}, 0);
        this.scale = mlc.getPlaceholderDouble(new String[]{"scale", "s"}, 1);
        this.rotation = mlc.getPlaceholderDouble(new String[]{"rotation", "rot"}, 0);
        this.radians = mlc.getBoolean(new String[]{"radians", "rad", "useradians", "ur"}, false);
    }

    public double toRadians(double angle) {
        return radians ? angle : Math.toRadians(angle);
    }

    protected List<Location> transform(SkillMetadata skillMetadata, List<Location> locations) {
        double averageX = locations.stream().map(Location::getX).reduce(0.0, Double::sum) / locations.size();
        double averageY = locations.stream().map(Location::getY).reduce(0.0, Double::sum) / locations.size();
        double averageZ = locations.stream().map(Location::getZ).reduce(0.0, Double::sum) / locations.size();
        float averageYaw = locations.stream().map(Location::getYaw).reduce(0.0F, Float::sum) / locations.size();
        float averagePitch = locations.stream().map(Location::getPitch).reduce(0.0F, Float::sum) / locations.size();
        Location center = new Location(locations.get(0).getWorld(), averageX, averageY, averageZ, averageYaw, averagePitch);
        Location casterLocation = BukkitAdapter.adapt(skillMetadata.getCaster().getLocation());
        return locations.stream().map(location -> {
            // add offsets
            location.add(xOffset.get(), yOffset.get(), zOffset.get());
            // add relative offsets
            location = LocationUtilities.relativeOffset(
                    location.clone().setDirection(
                            casterLocation.getDirection()
                    ),
                    forwardOffset.get(),
                    rightOffset.get(),
                    verticalOffset.get()
            );
            // rotations
            Vector casterToTarget = location.toVector().subtract(casterLocation.toVector());
            casterToTarget = casterToTarget.rotateAroundAxis(casterLocation.getDirection().normalize(), toRadians(rotation.get()));
            location = casterLocation.clone().add(casterToTarget);
            // size transformation
            Vector vector = location.clone().subtract(center).toVector();
            return center.clone().add(vector.multiply(scale.get()));
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
