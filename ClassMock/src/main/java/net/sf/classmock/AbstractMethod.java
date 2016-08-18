package net.sf.classmock;

import static org.objectweb.asm.Opcodes.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class AbstractMethod {
	
	protected String name;
	protected Class returnType;
	protected Class[] parameters;
	
	protected Map<Class,Annotation> annotations = new HashMap<Class,Annotation>();
	protected Map<Integer,Map<Class,Annotation>> paramAnnotations = new HashMap<Integer,Map<Class,Annotation>>();
	
	public AbstractMethod(String name, Class returnType, Class[] parameters) {
		super();
		this.name = name;
		this.returnType = returnType;
		this.parameters = parameters;
	}
	
	public AbstractMethod() {
		super();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class getReturnType() {
		return returnType;
	}
	public void setReturnType(Class returnType) {
		this.returnType = returnType;
	}
	public Class[] getParameters() {
		return parameters;
	}
	public void setParameters(Class[] parameters) {
		this.parameters = parameters;
	}
	public void addAnnotation(Annotation annotation){
		annotations.put(annotation.getAnnotation(), annotation);
	}
	public void addAnnotationProperty(Class annotation, String name, Object value){
		annotations.get(annotation).addProperty(name, value);
	}
	public void addParamAnnotation(Integer i, Annotation annotation){
		if(!paramAnnotations.containsKey(i))
			paramAnnotations.put(i, new HashMap<Class,Annotation>());
		paramAnnotations.get(i).put(annotation.getAnnotation(), annotation);
	}
	public void addParamAnnotationProperty(Integer i, Annotation annotation, String name, Object value){
		paramAnnotations.get(i).get(annotation.getAnnotation()).addProperty(name, value);
	}
	public void createMethod(ClassWriter cw){
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, name, getMethodSignature(), null, null);
		for(Annotation an : annotations.values()){
				an.createAnnotation(mv);
		}
		for(Integer i : paramAnnotations.keySet()){
			for(Annotation an : paramAnnotations.get(i).values()){
				an.createParameterAnnotation(mv, i);
			}
		}		
		mv.visitEnd();
	}
	public String getMethodSignature(){
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		for(Class paramType : parameters){
			sb.append(Type.getType(paramType).getDescriptor());
		}
		sb.append(")");
		sb.append(Type.getType(returnType).getDescriptor());
		return sb.toString();
	}
	
	public Annotation getAnnotation(Class annotationClass){
		return annotations.get(annotationClass);
	}
	
	public Annotation getParamAnnotation(int param, Class annotationClass){
		return paramAnnotations.get(param).get(annotationClass);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(parameters);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AbstractMethod other = (AbstractMethod) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (!Arrays.equals(parameters, other.parameters))
			return false;
		return true;
	}
	
	public boolean isAbstract(){
		return true;
	}
	
}
