package net.sf.classmock.example.property;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PropertyMapFactory {
	
	public static Map<String,String> getPropertyMap(Object instance){
		Map<String,String> map = new HashMap<String, String>();
		Class clazz = instance.getClass();
		for(Method m : clazz.getMethods()){
			if(m.getName().startsWith("get") && m.getParameterTypes().length == 0 && !m.isAnnotationPresent(Ignore.class)){
				try {
					map.put(acessorToProperty(m.getName()), m.invoke(instance).toString());
				} catch (Exception e) {
					throw new RuntimeException("Problems to invoke method!",e);
				}
			}
		}
		return map;
	}
	
    private static String acessorToProperty(String acessorName){
		int initLetter = 3;
		if(acessorName.startsWith("is")){
			initLetter = 2;
		}
		if(Character.isLowerCase(acessorName.charAt(initLetter+1)))
			return acessorName.substring(initLetter,initLetter+1).toLowerCase()+acessorName.substring(initLetter+1);
		else
			return acessorName.substring(initLetter);
    }

}
