package net.sf.classmock;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class Annotation {
	
	private Class annotation;
	private Map<String,Object> properties = new HashMap<String, Object>();
	private Location location;
	
	public Annotation(Class annotation, Location location) {
		this.annotation = annotation;
		this.location = location;
	}
	public Annotation(Class annotation) {
		this.annotation = annotation;
	}
	public Annotation(Class annotation, Object value) {
		this.annotation = annotation;
		addProperty(value);
	}
	public Annotation(Class annotation, String property, Object value) {
		this.annotation = annotation;
		addProperty(property, value);
	}
	public Class getAnnotation() {
		return annotation;
	}
	public void setAnnotation(Class annotation) {
		this.annotation = annotation;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public void addProperty(String name, Object value){
		properties.put(name,value);
	}
	public void addProperty(Object value){
		properties.put("value",value);
	}
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public void createAnnotation(ClassVisitor cv){
		AnnotationVisitor av = cv.visitAnnotation(getAnnotationType(), true);
		createAnnotation(av);
	}
	
	public void createAnnotation(MethodVisitor mv){
		AnnotationVisitor av = mv.visitAnnotation(getAnnotationType(), true);
		createAnnotation(av);
	}
	
	public void createParameterAnnotation(MethodVisitor mv, int paramIndex){
		AnnotationVisitor av = mv.visitParameterAnnotation(paramIndex, getAnnotationType(), true);
		createAnnotation(av);
	}
	
	public void createAnnotation(FieldVisitor fv){
		AnnotationVisitor av = fv.visitAnnotation(getAnnotationType(), true);
		createAnnotation(av);
	}
	
	public void createAnnotation(AnnotationVisitor av) {
		Map<String,Object> prop = getProperties();
		for(String propName : prop.keySet()){
			Object value = prop.get(propName);
			if(value.getClass().isArray()){
				boolean isAnnotationArray = false;
				if(value.getClass().getComponentType() == Annotation.class)
					isAnnotationArray = true;
				AnnotationVisitor avArray = av.visitArray(propName);
				for(int i = 0; i < Array.getLength(value); i++){
					if(isAnnotationArray){
						Annotation arrayAnnotation = (Annotation) Array.get(value, i);
						AnnotationVisitor avArrayMember = avArray.visitAnnotation(null, arrayAnnotation.getAnnotationType());
						arrayAnnotation.createAnnotation(avArrayMember);
					}else{
						avArray.visit(null, Array.get(value, i));
					}
				}
				avArray.visitEnd();
			} else if(value.getClass().isEnum()){ 
				av.visitEnum(propName, Type.getType(value.getClass()).getDescriptor(), value.toString() );
			} else if(value.getClass() == Annotation.class){
				Annotation annotation = (Annotation)value;  
				AnnotationVisitor aiv = av.visitAnnotation(propName, annotation.getAnnotationType());
				annotation.createAnnotation(aiv);
			} else {
				av.visit(propName, value);	
			}
			
		}
		av.visitEnd();
	}
	
	public String getAnnotationType() {
		return Type.getType(getAnnotation()).getDescriptor();
	}
	
}
