package me.tick.lccmythicmobsutilities;

import io.lumine.mythic.core.utils.annotations.MythicField;
import me.tick.lccmythicmobsutilities.models.ComponentEntry;
import me.tick.lccmythicmobsutilities.models.ComponentType;
import me.tick.lccmythicmobsutilities.modules.ComponentEntryGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DocumentationGenerator {

    // method that writes generate() to a file
    public static void writeToFile() throws IOException {
        File folder = LccMythicmobsUtilities.getPlugin().getDataFolder();
        if (!folder.exists()) {
            folder.mkdir();
        }
        String path = folder + File.separator + "documentation.md";
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(path);
        writer.write(generate() + System.lineSeparator());
        writer.close();
    }

    public static String generate() {
        List<ComponentEntry> conditions = new ArrayList<>();
        List<ComponentEntry> mechanics = new ArrayList<>();
        List<ComponentEntry> placeholders = new ArrayList<>();
        // get all conditions and mechanics
        for (ComponentEntry entry : LccMythicmobsUtilities.getComponentAnnotations()) {
            if (entry.type() == ComponentType.CONDITION) {
                conditions.add(entry);
            } else if (entry.type() == ComponentType.MECHANIC) {
                mechanics.add(entry);
            } else if (entry.type() == ComponentType.PLACEHOLDER) {
                placeholders.add(entry);
            }
        }
        String sep = System.lineSeparator() + System.lineSeparator();
        InputStream is = LccMythicmobsUtilities.getPlugin().getResource("otherstuff.md");
        String otherStuffText;
        if (is == null) {
            otherStuffText = "";
        }
        else {
            try {
                otherStuffText = new String(is.readAllBytes());
            } catch (IOException e) {
                LccMythicmobsUtilities.error(e);
                otherStuffText = "";
            }
        }

        return "# LCC MythicMobs Utilities - Autogenerated Documentation"
                + sep
                + "## Conditions"
                + sep
                + String.join(sep, conditions.stream().map(DocumentationGenerator::generateDocumentation).toList())
                + sep
                + "## Mechanics"
                + sep
                + String.join(sep, mechanics.stream().map(DocumentationGenerator::generateDocumentation).toList())
                + sep
                + "## Placeholders"
                + sep
                + String.join(sep, placeholders.stream().map(DocumentationGenerator::generateDocumentation).toList())
                + sep
                + otherStuffText;
    }

    public static String generateDocumentation(ComponentEntry clazz) {
        List<String> lines = new ArrayList<>();
        String sep = System.lineSeparator();
        ComponentEntryGenerator manager = new ComponentEntryGenerator(clazz).inheritAnnotations();
        ComponentEntry entry = manager.generate();

        // name
        if (clazz.type() == ComponentType.CONDITION) {
            lines.add("### Condition: `" + entry.name() + "`");
        } else if (clazz.type() == ComponentType.MECHANIC) {
            lines.add("### Mechanic: `" + entry.name() + "`");
        } else if (clazz.type() == ComponentType.PLACEHOLDER) {
            lines.add("### Placeholder: `" + entry.name() + "`");
        } else {
            lines.add("### Unknown: `" + entry.name() + "`");
        }
        // description
        lines.add(entry.description());
        // author
        lines.add("Author: " + entry.author());
        // fields
        if (entry.fields().length > 0) {
            lines.add("#### Fields");
            for (MythicField field : entry.fields()) {
                lines.add("- `" + field.name() + "`: " + field.description());
                if (field.aliases().length > 0) {
                    lines.add("  Aliases: " + String.join(", ", Arrays.stream(field.aliases()).map(alias -> "`" + alias + "`").toArray(String[]::new)));
                }
                if (field.defValue().length() > 0) {
                    lines.add("  Default: `" + field.defValue() + "`");
                }
            }
        }
        // examples
        if (entry.examples().length > 0) {
            lines.add("#### Examples");
            for (String example : entry.examples()) {
                lines.add("```yaml" + sep + example + sep + "```");
            }
        }
        return String.join(sep + sep, lines);
    }

//    public static String generateConditions() {
//        List<String> conditionEntries = new ArrayList<>();
//        LccMythicmobsUtilities.getPlugin().getLogger().info(
//                "Generating documentation for conditions "
//                        + String.join(", ", ConditionsEvent.conditions.stream().map(Class::getSimpleName).toList()))
//        ;
//        ConditionsEvent.conditions.forEach((condition) -> {
//            LccMythicmobsUtilities.getPlugin().getLogger().log(java.util.logging.Level.INFO, "Generating docs for condition " + condition.getName());
//            List<String> components = new ArrayList<>();
//            ConditionEntry annotation = condition.getAnnotation(ConditionEntry.class);
//            if (annotation == null) {
//                return;
//            }
//            MythicCondition data = annotation.data();
//            components.add("### Condition: `" + data.name() + "`");
//            components.addAll(generateGeneric(data.description(), annotation.examples(), annotation.fields()));
//            conditionEntries.add(String.join(System.lineSeparator() + System.lineSeparator(), components));
//        });
//        return String.join(System.lineSeparator() + System.lineSeparator(), conditionEntries);
//    }
//
//    public static String generateMechanics() {
//        List<String> mechanicEntries = new ArrayList<>();
//        LccMythicmobsUtilities.getPlugin().getLogger().info(
//                "Generating documentation for mechanics "
//                        + String.join(", ", MechanicsEvent.mechanics.stream().map(Class::getSimpleName).toList()));
//        MechanicsEvent.mechanics.forEach((mechanic) -> {
//            LccMythicmobsUtilities.getPlugin().getLogger().log(java.util.logging.Level.INFO, "Generating docs for mechanic " + mechanic.getName());
//            List<String> components = new ArrayList<>();
//            MechanicEntry annotation = mechanic.getAnnotation(MechanicEntry.class);
//            if (annotation == null) {
//                return;
//            }
//            MythicMechanic data = annotation.data();
//            components.add("### Mechanic: `" + data.name() + "`");
//            components.addAll(generateGeneric(data.description(), annotation.examples(), annotation.fields()));
//            mechanicEntries.add(String.join(System.lineSeparator() + System.lineSeparator(), components));
//        });
//        return String.join(System.lineSeparator() + System.lineSeparator(), mechanicEntries);
//    }
//
//    public static List<String> generateGeneric(String description, String[] examples, MythicField[] fields) {
//        List<String> components = new ArrayList<>();
//        components.add(description);
//        if (examples.length > 0) {
//            components.add("#### Examples");
//            for (String example : examples) {
//                components.add("```yml" + System.lineSeparator() + example + System.lineSeparator() + "```");
//            }
//        }
//        if (fields.length > 0) {
//            components.add("#### Fields");
//            for (MythicField field : fields) {
//                components.addAll(generateField(field));
//            }
//        }
//        return components;
//    }
//
//    public static List<String> generateField(MythicField field) {
//        List<String> components = new ArrayList<>();
//        components.add("- **" + field.name() + "**"
//                + (!field.description().equals("") ? " - " + field.description() : "")
//                + (field.aliases().length != 0 ? "\n\n  Aliases: " + String.join(", ", Arrays.stream(field.aliases()).map(str -> "`" + str + "`").toList()) : "")
//                + (!field.defValue().equals("") ? "\n\n  Default: `" + field.defValue() + "`" : ""));
//        return components;
//    }
}
