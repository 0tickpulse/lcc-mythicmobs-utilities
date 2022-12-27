package me.tick.lccmythicmobsutilities.components.conditions;

import com.google.common.base.Function;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityComparisonCondition;
import io.lumine.mythic.core.utils.annotations.MythicField;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import me.tick.lccmythicmobsutilities.models.ComponentType;
import me.tick.lccmythicmobsutilities.models.CustomMythicCondition;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;

@ComponentEntry(
        type = ComponentType.CONDITION,
        name = "canattack",
        description = "Checks if an entity can attack another entity. It's done by simulating a damage event.",
        aliases = {"canattackentity", "candamage", "candamageentity"},
        author = "0TickPulse",
        fields = {
                @MythicField(
                        name = "cause",
                        aliases = {"damagecause", "c"},
                        defValue = "CUSTOM",
                        description = "The cause of the damage event. Defaults to CUSTOM."
                )
        }
)
public class CanAttackCondition extends CustomMythicCondition implements IEntityComparisonCondition {

    public final EntityDamageEvent.DamageCause cause;

    public CanAttackCondition(MythicLineConfig mlc) {
        super(mlc);
        String causeString = mlc.getString(new String[]{"cause", "damagecause", "c"}, "CUSTOM");
        EntityDamageEvent.DamageCause damageCause;
        try {
            damageCause = EntityDamageEvent.DamageCause.valueOf(causeString);
        } catch (IllegalArgumentException e) {
            damageCause = EntityDamageEvent.DamageCause.CUSTOM;
        }
        this.cause = damageCause;
    }

    public static boolean isRunning;

    @Override
    public boolean check(AbstractEntity caster, AbstractEntity target) {
        if (isRunning) {
            return false;
        }
        Entity attacker = caster.getBukkitEntity();
        Entity targetEntity = target.getBukkitEntity();
        isRunning = true;
        Map<EntityDamageEvent.DamageModifier, Double> damageMap = new HashMap<>();
        damageMap.put(EntityDamageEvent.DamageModifier.BASE, 0.0);
        Map<EntityDamageEvent.DamageModifier, Function<Double, Double>> modifierFunctions = new HashMap<>();
        modifierFunctions.put(EntityDamageEvent.DamageModifier.BASE, input -> input);
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(
                attacker,
                targetEntity,
                cause,
                damageMap,
                modifierFunctions,
                false);
        try {
            Bukkit.getPluginManager().callEvent(event);
            return !event.isCancelled();
        } catch (IllegalStateException e) {
            return false;
        } finally {
            isRunning = false;
        }
    }
}
