package net.sf.esfinge.classmock.example.property;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PropertyMapFactory {

    public static Map<String, String> getPropertyMap(final Object instance) {

        final Map<String, String> map = new HashMap<>();
        final Class<?> clazz = instance.getClass();

        for (final Method m : clazz.getMethods()) {

            if (m.getName().startsWith("get") && (m.getParameterTypes().length == 0) && !m.isAnnotationPresent(Ignore.class)) {

                try {
                    map.put(PropertyMapFactory.acessorToProperty(m.getName()), m.invoke(instance).toString());
                } catch (final Exception e) {
                    map.put(PropertyMapFactory.acessorToProperty(m.getName()), null);
                }
            }
        }

        return map;
    }

    private static String acessorToProperty(final String acessorName) {

        int initLetter = 3;

        if (acessorName.startsWith("is")) {
            initLetter = 2;
        }

        if (Character.isLowerCase(acessorName.charAt(initLetter + 1))) {
            return acessorName.substring(initLetter, initLetter + 1).toLowerCase() + acessorName.substring(initLetter + 1);
        } else {
            return acessorName.substring(initLetter);
        }
    }
}