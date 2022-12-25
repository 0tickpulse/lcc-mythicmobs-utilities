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

    public AnnotationManager inheritAnnotations() {
        for (Class<?> clazz : annotation.inherit()) {
            ComponentEntry inheritedAnnotation = clazz.getAnnotation(ComponentEntry.class);
            if (inheritedAnnotation == null) {
                continue;
            }
            this.setFields(inheritedAnnotation.fields());
        }
        return this;
    }

    public AnnotationManager setFields(MythicField[] fields) {
        // set annotation's fields() to fields
        ComponentEntry cur = annotation;
        this.annotation = new ComponentEntry() {
            @Override
            public ComponentType type() {
                return cur.type();
            }

            @Override
            public String name() {
                return cur.name();
            }

            @Override
            public String[] aliases() {
                return cur.aliases();
            }

            @Override
            public String author() {
                return cur.author();
            }

            @Override
            public String description() {
                return cur.description();
            }

            @Override
            public String[] examples() {
                return cur.examples();
            }

            @Override
            public MythicField[] fields() {
                return fields;
            }

            @Override
            public Class[] inherit() {
                return cur.inherit();
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return cur.annotationType();
            }
        };
        return this;
    }
}