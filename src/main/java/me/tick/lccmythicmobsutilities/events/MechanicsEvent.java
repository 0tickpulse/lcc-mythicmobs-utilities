package me.tick.lccmythicmobsutilities.events;

import io.lumine.mythic.api.skills.ISkillMechanic;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import me.tick.lccmythicmobsutilities.LccMythicmobsUtilities;
import me.tick.lccmythicmobsutilities.components.mechanics.SlashMechanic;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import me.tick.lccmythicmobsutilities.models.CustomMythicMechanic;
import me.tick.lccmythicmobsutilities.models.generic.generators.MechanicGenerator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class MechanicsEvent implements Listener {
    public static Set<MechanicGenerator> mechanics = new LinkedHashSet<>();
    public static Set<Class<? extends CustomMythicMechanic>> legacyMechanics = new LinkedHashSet<>();

    static {
        //mechanics.add(new MechanicGenerator(new MessageMechanic()));
    }
    static {
        legacyMechanics.add(SlashMechanic.class);
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
        registerlegacyMechanics(event);
    }

    public static void registerlegacyMechanics(MythicMechanicLoadEvent event) {
        for (Class<? extends ISkillMechanic> mechanic : legacyMechanics) {
            ComponentEntry annotation = mechanic.getAnnotation(ComponentEntry.class);
            if (annotation == null) {
                throw new Error("ComponentEntry annotation is missing from " + mechanic.getName());
            }
            List<String> names = new ArrayList<>();
            names.add(annotation.name());
            names.addAll(Arrays.asList(annotation.aliases()));
            LccMythicmobsUtilities.getPlugin().getLogger().info("Registering condition " + mechanic.getName() + " with names " + names);
            if (names.stream().map(String::toLowerCase).toList().contains(event.getMechanicName().toLowerCase())) {
                try {
                    event.register(mechanic.getConstructor(new Class[0]).newInstance(event.getContainer().getManager(), event.getContainer().getFile(), event.getConfig().getLine(), event.getConfig()));
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                         InvocationTargetException e) {
                    LccMythicmobsUtilities.error(e);
                }
            }
        }
    }
}
