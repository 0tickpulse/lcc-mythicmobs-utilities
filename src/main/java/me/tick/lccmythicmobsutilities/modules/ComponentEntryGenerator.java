package me.tick.lccmythicmobsutilities.modules;

import io.lumine.mythic.core.utils.annotations.MythicField;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import me.tick.lccmythicmobsutilities.models.ComponentType;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * A generator class for {@link ComponentEntry}s, allowing users to change properties within instances much easier.
 * To use:
 * <ol>
 *     <li>Create an instance of this class. You can do so by either providing no arguments - this will create an empty instance,
 * or by providing a {@link ComponentEntry} - this will create an instance with the same properties as the provided entry.
 *     </li>
 *     <li>
 *         Modify the properties of the component entry using the appropriate setter methods. These methods return the instance, allowing you to perform method chaining with them.
 *     </li>
 *     <li>
 *         Once you have modified the properties to your liking, you can call {@link #generate()} to create a new {@link ComponentEntry} with the modified properties.
 *     </li>
 * </ol>
 * @author 0TickPulse
 */
public class ComponentEntryGenerator {
    public String name;
    public ComponentType type;
    public String[] aliases = new String[0];
    public String author;
    public String description;
    public String[] examples = new String[0];
    public MythicField[] fields = new MythicField[0];
    public Class<?>[] inherit = new Class[0];

    public ComponentEntryGenerator() {
    }

    public ComponentEntryGenerator(ComponentEntry annotation) {
        this.name = annotation.name();
        this.type = annotation.type();
        this.aliases = annotation.aliases();
        this.author = annotation.author();
        this.description = annotation.description();
        this.examples = annotation.examples();
        this.fields = annotation.fields();
        this.inherit = annotation.inherit();
    }

    /**
     * Applies a transformer function on the component entry's name and aliases.
     * @param transformer The transformer function to apply.
     */
    public ComponentEntryGenerator transformNames(Function<String, String> transformer) {
        this.name = transformer.apply(this.name);
        this.aliases = Arrays.stream(this.aliases).map(transformer).toArray(String[]::new);
        return this;
    }

    /**
     * Combines the name and aliases.
     */
    public String[] getNames() {
        List<String> names = new ArrayList<>();
        names.add(name);
        names.addAll(List.of(aliases));
        return names.toArray(new String[0]);
    }

    public ComponentEntryGenerator inheritAnnotations() {
        for (Class<?> clazz : inherit) {
            ComponentEntry inheritedAnnotation = clazz.getAnnotation(ComponentEntry.class);
            if (inheritedAnnotation == null) {
                continue;
            }
            List<MythicField> fields = new ArrayList<>();
            fields.addAll(List.of(inheritedAnnotation.fields()));
            fields.addAll(List.of(this.fields));
            this.setFields(fields.toArray(MythicField[]::new));
        }
        return this;
    }

    public ComponentEntryGenerator setName(String name) {
        this.name = name;
        return this;
    }

    public ComponentEntryGenerator setType(ComponentType type) {
        this.type = type;
        return this;
    }

    public ComponentEntryGenerator setAliases(String[] aliases) {
        this.aliases = aliases;
        return this;
    }

    public ComponentEntryGenerator addAlias(String alias) {
        List<String> aliases = new ArrayList<>(List.of(this.aliases));
        aliases.add(alias);
        this.setAliases(aliases.toArray(String[]::new));
        return this;
    }

    public ComponentEntryGenerator setAuthor(String author) {
        this.author = author;
        return this;
    }

    public ComponentEntryGenerator setDescription(String description) {
        this.description = description;
        return this;
    }

    public ComponentEntryGenerator setExamples(String[] examples) {
        this.examples = examples;
        return this;
    }

    public ComponentEntryGenerator addExample(String example) {
        List<String> examples = new ArrayList<>(List.of(this.examples));
        examples.add(example);
        this.setExamples(examples.toArray(String[]::new));
        return this;
    }

    public ComponentEntryGenerator setFields(MythicField[] fields) {
        this.fields = fields;
        return this;
    }

    public ComponentEntryGenerator addField(MythicField field) {
        List<MythicField> fields = new ArrayList<>(List.of(this.fields));
        fields.add(field);
        this.setFields(fields.toArray(MythicField[]::new));
        return this;
    }

    public ComponentEntry generate() {
        return new ComponentEntry() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return ComponentEntry.class;
            }

            @Override
            public ComponentType type() {
                return ComponentType.PLACEHOLDER;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public String[] aliases() {
                return aliases;
            }

            @Override
            public String author() {
                return author;
            }

            @Override
            public String description() {
                return description;
            }

            @Override
            public String[] examples() {
                return examples;
            }

            @Override
            public MythicField[] fields() {
                return fields;
            }

            @Override
            public Class<?>[] inherit() {
                return inherit;
            }
        };
    }
}