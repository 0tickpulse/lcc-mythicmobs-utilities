package me.tick.lccmythicmobsutilities.models.generic.fields;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldDefaultString {
    String value();
}
