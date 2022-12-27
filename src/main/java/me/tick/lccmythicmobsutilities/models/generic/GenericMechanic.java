package me.tick.lccmythicmobsutilities.models.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GenericMechanic {

    public String[] names = new String[0];
    public String description;
    public String author;
    public String[] examples;

    public void setNames(String[] names) {
        this.names = Arrays.stream(names).map(String::toLowerCase).toArray(String[]::new);
    }
    public void addName(String name) {
        List<String> names = new ArrayList<>(Arrays.asList(this.names));
        names.add(name.toLowerCase());
        this.names = names.toArray(new String[0]);
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setExamples(String[] examples) {
        this.examples = examples;
    }
    public void addExample(String example) {
        List<String> examples = new ArrayList<>(Arrays.asList(this.examples));
        examples.add(example);
        this.examples = examples.toArray(new String[0]);
    }
}
