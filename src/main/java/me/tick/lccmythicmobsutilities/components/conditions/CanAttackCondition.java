package me.tick.lccmythicmobsutilities.components.conditions;

import com.google.common.base.Function;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.skills.conditions.IEntityComparisonCondition;
import io.lumine.mythic.core.skills.SkillCondition;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import me.tick.lccmythicmobsutilities.models.ComponentType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;

@ComponentEntry(
        type = ComponentType.CONDITION,
        name = "canattack",
        description = "Checks if an entity can attack another entity",
        aliases = {"canattackentity", "candamage", "candamageentity"},
        author = "0TickPulse"
)
public class CanAttackCondition extends SkillCondition implements IEntityComparisonCondition {
    public CanAttackCondition(String line) {
        super(line);
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
        Map<EntityDamageEvent.DamageModifier, ? extends Function<? super Double, Double>> modifierFunctions = new HashMap<>();
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(attacker, targetEntity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damageMap, modifierFunctions, false);
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
