package me.tick.lccmythicmobsutilities.models.generic;

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
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;
import me.tick.lccmythicmobsutilities.LccMythicmobsUtilities;
import me.tick.lccmythicmobsutilities.models.generic.fields.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GenericMechanic extends SkillMechanic implements ITargetedEntitySkill, ITargetedLocationSkill {

    List<MlcArg> args = new ArrayList<>();

    private String get(PlaceholderString placeholder, SkillMetadata skillMetadata) {
        return placeholder.get();
    }

    private double get(PlaceholderDouble placeholder, SkillMetadata skillMetadata) {
        return placeholder.get();
    }

    private float get(PlaceholderFloat placeholder, SkillMetadata skillMetadata) {
        return placeholder.get();
    }

    private int get(PlaceholderInt placeholder, SkillMetadata skillMetadata) {
        return placeholder.get();
    }

    private Object get(Object object) {
        if (object instanceof PlaceholderString) {
            return get((PlaceholderString) object, null);
        } else if (object instanceof PlaceholderDouble) {
            return get((PlaceholderDouble) object, null);
        } else if (object instanceof PlaceholderFloat) {
            return get((PlaceholderFloat) object, null);
        } else if (object instanceof PlaceholderInt) {
            return get((PlaceholderInt) object, null);
        } else {
            return object;
        }
    }

    private Method getSmartExecuteMethod() {
        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals("smartExecute")) {
                return method;
            }
        }
        return null;
    }

    public GenericMechanic(SkillExecutor manager, File file, String line, MythicLineConfig mlc) {
        super(manager, file, line, mlc);
        Method method = getSmartExecuteMethod();
        if (method == null) {
            LccMythicmobsUtilities.devLog("Method smartExecute not found in " + this.getClass().getName());
            return;
        }
        Parameter[] params = method.getParameters();
        for (Parameter param : params) {
            if (param.getType().equals(SkillMetadata.class) || param.getType().equals(Targeted.class)) {
                continue;
            }
            String name = param.getName();
            FieldAliases aliasesAnnotation = param.getAnnotation(FieldAliases.class);
            String[] aliases = aliasesAnnotation != null ? aliasesAnnotation.value() : new String[0];
            List<String> names = new ArrayList<>(List.of(aliases));
            names.add(name);
            String[] namesArray = names.toArray(new String[0]);

            if (String.class.equals(param.getType())) {
                FieldDefaultString annotation1 = param.getAnnotation(FieldDefaultString.class);
                String defaultValue = annotation1 != null ? annotation1.value() : "";
                args.add(new MlcArg(param.getType(), mlc.getPlaceholderString(namesArray, defaultValue)));
            } else if (int.class.equals(param.getType())) {
                FieldDefaultInt annotation1 = param.getAnnotation(FieldDefaultInt.class);
                int defaultValue = annotation1 != null ? annotation1.value() : 0;
                args.add(new MlcArg(param.getType(), mlc.getPlaceholderInteger(namesArray, defaultValue)));
            } else if (boolean.class.equals(param.getType())) {
                FieldDefaultBoolean annotation1 = param.getAnnotation(FieldDefaultBoolean.class);
                boolean defaultValue = annotation1 != null ? annotation1.value() : false;
                args.add(new MlcArg(param.getType(), mlc.getBoolean(namesArray, defaultValue)));
            } else if (double.class.equals(param.getType())) {
                FieldDefaultDouble annotation1 = param.getAnnotation(FieldDefaultDouble.class);
                double defaultValue = annotation1 != null ? annotation1.value() : 0.0;
                args.add(new MlcArg(param.getType(), mlc.getPlaceholderDouble(namesArray, defaultValue)));
            } else if (float.class.equals(param.getType())) {
                FieldDefaultFloat annotation1 = param.getAnnotation(FieldDefaultFloat.class);
                float defaultValue = annotation1 != null ? annotation1.value() : 0.0F;
                args.add(new MlcArg(param.getType(), mlc.getPlaceholderFloat(namesArray, defaultValue)));
            } else {
                LccMythicmobsUtilities.devLog("Method " + method.getName() + " has an unsupported parameter type: " + param.getType().getName());
                args.add(null);
            }
            LccMythicmobsUtilities.devLog("Method " + method.getName() + " of class " + this.getClass().getName() + " has parameter " + param.getName() + " with type " + param.getType().getName());
        }
    }

    public SkillResult castAtTarget(SkillMetadata skillMetadata, Targeted targeted) {
        try {
            Method method = getSmartExecuteMethod();
            if (method == null) {
                LccMythicmobsUtilities.devLog("Method smartExecute not found in " + this.getClass().getName());
                return SkillResult.ERROR;
            }
            if (method.getReturnType() == SkillResult.class) {
                List<Object> paramsList = new ArrayList<>();
                paramsList.add(skillMetadata);
                paramsList.add(targeted);
                for (MlcArg arg : args) {
                    paramsList.add(
                            arg.type().cast(get(arg.value()))
                    );
                }
                Object[] params = paramsList.toArray();
                LccMythicmobsUtilities.devLog("Params: " + Arrays.toString(Arrays.stream(params).map(Object::toString).toArray()));
                return (SkillResult) method.invoke(this, params);
            } else {
                LccMythicmobsUtilities.devLog("Method smartExecute() must return SkillResult!");
                return SkillResult.ERROR;
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            LccMythicmobsUtilities.devLog("Error while executing mechanic!");
            e.printStackTrace();
            return SkillResult.ERROR;
        }
    }

    @Override
    public SkillResult castAtLocation(SkillMetadata skillMetadata, AbstractLocation location) {
        return castAtTarget(skillMetadata, new Targeted(location));
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity entity) {
        return castAtTarget(skillMetadata, new Targeted(entity));
    }
}
