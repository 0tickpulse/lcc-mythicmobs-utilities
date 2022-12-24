package me.tick.lccmythicmobsutilities.models;

import io.lumine.mythic.core.utils.annotations.MythicCondition;
import io.lumine.mythic.core.utils.annotations.MythicField;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents an entry for a custom MythicCondition. This should always be used on anything that implements ISkillCondition, or should be registered as a condition.
 * This is mandatory for registering conditions - the data field's name is used for validating condition names.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionEntry {
    MythicCondition data();
    String[] examples() default {};
    MythicField[] fields() default {};
}
