package me.tick.lccmythicmobsutilities.events;

import io.lumine.mythic.api.skills.conditions.ISkillCondition;
import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import me.tick.lccmythicmobsutilities.LccMythicmobsUtilities;
import me.tick.lccmythicmobsutilities.components.conditions.McMMOSamePartyCondition;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class ConditionsEvent implements Listener {

    public static Set<Class<? extends ISkillCondition>> conditions = new HashSet<>();

    @EventHandler
    public void onConditionsLoad(MythicConditionLoadEvent event) {
        registerCondition(new McMMOSamePartyCondition(), event);
        //registerCondition(new WorldGuardFlagCondition(), event);
    }

    public static void registerCondition(ISkillCondition condition, MythicConditionLoadEvent event) {
        ComponentEntry annotation = condition.getClass().getAnnotation(ComponentEntry.class);
        if (annotation == null) {
            throw new Error("ConditionEntry annotation is missing from " + condition.getClass().getName());
        }
        conditions.add(condition.getClass());
        List<String> names = new ArrayList<>();
        names.add(annotation.name());
        names.addAll(Arrays.asList(annotation.aliases()));
        LccMythicmobsUtilities.getPlugin().getLogger().info("Registering condition " + condition.getClass().getName() + " with names " + names);
        if (names.stream().map(String::toLowerCase).toList().contains(event.getConditionName().toLowerCase())) {
            event.register(condition);
        }
    }
}
