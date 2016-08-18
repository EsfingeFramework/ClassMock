package net.sf.classmock;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.*;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class ClassMock{
	
	private static Map<String,Integer> cache = new HashMap<String, Integer>();
	
	private String name;
	private String realName;
	private boolean isInterface = false;
	private Map<String,Property> properties = new HashMap<String,Property>();
	private Map<Class,Annotation> annotations = new HashMap<Class,Annotation>();
	private Set<AbstractMethod> methods = new HashSet<AbstractMethod>();
	private Class superclass = Object.class;
	private List<Class> interfaces = new ArrayList<Class>();
	
	private Class cacheClass;
	
	public ClassMock(String name) {
		this.name = name;
	}
	
	public ClassMock(String name, boolean isInterface) {
		this.name = name;
		this.isInterface = isInterface;
	}
	
	private String getClassName(){
		if(realName != null)
			return realName;
		else{
			if(cache.containsKey(name)){
				cache.put(name, cache.get(name)+1);
				return name+"_"+cache.get(name);
			}else{
				cache.put(name, 1);
				return name+"_1";
			}
		}
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public ClassMock addProperty(String name, Class type){
		Property prop = new Property(type, name, true, true);
		properties.put(name,prop);
		return this;
	}
	
	public ClassMock addPropertyReadOnly(String name, Class type){
		Property prop = new Property(type, name, false, true);
		properties.put(name,prop);
		return this;
	}
	
	public ClassMock addProperty(String name, Class type, boolean hasSetter,
			boolean hasGetter){
		Property prop = new Property(type, name, hasSetter, hasGetter);
		properties.put(name,prop);
		return this;
	}
	public ClassMock addAnnotation(String property, Annotation annotation){
		properties.get(property).addAnnotation(annotation);
		return this;
	}
	public ClassMock addAnnotation(String property, Class annotationClass){
		Annotation annotation =  new Annotation(annotationClass);
		properties.get(property).addAnnotation(annotation);
		return this;
	}
	public ClassMock addAnnotation(String property, Class annotationClass, Location location){
		Annotation annotation =  new Annotation(annotationClass,location);
		properties.get(property).addAnnotation(annotation);
		return this;
	}
	public ClassMock addAnnotation(String property, Class annotationClass, Location location, Object value){
		Annotation annotation =  new Annotation(annotationClass,location);
		annotation.addProperty("value", value);
		properties.get(property).addAnnotation(annotation);
		return this;
	}
	public ClassMock addAnnotation(String property, Class annotationClass, Object value){
		Annotation annotation =  new Annotation(annotationClass);
		annotation.addProperty("value", value);
		properties.get(property).addAnnotation(annotation);
		return this;
	}
	public ClassMock addAnnotation(String property, Class annotationClass, String annotationProperty, Object value){
		Annotation annotation =  new Annotation(annotationClass);
		annotation.addProperty(annotationProperty, value);
		properties.get(property).addAnnotation(annotation);
		return this;
	}
	public ClassMock addAnnotation(String property, Class annotationClass, Location location, String annotationProperty, Object value){
		Annotation annotation =  new Annotation(annotationClass,location);
		annotation.addProperty(annotationProperty, value);
		properties.get(property).addAnnotation(annotation);
		return this;
	}
	public ClassMock addAnnotationProperty(String property, Class annotationClass,String annotationProperty, Object value){
		properties.get(property).addAnnotationProperty(annotationClass, annotationProperty, value);
		return this;
	}
	public ClassMock addAnnotationProperty(String property, Class annotationClass, Object value){
		properties.get(property).addAnnotationProperty(annotationClass, "value", value);
		return this;
	}
	public ClassMock addAnnotation(Annotation annotation){
		annotations.put(annotation.getAnnotation(), annotation);
		return this;
	}
	public ClassMock addAnnotation(Class annotationClass){
		Annotation annotation =  new Annotation(annotationClass);
		annotations.put(annotation.getAnnotation(), annotation);
		return this;
	}
	public ClassMock addAnnotation(Class annotationClass, Object value){
		Annotation annotation =  new Annotation(annotationClass);
		annotation.addProperty("value", value);
		annotations.put(annotation.getAnnotation(), annotation);
		return this;
	}
	public ClassMock addAnnotation(Class annotationClass, String annotationProperty, Object value){
		Annotation annotation =  new Annotation(annotationClass);
		annotation.addProperty(annotationProperty, value);
		annotations.put(annotation.getAnnotation(), annotation);
		return this;
	}
	public ClassMock addAnnotationProperty(Class annotationClass,String annotationProperty, Object value){
		annotations.get(annotationClass).addProperty(annotationProperty, value);
		return this;
	}
	public ClassMock addAnnotationProperty(Class annotationClass, Object value){
		annotations.get(annotationClass).addProperty("value", value);
		return this;
	}
	public ClassMock addAbstractMethod(Class returnType, String name, Class... params){
		methods.add(new AbstractMethod(name,returnType,params));
		return this;
	}
	public ClassMock addMethod(Class returnType, String name, Class... params){
		methods.add(new Method(name,returnType,params));
		return this;
	}
	public ClassMock addMethodAnnotation(String methodName, Annotation an){
		for(AbstractMethod am : methods){
			if(am.getName().equals(methodName)){
				am.addAnnotation(an);
			}
		}
		return this;
	}
	public ClassMock addMethodAnnotation(String methodName, Annotation an, Class... params){
		for(AbstractMethod am : methods){
			if(am.getName().equals(methodName) && Arrays.equals(am.getParameters(),params)){
				am.addAnnotation(an);
			}
		}
		return this;
	}
	public ClassMock addMethodAnnotation(String methodName, Class anClass){
		Annotation an = new Annotation(anClass);
		addMethodAnnotation(methodName, an);
		return this;
	}
	public ClassMock addMethodAnnotation(String methodName, Class anClass, Object value){
		Annotation an = new Annotation(anClass,value);
		addMethodAnnotation(methodName, an);
		return this;
	}
	public ClassMock addMethodAnnotation(String methodName, Class anClass, String property, Object value){
		Annotation an = new Annotation(anClass,property,value);
		addMethodAnnotation(methodName, an);
		return this;
	}
	public ClassMock addMethodAnnotation(String methodName, Class anClass, Class... params){
		Annotation an = new Annotation(anClass);
		addMethodAnnotation(methodName, an, params);
		return this;
	}
	public ClassMock addMethodAnnotation(String methodName, Class anClass, Object value, Class... params){
		Annotation an = new Annotation(anClass,value);
		addMethodAnnotation(methodName, an, params);
		return this;
	}
	public ClassMock addMethodAnnotation(String methodName, Class anClass, String property, Object value, Class... params){
		Annotation an = new Annotation(anClass,property,value);
		addMethodAnnotation(methodName, an, params);
		return this;
	}
	public ClassMock addMethodAnnotationProperty(String methodName, Class anClass, String property, Object value, Class... params){
		for(AbstractMethod am : methods){
			if(am.getName().equals(methodName) && Arrays.equals(am.getParameters(),params)){
				am.getAnnotation(anClass).addProperty(property, value);
			}
		}
		return this;
	}
	public ClassMock addMethodAnnotationProperty(String methodName, Class anClass, String property, Object value){
		for(AbstractMethod am : methods){
			if(am.getName().equals(methodName)){
				am.getAnnotation(anClass).addProperty(property, value);
			}
		}
		return this;
	}
	public ClassMock addMethodParamAnnotationProperty(int param, String methodName, Class anClass, String property, Object value, Class... params){
		for(AbstractMethod am : methods){
			if(am.getName().equals(methodName) && Arrays.equals(am.getParameters(),params)){
				am.getParamAnnotation(param, anClass).addProperty(property, value);
			}
		}
		return this;
	}
	public ClassMock addMethodParamAnnotationProperty(int param, String methodName, Class anClass, String property, Object value){
		for(AbstractMethod am : methods){
			if(am.getName().equals(methodName)){
				am.getParamAnnotation(param, anClass).addProperty(property, value);
			}
		}
		return this;
	}
	public ClassMock addMethodParamAnnotation(int param,String methodName, Annotation an){
		for(AbstractMethod am : methods){
			if(am.getName().equals(methodName)){
				am.addParamAnnotation(param, an);
			}
		}
		return this;
	}
	public ClassMock addMethodParamAnnotation(int param, String methodName, Annotation an, Class... params){
		for(AbstractMethod am : methods){
			if(am.getName().equals(methodName) && Arrays.equals(am.getParameters(),params)){
				am.addParamAnnotation(param, an);
			}
		}
		return this;
	}
	public ClassMock addMethodParamAnnotation(int param, String methodName, Class anClass){
		Annotation an = new Annotation(anClass);
		addMethodParamAnnotation(param, methodName, an);
		return this;
	}
	public ClassMock addMethodParamAnnotation(int param, String methodName, Class anClass, Object value){
		Annotation an = new Annotation(anClass,value);
		addMethodParamAnnotation(param, methodName, an);
		return this;
	}
	public ClassMock addMethodParamAnnotation(int param, String methodName, Class anClass, String property, Object value){
		Annotation an = new Annotation(anClass,property,value);
		addMethodParamAnnotation(param, methodName, an);
		return this;
	}
	public ClassMock addMethodParamAnnotation(int param, String methodName, Class anClass, Class... params){
		Annotation an = new Annotation(anClass);
		addMethodParamAnnotation(param, methodName, an, params);
		return this;
	}
	public ClassMock addMethodParamAnnotation(int param, String methodName, Class anClass, Object value, Class... params){
		Annotation an = new Annotation(anClass,value);
		addMethodParamAnnotation(param, methodName, an, params);
		return this;
	}
	public ClassMock addMethodParamAnnotation(int param, String methodName, Class anClass, String property, Object value, Class... params){
		Annotation an = new Annotation(anClass,property,value);
		addMethodParamAnnotation(param, methodName, an, params);
		return this;
	}
	public Class getSuperclass() {
		return superclass;
	}
	public ClassMock setSuperclass(Class superclass) {
		this.superclass = superclass;
		return this;
	}
	public List<Class> getInterfaces() {
		return interfaces;
	}
	public ClassMock addInterface(Class interf){
		if(interf.isInterface()){
			interfaces.add(interf);
		}else{
			throw new IllegalArgumentException("The parameter must be an interface");
		}
		return this;
	}

	private String[] getInterfacesNames(){
	   String[] inames = new String[this.interfaces.size()];
	   for (int i = 0; i < inames.length; i++) {
		  inames[i] = Type.getType(interfaces.get(i)).getInternalName();
	   }
	   return inames;
	}
	
	private boolean isAbstract(){
		for(AbstractMethod m : methods){
			if(m.isAbstract())
				return true;
		}			
		if(Modifier.isAbstract(superclass.getModifiers()))
			return true;
		for(Class interf : interfaces){
			if(interf.getMethods().length >0){
				return true;
			}
		}
		return false;
	}
	
	public Class createClass(){
		
		if(cacheClass != null)
			return cacheClass;
		
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		String[] interfaceNames = null;
		
		if(interfaces.size() > 0)
			interfaceNames = getInterfacesNames();
		
		String name = getClassName();
		
		if(isInterface){
			cw.visit(V1_5,ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE , name , null, "java/lang/Object",interfaceNames);
		}else{
			int modificadores = ACC_PUBLIC + ACC_SUPER; 
			if(isAbstract())
				modificadores += ACC_ABSTRACT;
			cw.visit(V1_5,modificadores , name , null, Type.getType(superclass).getInternalName(),interfaceNames);
		}
			
		for(Annotation an : annotations.values()){
			an.createAnnotation(cw);
		}

		if (!isInterface) {
			createConstructor(cw);
			for (Property prop : properties.values()) {
				prop.createProperty(cw, name);
			}
		}
		
		for(AbstractMethod ab : methods){
			ab.createMethod(cw);
		}
		
		cw.visitEnd();
		
		MockClassLoader cl = MockClassLoader.getInstance();
		cacheClass = cl.defineClass(name, cw.toByteArray());
		return cacheClass;
	}

	private void createConstructor(ClassWriter cw) {
		MethodVisitor mv;
		mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, Type.getType(superclass)
				.getInternalName(), "<init>", "()V");
		mv.visitInsn(RETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}

}
