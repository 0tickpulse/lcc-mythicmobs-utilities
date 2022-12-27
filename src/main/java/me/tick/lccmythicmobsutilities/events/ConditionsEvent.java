package me.tick.lccmythicmobsutilities.events;

import io.lumine.mythic.api.skills.conditions.ISkillCondition;
import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import me.tick.lccmythicmobsutilities.LccMythicmobsUtilities;
import me.tick.lccmythicmobsutilities.components.conditions.CanAttackCondition;
import me.tick.lccmythicmobsutilities.components.conditions.mcmmo.McMMOSamePartyCondition;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import me.tick.lccmythicmobsutilities.models.CustomMythicCondition;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ConditionsEvent implements Listener {

    public static Set<Class<? extends CustomMythicCondition>> conditions = new LinkedHashSet<>();
    static {
        conditions.add(CanAttackCondition.class);
        conditions.add(McMMOSamePartyCondition.class);
    }

    @EventHandler
    public void onConditionsLoad(MythicConditionLoadEvent event) {
        registerConditions(event);
        //registerCondition(new WorldGuardFlagCondition(), event);
    }

    public static void registerConditions(MythicConditionLoadEvent event) {
        for (Class<? extends ISkillCondition> condition : conditions) {
            ComponentEntry annotation = condition.getAnnotation(ComponentEntry.class);
            if (annotation == null) {
                throw new Error("ComponentEntry annotation is missing from " + condition.getName());
            }
            List<String> names = new ArrayList<>();
            names.add(annotation.name());
            names.addAll(Arrays.asList(annotation.aliases()));
            LccMythicmobsUtilities.getPlugin().getLogger().info("Registering condition " + condition.getName() + " with names " + names);
            if (names.stream().map(String::toLowerCase).toList().contains(event.getConditionName().toLowerCase())) {
                try {
                    event.register(condition.getConstructor(new Class[0]).newInstance(event.getConfig()));
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    LccMythicmobsUtilities.error(e);
                }
            }
        }
    }
}
