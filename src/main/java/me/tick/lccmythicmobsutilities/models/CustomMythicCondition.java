package me.tick.lccmythicmobsutilities.models;

import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.core.skills.SkillCondition;

public abstract class CustomMythicCondition extends SkillCondition {
    public CustomMythicCondition(MythicLineConfig mlc) {
        super(mlc.getLine());
    }
}
