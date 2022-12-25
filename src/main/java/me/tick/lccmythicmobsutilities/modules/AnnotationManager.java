package me.tick.lccmythicmobsutilities.modules;

import io.lumine.mythic.core.utils.annotations.MythicField;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import me.tick.lccmythicmobsutilities.models.ComponentType;

public class AnnotationManager {
    private ComponentEntry annotation;

    public AnnotationManager(ComponentEntry annotation) {
        this.annotation = annotation;
    }

    public ComponentEntry getAnnotation() {
        return annotation;
    }

    public void inheritAnnotations() {
        for (Class<?> clazz : annotation.inherit()) {
            ComponentEntry inheritedAnnotation = clazz.getAnnotation(ComponentEntry.class);
            if (inheritedAnnotation == null) {
                continue;
            }
            this.setFields(inheritedAnnotation.fields());
        }
    }

    public void setFields(MythicField[] fields) {
        // set annotation's fields() to fields
        this.annotation = new ComponentEntry() {
            @Override
            public ComponentType type() {
                return annotation.type();
            }

            @Override
            public String name() {
                return annotation.name();
            }

            @Override
            public String[] aliases() {
                return annotation.aliases();
            }

            @Override
            public String author() {
                return annotation.author();
            }

            @Override
            public String description() {
                return annotation.description();
            }

            @Override
            public String[] examples() {
                return annotation.examples();
            }

            @Override
            public MythicField[] fields() {
                return fields;
            }

            @Override
            public Class[] inherit() {
                return annotation.inherit();
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return annotation.annotationType();
            }
        };
    }
}