package me.tick.lccmythicmobsutilities.components.mechanics;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.*;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.api.skills.placeholders.PlaceholderInt;
import io.lumine.mythic.api.skills.placeholders.PlaceholderString;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.utils.Schedulers;
import io.lumine.mythic.core.logging.MythicLogger;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.utils.annotations.MythicField;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import me.tick.lccmythicmobsutilities.models.ComponentType;
import me.tick.lccmythicmobsutilities.modules.SlashGenerator;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ComponentEntry(
        type = ComponentType.MECHANIC,
        name = "slash",
        author = "0Tick",
        description = """
                Performs a slash. This slash is executed by calculating points along a circle.
                This circle is calculated by fields like `points` and `radius`.
                When used effectively, this mechanic can simulate a weapon's slash.
                This mechanic also provides entity targeting and allows you to specify actions performed on targeted entities through the `onhitskill` field.
                However, if you want more lenient targeting, you can use the `@EntitiesInCone` targeter provided by Mythic.""",
        inherit = {TransformableMechanic.class},
        fields = {
                @MythicField(
                        name = "onpointskill",
                        aliases = {"onpoint", "op"},
                        description = "The skill to perform for every point in the slash."),
                @MythicField(
                        name = "onhitskill",
                        aliases = {"onhit", "oh"},
                        description = "The skill to perform when the slash ends."),
                @MythicField(
                        name = "radius",
                        aliases = {"r"},
                        description = "The radius of the slash.",
                        defValue = "2"
                ),
                @MythicField(
                        name = "points",
                        aliases = {"p"},
                        description = "The number of points in the slash.",
                        defValue = "5"),
                @MythicField(
                        name = "rotation",
                        aliases = {"rot"},
                        description = "The rotation of the slash in degrees.",
                        defValue = "0"),
                @MythicField(
                        name = "arc",
                        aliases = {"a"},
                        description = "The arc of the slash in degrees.",
                        defValue = "180"),
                @MythicField(
                        name = "interval",
                        description = "The interval between each iteration in the slash.",
                        aliases = {"i"},
                        defValue = "0"),
                @MythicField(
                        name = "iterationCount",
                        description = "The number of points each iteration will have.",
                        aliases = {"count", "ic", "c"},
                        defValue = "1"),
                @MythicField(
                        name = "lineDistance",
                        description = "When slashing, sometimes the target entities are in between the caster and the points of the slash, causing the entity not to be hit. In order to circumvent this, the slash mechanic also takes points in between each slash point and checks for entities there. This is the distance between each line point. Set to 0 to disable.",
                        aliases = {"ld"},
                        defValue = "0.2"),
                @MythicField(
                        name = "pointRadius",
                        description = "Each point in the slash checks for entities within a certain radius to determine if the entity was hit. This is the radius of each point.",
                        aliases = {"pr"},
                        defValue = "0.2")
        },
        examples = {"""
                SlashTest:
                  Skills:
                  - slash{onpointskill=SlashTestTick;points=80;r=5;rot=<random.1to180>} @forward{f=0;uel=true}
                                        
                SlashTestTick:
                  Skills:
                  - e:p{p=flame} @Origin"""})
public class SlashMechanic extends TransformableMechanic implements ITargetedLocationSkill, ITargetedEntitySkill {

    private final PlaceholderString onPointSkillName;
    private final PlaceholderString onHitSkillName;
    private final PlaceholderDouble radius;
    private final PlaceholderInt points;
    private final PlaceholderDouble rotation;
    private final PlaceholderDouble arc;
    private final PlaceholderInt iterationInterval;
    private final PlaceholderInt iterationCount;
    private final PlaceholderDouble lineDistance;
    private final PlaceholderDouble pointRadius;
    private Optional<Skill> onPointSkill;
    private Optional<Skill> onHitSkill;

    public SlashMechanic(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
        super(manager, file, line, mlc);

        this.onPointSkillName = mlc.getPlaceholderString(new String[]{"onpointskill", "onpoint", "op"}, "");
        this.onHitSkillName = mlc.getPlaceholderString(new String[]{"onhitskill", "onhit", "oh"}, "");

        this.radius = mlc.getPlaceholderDouble(new String[]{"radius", "r"}, "2");
        this.points = mlc.getPlaceholderInteger(new String[]{"points", "p"}, "5");

        this.rotation = mlc.getPlaceholderDouble(new String[]{"rotation", "rot"}, "0");
        this.arc = mlc.getPlaceholderDouble(new String[]{"arc", "a"}, "180");

        this.iterationInterval = mlc.getPlaceholderInteger(new String[]{"interval", "i"}, "1");
        this.iterationCount = mlc.getPlaceholderInteger(new String[]{"iterationCount", "count", "ic", "c"}, "1");

        this.lineDistance = mlc.getPlaceholderDouble(new String[]{"lineDistance", "ld"}, "0.2");
        this.pointRadius = mlc.getPlaceholderDouble(new String[]{"pointRadius", "pr"}, "0.2");

        this.getManager().queueSecondPass(() -> {
            this.onPointSkill = this.getManager().getSkill(this.onPointSkillName.get());
            if (this.onPointSkill.isEmpty() && !this.onPointSkillName.get().equals("")) {
                MythicLogger.error("Could not find onPointSkill " + this.onPointSkillName.get());
            }
            this.onHitSkill = this.getManager().getSkill(this.onHitSkillName.get());
            if (this.onHitSkill.isEmpty() && !this.onHitSkillName.get().equals("")) {
                MythicLogger.error("Could not find onHitSkill " + this.onHitSkillName.get());
            }
        });
    }

    @Override
    public List<Location> getPoints(SkillMetadata skillMetadata, Location target) {
        return SlashGenerator.getSlashLocations(
                target,
                this.radius.get(skillMetadata),
                this.rotation.get(skillMetadata),
                this.points.get(skillMetadata),
                this.arc.get(skillMetadata)
        );
    }

    @Override
    public SkillResult cast(SkillMetadata skillMetadata, List<Location> locations) {
        int i = 0;
        for (Location point : locations) {
            int interval = this.iterationInterval.get(skillMetadata);
            int count = this.iterationCount.get(skillMetadata);
            // get delay based on iteration count and interval
            int currentIteration = i / count + 1;
            int delay = interval * currentIteration;
            SkillMetadata pointData = skillMetadata.deepClone();
            pointData.setOrigin(BukkitAdapter.adapt(point));
            this.onPointSkill.ifPresent(skill -> Schedulers.sync().runLater(() -> skill.execute(pointData), delay));
            i++;
        }
        Set<Entity> entities = SlashGenerator.getEntitiesInPoints(BukkitAdapter.adapt(skillMetadata.getCaster().getLocation()), new HashSet<>(locations), this.pointRadius.get(), this.lineDistance.get());
        for (Entity entity : entities) {
            SkillMetadata hitData = skillMetadata.deepClone();
            hitData.setOrigin(BukkitAdapter.adapt(entity.getLocation()));
            hitData.setEntityTarget(BukkitAdapter.adapt(entity));
            this.onHitSkill.ifPresent(skill -> skill.execute(hitData));
        }
        return SkillResult.SUCCESS;
    }
}
