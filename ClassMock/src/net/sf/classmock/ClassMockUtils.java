package net.sf.classmock;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;


public class ClassMockUtils {
	
	public static Object get(Object bean, String property) {
		try {
			if(property.indexOf(".")>=0){
				Object subBean = get(bean, property.substring(0,property.indexOf(".")));
				if(subBean == null)
					return null;
				String newProperty = property.substring(property.indexOf(".")+1);
				return get(subBean, newProperty);
			}
			Method method = null;
			try {
				method = bean.getClass().getMethod(propertyToGetter(property),new Class[]{});
			} catch (NoSuchMethodException e) {
				method = bean.getClass().getMethod(propertyToGetter(property,true),new Class[]{});
			}
			return method.invoke(bean,new Object[]{});
		} catch (Exception e) {
			throw new RuntimeException("Can't get property "+property+" in the class "+ bean.getClass().getName(),e);
		}
	}

	public static void set(Object bean, String property, Object value) {
		try {
			if(property.indexOf(".")>=0){
				String firstProperty = property.substring(0,property.indexOf("."));
				Object subBean = get(bean, firstProperty);
				if(subBean == null){
					subBean = getPropertyClass(bean.getClass(),firstProperty).newInstance();
					set(bean,firstProperty,subBean);
				}	
				String newProperty = property.substring(property.indexOf(".")+1);
				set(subBean, newProperty, value);
				return;
			}
			Method setterMethod = null;
			for(Method method : bean.getClass().getMethods()){
				if(method.getName().equals(propertyToSetter(property))){
					setterMethod = method;
					break;
				}
			}
			if(setterMethod != null){
				Class type = setterMethod.getParameterTypes()[0];
				if(type == null){
					throw new RuntimeException("The setter method of "+ property +" does not have a parameter.");
				}else if(type.isArray() && !type.getComponentType().isPrimitive()){
					Object[] array = (Object[])Array.newInstance(type.getComponentType(),((Object[])value).length);
					for(int i=0;i<array.length;i++){
						array[i] = type.getComponentType().cast(((Object[])value)[i]);
					}
					setterMethod.invoke(bean,new Object[]{array});
				}else{
					if(value instanceof String && (type == Integer.class || type == int.class))
						value = Integer.parseInt((String)value);
					else if(value instanceof String && (type == Long.class || type == long.class))
						value = Long.parseLong((String)value);
					else if(value instanceof String && (type == Byte.class || type == byte.class))
						value = Byte.parseByte((String)value);
					else if(value instanceof String && (type == Short.class || type == short.class))
						value = Short.parseShort((String)value);
					else if(value instanceof String && (type == Float.class || type == float.class))
						value = Float.parseFloat((String)value);
					else if(value instanceof String && (type == Double.class || type == double.class))
						value = Double.parseDouble((String)value);
					else if(value instanceof String && (type == Boolean.class || type == boolean.class))
						value = Boolean.parseBoolean((String)value);
					
					setterMethod.invoke(bean,value);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Can't set property "+property+" in the class "+ bean.getClass().getName());
		}
	}
	
	public static Object invoke(Object instance, String method, Object... parameters) throws Throwable {
		Class clazz = instance.getClass();
		for(Method m : clazz.getMethods()){
			if(m.getName().equals(method)){
				try {
					return m.invoke(instance, parameters);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Can't invoke method",e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Can't invoke method",e);
				} catch (InvocationTargetException e) {
					throw e.getTargetException();
				}
			}
		}
		throw new RuntimeException("Can't find method");
	}
	
	public static Object newInstance(ClassMock classMock){
		try {
			Class clazz = classMock.createClass();
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Can't intanciate class",e);
		}
	}
	
	public static Object invoke(Object instance, String method, Class[] paramTypes, Object... parameters){
		Class clazz = instance.getClass();
		for(Method m : clazz.getMethods()){
			if(m.getName().equals(method) && Arrays.equals(m.getParameterTypes(), paramTypes)){
				try {
					return m.invoke(instance, parameters);
				} catch (Exception e) {
					throw new RuntimeException("Can't invoke method",e);
				}
			}
		}
		throw new RuntimeException("Can't find method");
	}
	
	
	private static Class getPropertyClass(Class beanClass, String property){
		try {
			Class returnType = null;
			
			if(property.indexOf(".")>=0){
				Class subClass =  getPropertyClass(beanClass, property.substring(0,property.indexOf(".")));
				String newProperty = property.substring(property.indexOf(".")+1);
				return getPropertyClass(subClass, newProperty);
			}
			
			try {
				returnType = beanClass.getMethod(propertyToGetter(property),new Class[]{}).getReturnType();
			} catch (NoSuchMethodException e) {
				returnType = beanClass.getMethod(propertyToGetter(property,true),new Class[]{}).getReturnType();
			}
			
			if(returnType == null)
				throw new RuntimeException("Can't find property "+property + " in the class "+ beanClass);
			
			return returnType;
		} catch (Exception e) {
			throw new RuntimeException("Can't find property "+property + " in the class "+ beanClass);
		}
	}
	
	private static String propertyToGetter(String propertieName) {
		return "get"+propertieName.substring(0,1).toUpperCase()+propertieName.substring(1);
	}

	private static String propertyToSetter(String propertieName) {
		return "set"+propertieName.substring(0,1).toUpperCase()+propertieName.substring(1);
	}
	
	private static String propertyToGetter(String propertieName, boolean isBoolean) {
		if(isBoolean)
			return "is"+propertieName.substring(0,1).toUpperCase()+propertieName.substring(1);
		return "get"+propertieName.substring(0,1).toUpperCase()+propertieName.substring(1);
	}

}
