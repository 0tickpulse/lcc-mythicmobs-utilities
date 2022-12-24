package me.tick.lccmythicmobsutilities.models;

import io.lumine.mythic.core.utils.annotations.MythicField;
import io.lumine.mythic.core.utils.annotations.MythicMechanic;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MechanicEntry {
    MythicMechanic data();
    String[] examples() default {};
    MythicField[] fields() default {};
}

