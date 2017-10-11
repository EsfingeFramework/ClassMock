package net.sf.esfinge.classmock;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import net.sf.esfinge.classmock.api.IClassWriter;

public class ClassMockUtils {

    public static Object get(final Object bean, final String property) {

        try {
            if (property.indexOf(".") >= 0) {

                final Object subBean = ClassMockUtils.get(bean, property.substring(0, property.indexOf(".")));

                if (subBean == null) {

                    return null;
                }

                final String newProperty = property.substring(property.indexOf(".") + 1);

                return ClassMockUtils.get(subBean, newProperty);
            }

            Method method = null;

            try {
                method = bean.getClass().getMethod(ClassMockUtils.propertyToGetter(property), new Class[] {});
            } catch (final NoSuchMethodException e) {
                method = bean.getClass().getMethod(ClassMockUtils.propertyToGetter(property, true), new Class[] {});
            }

            return method.invoke(bean, new Object[] {});

        } catch (final Exception e) {

            throw new RuntimeException("Can't get property " + property + " in the class " + bean.getClass().getName(), e);
        }
    }

    public static void set(final Object bean, final String property, Object value) {

        try {
            if (property.indexOf(".") >= 0) {

                final String firstProperty = property.substring(0, property.indexOf("."));
                Object subBean = ClassMockUtils.get(bean, firstProperty);

                if (subBean == null) {

                    subBean = ClassMockUtils.getPropertyClass(bean.getClass(), firstProperty).newInstance();
                    ClassMockUtils.set(bean, firstProperty, subBean);
                }

                final String newProperty = property.substring(property.indexOf(".") + 1);
                ClassMockUtils.set(subBean, newProperty, value);

                return;
            }

            Method setterMethod = null;

            for (final Method method : bean.getClass().getMethods()) {

                if (method.getName().equals(ClassMockUtils.propertyToSetter(property))) {

                    setterMethod = method;
                    break;
                }
            }

            if (setterMethod != null) {

                final Class<?> type = setterMethod.getParameterTypes()[0];

                if (type == null) {

                    throw new RuntimeException("The setter method of " + property + " does not have a parameter.");

                } else if (type.isArray() && !type.getComponentType().isPrimitive()) {

                    final Object[] array = (Object[]) Array.newInstance(type.getComponentType(), ((Object[]) value).length);

                    for (int i = 0; i < array.length; i++) {

                        array[i] = type.getComponentType().cast(((Object[]) value)[i]);
                    }

                    setterMethod.invoke(bean, new Object[] { array });

                } else {

                    if ((value instanceof String) && ((type == Integer.class) || (type == int.class))) {
                        value = Integer.parseInt((String) value);
                    } else if ((value instanceof String) && ((type == Long.class) || (type == long.class))) {
                        value = Long.parseLong((String) value);
                    } else if ((value instanceof String) && ((type == Byte.class) || (type == byte.class))) {
                        value = Byte.parseByte((String) value);
                    } else if ((value instanceof String) && ((type == Short.class) || (type == short.class))) {
                        value = Short.parseShort((String) value);
                    } else if ((value instanceof String) && ((type == Float.class) || (type == float.class))) {
                        value = Float.parseFloat((String) value);
                    } else if ((value instanceof String) && ((type == Double.class) || (type == double.class))) {
                        value = Double.parseDouble((String) value);
                    } else if ((value instanceof String) && ((type == Boolean.class) || (type == boolean.class))) {
                        value = Boolean.parseBoolean((String) value);
                    }

                    setterMethod.invoke(bean, value);
                }
            }
        } catch (final Exception e) {

            throw new RuntimeException("Can't set property " + property + " in the class " + bean.getClass().getName());
        }
    }

    public static Object invoke(final Object instance, final String method, final Object... parameters) throws Throwable {

        final Class<?> clazz = instance.getClass();

        for (final Method m : clazz.getMethods()) {

            if (m.getName().equals(method)) {

                try {
                    return m.invoke(instance, parameters);
                } catch (final IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException("Can't invoke method", e);
                } catch (final InvocationTargetException e) {
                    throw e.getTargetException();
                }
            }
        }
        throw new RuntimeException("Can't find method");
    }

    public static Object newInstance(final IClassWriter classMock) {

        try {

            final Class<?> clazz = classMock.build();

            return clazz.newInstance();

        } catch (final Exception e) {

            throw new RuntimeException("Can't intanciate class", e);
        }
    }

    public static Object invoke(final Object instance, final String method, final Class<?>[] paramTypes, final Object... parameters) {

        final Class<?> clazz = instance.getClass();

        for (final Method m : clazz.getMethods()) {

            if (m.getName().equals(method) && Arrays.equals(m.getParameterTypes(), paramTypes)) {

                try {
                    return m.invoke(instance, parameters);
                } catch (final Exception e) {
                    throw new RuntimeException("Can't invoke method", e);
                }
            }
        }
        throw new RuntimeException("Can't find method");
    }

    private static Class<?> getPropertyClass(final Class<?> beanClass, final String property) {

        try {
            Class<?> returnType = null;

            if (property.indexOf(".") >= 0) {

                final Class<?> subClass = ClassMockUtils.getPropertyClass(beanClass, property.substring(0, property.indexOf(".")));
                final String newProperty = property.substring(property.indexOf(".") + 1);

                return ClassMockUtils.getPropertyClass(subClass, newProperty);
            }

            try {
                returnType = beanClass.getMethod(ClassMockUtils.propertyToGetter(property), new Class[] {}).getReturnType();
            } catch (final NoSuchMethodException e) {
                returnType = beanClass.getMethod(ClassMockUtils.propertyToGetter(property, true), new Class[] {}).getReturnType();
            }

            if (returnType == null) {

                throw new RuntimeException("Can't find property " + property + " in the class " + beanClass);
            }

            return returnType;

        } catch (final Exception e) {

            throw new RuntimeException("Can't find property " + property + " in the class " + beanClass);
        }
    }

    private static String propertyToGetter(final String propertieName) {

        return "get" + propertieName.substring(0, 1).toUpperCase() + propertieName.substring(1);
    }

    private static String propertyToSetter(final String propertieName) {

        return "set" + propertieName.substring(0, 1).toUpperCase() + propertieName.substring(1);
    }

    private static String propertyToGetter(final String propertieName, final boolean isBoolean) {

        if (isBoolean) {

            return "is" + propertieName.substring(0, 1).toUpperCase() + propertieName.substring(1);
        }

        return "get" + propertieName.substring(0, 1).toUpperCase() + propertieName.substring(1);
    }
}