package me.tick.lccmythicmobsutilities.models.generic.generators;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.ITargetedLocationSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.placeholders.PlaceholderDouble;
import io.lumine.mythic.api.skills.placeholders.PlaceholderFloat;
import io.lumine.mythic.api.skills.placeholders.PlaceholderInt;
import io.lumine.mythic.api.skills.placeholders.PlaceholderString;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;
import me.tick.lccmythicmobsutilities.LccMythicmobsUtilities;
import me.tick.lccmythicmobsutilities.models.generic.GenericMechanic;
import me.tick.lccmythicmobsutilities.models.generic.Targeted;
import me.tick.lccmythicmobsutilities.models.generic.fields.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MechanicGenerator {

    private Object get(Object object) {
        if (object instanceof PlaceholderString) {
            return ((PlaceholderString) object).get();
        } else if (object instanceof PlaceholderDouble) {
            return ((PlaceholderDouble) object).get();
        } else if (object instanceof PlaceholderFloat) {
            return ((PlaceholderFloat) object).get();
        } else if (object instanceof PlaceholderInt) {
            return ((PlaceholderInt) object).get();
        } else {
            return object;
        }
    }

    public static class FieldData<T> {
        public String[] names;
        public Class<T> type;
        public T defaultValue;
        public String description;
        public Function<MythicLineConfig, Object> placeholderGetter;

        public FieldData() {
        }

        public FieldData(String[] names, Class<T> type, T defaultValue, String description) {
            this.names = names;
            this.type = type;
            this.defaultValue = defaultValue;
            this.description = description;
        }

        public FieldData<T> setNames(String[] names) {
            this.names = names;
            return this;
        }

        public FieldData<T> addName(String name) {
            List<String> names = new ArrayList<>(Arrays.asList(this.names));
            names.add(name);
            this.names = names.toArray(new String[0]);
            return this;
        }

        public FieldData<T> setType(Class<T> type) {
            this.type = type;
            return this;
        }

        public FieldData<T> setDefaultValue(T defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public FieldData<T> setDescription(String description) {
            this.description = description;
            return this;
        }

        public FieldData<T> setPlaceholderGetter(Function<MythicLineConfig, Object> placeholderGetter) {
            this.placeholderGetter = placeholderGetter;
            return this;
        }
    }

    public List<FieldData<?>> fields = new ArrayList<>();
    public List<Object> fieldValues = new ArrayList<>();

    public Class<? extends GenericMechanic> clazz;
    public GenericMechanic instance;

    public MechanicGenerator(GenericMechanic instance) {
        this.clazz = instance.getClass();
        this.instance = instance;
        generateArgs();
    }

    private Method getSmartExecuteMethod() {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (
                    method.getName().equals("smartExecute")
                            && method.getParameterCount() >= 2
                            && method.getParameterTypes()[0].equals(SkillMetadata.class)
                            && method.getParameterTypes()[1].equals(Targeted.class)
                            && method.getReturnType().equals(SkillResult.class)
            ) {
                return method;
            }
        }
        return null;
    }

    private void generateArgs() {
        Method method = getSmartExecuteMethod();
        if (method == null) {
            LccMythicmobsUtilities.error("Could not find execute method for " + clazz.getName());
            return;
        }
        Parameter[] params = method.getParameters();
        for (Parameter param : params) {
            if (param.getType().equals(SkillMetadata.class) || param.getType().equals(Targeted.class)) {
                continue;
            }
            String[] names = param.getAnnotation(FieldNames.class).value();

            if (String.class.equals(param.getType())) {
                FieldDefaultString annotation1 = param.getAnnotation(FieldDefaultString.class);
                String defaultValue = annotation1 != null ? annotation1.value() : "";
                fields.add(new FieldData<String>().setNames(names).setType(String.class).setDefaultValue(defaultValue)
                        .setPlaceholderGetter(mlc -> mlc.getPlaceholderString(names, defaultValue)));
            } else if (int.class.equals(param.getType())) {
                FieldDefaultInt annotation1 = param.getAnnotation(FieldDefaultInt.class);
                int defaultValue = annotation1 != null ? annotation1.value() : 0;
                fields.add(new FieldData<Integer>().setNames(names).setType(Integer.class).setDefaultValue(defaultValue)
                        .setPlaceholderGetter(mlc -> mlc.getPlaceholderInteger(names, defaultValue)));
            } else if (boolean.class.equals(param.getType())) {
                FieldDefaultBoolean annotation1 = param.getAnnotation(FieldDefaultBoolean.class);
                boolean defaultValue = annotation1 != null ? annotation1.value() : false;
                fields.add(new FieldData<Boolean>().setNames(names).setType(Boolean.class).setDefaultValue(defaultValue)
                        .setPlaceholderGetter(mlc -> mlc.getBoolean(names, defaultValue)));
            } else if (double.class.equals(param.getType())) {
                FieldDefaultDouble annotation1 = param.getAnnotation(FieldDefaultDouble.class);
                double defaultValue = annotation1 != null ? annotation1.value() : 0.0;
                fields.add(new FieldData<Double>().setNames(names).setType(Double.class).setDefaultValue(defaultValue)
                        .setPlaceholderGetter(mlc -> mlc.getPlaceholderDouble(names, defaultValue)));
            } else if (float.class.equals(param.getType())) {
                FieldDefaultFloat annotation1 = param.getAnnotation(FieldDefaultFloat.class);
                float defaultValue = annotation1 != null ? annotation1.value() : 0.0F;
                fields.add(new FieldData<Float>().setNames(names).setType(Float.class).setDefaultValue(defaultValue)
                        .setPlaceholderGetter(mlc -> mlc.getPlaceholderFloat(names, defaultValue)));
            } else {
                LccMythicmobsUtilities.devLog("Method " + method.getName() + " has an unsupported parameter type: " + param.getType().getName());
                fields.add(null);
            }
        }
    }

    public void register(MythicMechanicLoadEvent event) {
        SkillExecutor executor = event.getContainer().getManager();
        File file = event.getContainer().getFile();
        String line = event.getConfig().getLine();
        MythicLineConfig mlc = event.getConfig();
        if (Arrays.stream(instance.names).toList().contains(event.getMechanicName())) {
            event.register(generate(executor, file, line, mlc));
        }
    }

    public SkillMechanic generate(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
        Method method = getSmartExecuteMethod();
        if (method == null) {
            LccMythicmobsUtilities.error("Could not find execute method in " + clazz.getName() + "! Aborting generation.");
            return null;
        }
        class Target extends SkillMechanic implements ITargetedLocationSkill, ITargetedEntitySkill {
            public Target(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
                super(manager, file, line, mlc);
                for (FieldData<?> field : fields) {
                    if (field == null) {
                        fieldValues.add(null);
                        continue;
                    }
                    fieldValues.add(field.placeholderGetter.apply(mlc));
                }
            }

            public SkillResult execute(SkillMetadata skillMetadata, Targeted target) {
                List<Object> paramsList = new ArrayList<>();
                paramsList.add(skillMetadata);
                paramsList.add(target);
                for (Object fieldValue : fieldValues) {
                    paramsList.add(get(fieldValue));
                }
                try {
                    return (SkillResult) method.invoke(this, paramsList.toArray());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LccMythicmobsUtilities.error(e);
                    return SkillResult.ERROR;
                }
            }

            @Override
            public SkillResult castAtLocation(SkillMetadata skillMetadata, AbstractLocation location) {
                return execute(skillMetadata, new Targeted(location));
            }

            @Override
            public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity entity) {
                return execute(skillMetadata, new Targeted(entity));
            }
        }
        return new Target(manager, file, line, mlc);
    }

}
