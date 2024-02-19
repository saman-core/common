package io.samancore.common.transformer;

import lombok.NonNull;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.logging.Logger;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class GenericTransformer<ENTITY, MODEL> {
    private static final Logger log = Logger.getLogger(GenericTransformer.class);
    public static final String SET = "set";
    private final Class<ENTITY> classEntity;
    private final Class<MODEL> classModel;
    private final List<Field> fieldsEntityValid;
    private final Map<Field, Field> fieldsEntityInModelMap;
    private final Map<Field, Field> fieldsModelInEntityMap;
    private final Map<String, Method> methodsToSet;
    private final Method methodBuildInModelBuilder;

    public GenericTransformer() {
        try {
            var type = this.getClass().getGenericSuperclass();
            ParameterizedType parameterizedType;
            if (type instanceof ParameterizedType) {
                parameterizedType = (ParameterizedType) type;
            } else {
                parameterizedType = (ParameterizedType) ((Class<?>) type).getGenericSuperclass();
            }

            @SuppressWarnings("unchecked")
            var classEntity = (Class<ENTITY>) parameterizedType.getActualTypeArguments()[0];
            this.classEntity = classEntity;
            @SuppressWarnings("unchecked")
            var classModel = (Class<MODEL>) parameterizedType.getActualTypeArguments()[1];
            this.classModel = classModel;
            var classModelBuilder = newBuilder().getClass();
            var fieldsEntity = getAllEntityFields(this.classEntity);
            var fieldsModelBuilder = getAllEntityFields(classModelBuilder);

            methodBuildInModelBuilder = Arrays.stream(classModelBuilder.getDeclaredMethods()).filter(method0 -> method0.getName().equals("build")).findAny().get();

            this.fieldsEntityInModelMap = fieldsEntity.stream()
                    .filter(fieldEntity -> fieldsModelBuilder.stream().anyMatch(fieldModel0 -> fieldModel0.getName().equalsIgnoreCase(fieldEntity.getName())))
                    .peek(field -> field.setAccessible(true))
                    .collect(Collectors.toMap(field -> field, field -> fieldsModelBuilder.stream().filter(fieldModel0 -> fieldModel0.getName().equalsIgnoreCase(field.getName())).findAny().get()));

            this.fieldsModelInEntityMap = fieldsModelBuilder.stream()
                    .filter(fieldModel -> fieldsEntity.stream().anyMatch(fieldEntity0 -> fieldEntity0.getName().equalsIgnoreCase(fieldModel.getName())))
                    .peek(field -> field.setAccessible(true))
                    .collect(Collectors.toMap(field -> field, field -> fieldsEntity.stream().filter(fieldModel0 -> fieldModel0.getName().equalsIgnoreCase(field.getName())).findAny().get()));

            this.fieldsEntityValid = fieldsEntity.stream()
                    .filter(field -> !Modifier.isFinal(field.getModifiers()) && isValidName(field))
                    .peek(field -> field.setAccessible(true))
                    .collect(Collectors.toList());
            var declaredMethods = Arrays.stream(classEntity.getDeclaredMethods()).collect(Collectors.toMap(Method::getName, method -> method));
            methodsToSet = fieldsEntityValid.stream().collect(Collectors.toMap(Field::getName, field -> declaredMethods.get(getMethodNameToSet(field.getName()))));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    protected String getMethodNameToSet(String text) {
        return SET.concat(text.substring(0, 1).toUpperCase(Locale.ROOT).concat(text.substring(1)));
    }

    public ENTITY transformCopyToAttached(@NonNull ENTITY detached, @NonNull ENTITY attached) {
        fieldsEntityValid.forEach(field -> {
            try {
                var value = field.get(detached);
                if (value != null) {
                    var method = methodsToSet.get(field.getName());
                    method.invoke(attached, value);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.warnf("Copy Entity Field fail, field: %s", field.getName());
            }
        });
        return attached;
    }

    @SafeVarargs
    public final ENTITY transformToEntity(@NonNull MODEL model, @NonNull Pair<String, ? extends Function<?, ?>>... pairs) {
        return transformToEntity(toBuilder(model), newEntityInstance(), pairs);
    }

    @SafeVarargs
    public final ENTITY transformToEntity(@NonNull Object modelBuilder, @NonNull ENTITY entity, @NonNull Pair<String, ? extends Function<?, ?>>... pairs) {
        var functionsName = Arrays.stream(pairs).map(Pair::getKey).toList();
        fieldsModelInEntityMap.forEach((fieldModel, fieldEntity) -> {
            try {
                var objectValue = fieldModel.get(modelBuilder);
                if (objectValue != null) {
                    Object newValue = transformValue(functionsName, fieldModel.getName(), objectValue, pairs);
                    fieldEntity.set(entity, newValue);
                } else {
                    log.warnf("objectValue has no value for field: %s", fieldModel.getName());
                }
            } catch (IllegalAccessException e) {
                log.warnf("Set Entity Field fail, field: %s", fieldModel.getName());
            }
        });
        return entity;
    }

    protected ENTITY newEntityInstance() {
        try {
            Constructor<ENTITY> constructor = this.classEntity.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    protected Object newBuilder() {
        try {
            var methodModel = this.classModel.getDeclaredMethod("newBuilder");
            return methodModel.invoke(null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    protected Object toBuilder(MODEL model) {
        try {
            var methodModel = model.getClass().getDeclaredMethod("toBuilder");
            return methodModel.invoke(model);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public final MODEL transformToModel(@NonNull ENTITY entity, @NonNull MODEL model, Pair<String, ? extends Function<?, ?>>[] pairs) {
        return transformToModel_(entity, toBuilder(model), pairs);
    }

    @SafeVarargs
    public final MODEL transformToModel(@NonNull ENTITY entity, @NonNull Pair<String, ? extends Function<?, ?>>... pairs) {
        return transformToModel_(entity, newBuilder(), pairs);
    }

    private MODEL transformToModel_(@NonNull ENTITY entity, @NonNull Object modelBuilder, Pair<String, ? extends Function<?, ?>>[] pairs) {
        var functionsName = Arrays.stream(pairs).map(Pair::getKey).toList();
        fieldsEntityInModelMap.forEach((fieldEntity, fieldModel) -> {
            try {
                var objectValue = fieldEntity.get(entity);
                if (objectValue != null) {
                    Object newValue = transformValue(functionsName, fieldEntity.getName(), objectValue, pairs);
                    fieldModel.set(modelBuilder, newValue);
                } else {
                    log.warnf("objectValue has no value for field: %s", fieldModel.getName());
                }
            } catch (Exception e) {
                log.warnf("Set Entity Field fail, field: %s", fieldEntity.getName());
            }
        });
        return build(modelBuilder);
    }

    protected MODEL build(Object modelbuilder) {
        try {
            return (MODEL) methodBuildInModelBuilder.invoke(modelbuilder);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @SafeVarargs
    public final List<ENTITY> toEntityList(List<MODEL> models, @NonNull Pair<String, ? extends Function<?, ?>>... pairs) {
        return models == null ? null :
                models.stream().map(model -> {
                    try {
                        return transformToEntity(model, pairs);
                    } catch (Exception exception) {
                        log.error(exception.getMessage(), exception);
                        throw new RuntimeException(exception);
                    }
                }).toList();
    }

    @SafeVarargs
    public final List<MODEL> toModelList(List<ENTITY> entityList, @NonNull Pair<String, ? extends Function<?, ?>>... pairs) {
        return entityList == null ? null :
                entityList.stream().map(entity -> {
                    try {
                        return transformToModel(entity, pairs);
                    } catch (Exception exception) {
                        log.error(exception.getMessage(), exception);
                        throw new RuntimeException(exception);
                    }
                }).toList();
    }

    protected static Object transformValue(List<String> functionsName, String fieldName, Object objectValue, Pair<String, ? extends Function<?, ?>>[] pairs) {
        Object newValue;
        if (functionsName.contains(fieldName)) {
            newValue = transformWithSpecificFunction(fieldName, objectValue, pairs);
        } else {
            newValue = objectValue;
        }
        return newValue;
    }


    protected static Object transformWithSpecificFunction(String fieldName, Object value, Pair<String, ? extends Function<?, ?>>[] pairs) {
        for (Pair<String, ? extends Function<?, ?>> pair : pairs) {
            if (fieldName.equals(pair.getKey())) {
                @SuppressWarnings("unchecked")
                var functionSearched = (Function<Object, ?>) pair.getValue();
                return functionSearched.apply(value);
            }
        }
        return value;
    }

    protected static boolean isValidName(Field field) {
        return !field.getName().contains("$");
    }

    protected static List<Field> getAllEntityFields(Class<?> aClass) {
        List<Field> fields = new ArrayList<>();
        Collections.addAll(fields, aClass.getDeclaredFields());
        return fields;
    }
}