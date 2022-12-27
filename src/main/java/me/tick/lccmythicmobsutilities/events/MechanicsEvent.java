package me.tick.lccmythicmobsutilities.events;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;
import me.tick.lccmythicmobsutilities.LccMythicmobsUtilities;
import me.tick.lccmythicmobsutilities.components.mechanics.SlashMechanic;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import me.tick.lccmythicmobsutilities.models.generic.generators.MechanicGenerator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.util.*;

public class MechanicsEvent implements Listener {
    public static Set<MechanicGenerator> mechanics = new HashSet<>();
    public static Set<Class<? extends SkillMechanic>> legacyMechanics = new HashSet<>();

    static {
        //mechanics.add(new MechanicGenerator(new MessageMechanic()));
    }

    @EventHandler
    public void onMechanicsLoad(MythicMechanicLoadEvent event) {
        try {
            registerMechanics(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerMechanics(MythicMechanicLoadEvent event) {
        for (MechanicGenerator mechanic : mechanics) {
            mechanic.register(event);
        }
        SkillExecutor executor = event.getContainer().getManager();
        File file = event.getContainer().getFile();
        String line = event.getConfig().getLine();
        MythicLineConfig mlc = event.getConfig();
        registerLegacyMechanic(new SlashMechanic(executor, file, line, mlc), event);
    }

    public static void registerLegacyMechanic(SkillMechanic mechanic, MythicMechanicLoadEvent event) {
        ComponentEntry annotation = mechanic.getClass().getAnnotation(ComponentEntry.class);
        if (annotation == null) {
            throw new Error("MechanicEntry annotation is missing from " + mechanic.getClass().getName());
        }
        legacyMechanics.add(mechanic.getClass());
        List<String> names = new ArrayList<>();
        names.add(annotation.name());
        names.addAll(Arrays.asList(annotation.aliases()));
        LccMythicmobsUtilities.getPlugin().getLogger().info("Registering legacy mechanic " + mechanic.getClass().getName() + " with names " + names);
        if (names.stream().map(String::toLowerCase).toList().contains(event.getMechanicName().toLowerCase())) {
            event.register(mechanic);
        }
    }
}
