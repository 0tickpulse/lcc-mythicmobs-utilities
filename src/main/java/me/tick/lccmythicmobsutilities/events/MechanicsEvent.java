package me.tick.lccmythicmobsutilities.events;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ISkillMechanic;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.mythic.core.skills.SkillExecutor;
import me.tick.lccmythicmobsutilities.LccMythicmobsUtilities;
import me.tick.lccmythicmobsutilities.components.mechanics.SlashMechanic;
import me.tick.lccmythicmobsutilities.models.MechanicEntry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.*;

public class MechanicsEvent implements Listener {
    public static Set<Class<? extends ISkillMechanic>> mechanics = new HashSet<>();

    @EventHandler
    public void onMechanicsLoad(MythicMechanicLoadEvent event) {
        SkillExecutor executor = event.getContainer().getManager();
        File file = event.getContainer().getFile();
        String line = event.getConfig().getLine();
        MythicLineConfig mlc = event.getConfig();
        registerMechanic(new SlashMechanic(executor, file, line, mlc), event);
    }

    public static void registerMechanic(ISkillMechanic mechanic, MythicMechanicLoadEvent event) {
        MechanicEntry annotation = mechanic.getClass().getAnnotation(MechanicEntry.class);
        if (annotation == null) {
            throw new Error("MechanicEntry annotation is missing from " + mechanic.getClass().getName());
        }
        LccMythicmobsUtilities.getPlugin().getLogger().info("Registering mechanic " + mechanic.getClass().getName());
        mechanics.add(mechanic.getClass());
        List<String> names = new ArrayList<>();
        names.add(annotation.data().name());
        names.addAll(Arrays.asList(annotation.data().aliases()));
        if (names.stream().map(String::toLowerCase).toList().contains(event.getMechanicName().toLowerCase())) {
            event.register(mechanic);
        }
    }
}
