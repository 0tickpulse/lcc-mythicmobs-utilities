package me.tick.lccmythicmobsutilities.models;

import io.lumine.mythic.core.utils.annotations.MythicField;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentEntry {
    ComponentType type();

    String name() default "";

    String[] aliases() default {};

    String author() default "";

    String description() default "";

    String[] examples() default {};

    MythicField[] fields() default {};

    Class[] inherit() default {};
}
