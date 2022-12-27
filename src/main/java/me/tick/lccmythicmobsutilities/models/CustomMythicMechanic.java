package me.tick.lccmythicmobsutilities.models;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;

import java.io.File;

public abstract class CustomMythicMechanic extends SkillMechanic {
    public CustomMythicMechanic(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
        super(manager, file, line, mlc);
    }
}
