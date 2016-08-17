package net.sf.classmock;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.DRETURN;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.FRETURN;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LRETURN;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class Property {
	
	private Class type;
	private String name;
	private boolean hasSetter;
	private boolean hasGetter;
	
	private Map<Class,Annotation> annotations = new HashMap<Class,Annotation>();

	public Property(Class type, String name, boolean hasSetter,
			boolean hasGetter) {
		super();
		this.type = type;
		this.name = name;
		this.hasSetter = hasSetter;
		this.hasGetter = hasGetter;
	}
	public Class getType() {
		return type;
	}
	public void setType(Class type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isHasSetter() {
		return hasSetter;
	}
	public void setHasSetter(boolean hasSetter) {
		this.hasSetter = hasSetter;
	}
	public boolean isHasGetter() {
		return hasGetter;
	}
	public void setHasGetter(boolean hasGetter) {
		this.hasGetter = hasGetter;
	}
	public String getGetterName(){
		return "get" + name.substring(0,1).toUpperCase()+name.substring(1);
	}
	public String getSetterName(){
		return "set" + name.substring(0,1).toUpperCase()+name.substring(1);
	}
	public void addAnnotation(Annotation annotation){
		annotations.put(annotation.getAnnotation(), annotation);
	}
	public void addAnnotationProperty(Class annotation, String name, Object value){
		annotations.get(annotation).addProperty(name, value);
	}
	void createProperty(ClassWriter cw, String className) {

		createField(cw);
		if(isHasGetter()){
			createGetter(cw, className);
		}
		if(isHasSetter()){
			createSetter(cw, className);
		}
	}
	
	private void createSetter(ClassWriter cw, String className) {
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, getSetterName(), "("+getPropertyType()+")V", null, null);
		for(Annotation an : annotations.values()){
			if(an.getLocation() == Location.SETTER){
				an.createAnnotation(mv);
			}
		}
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		if(getPropertyType().equals("I") || getPropertyType().equals("B") || getPropertyType().equals("Z") || getPropertyType().equals("C") || getPropertyType().equals("S"))
			mv.visitVarInsn(ILOAD, 1);
		else if(getPropertyType().equals("J"))
			mv.visitVarInsn(LLOAD, 1);
		else if(getPropertyType().equals("F"))
			mv.visitVarInsn(FLOAD, 1);
		else if(getPropertyType().equals("D"))
			mv.visitVarInsn(DLOAD, 1);
		else
			mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(PUTFIELD, className, getName(), getPropertyType());
		mv.visitInsn(RETURN);
		mv.visitMaxs(2, 2);
		mv.visitEnd();
	}
	
	private void createGetter(ClassWriter cw, String className) {
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, getGetterName(), "()"+getPropertyType(), null, null);
		for(Annotation an : annotations.values()){
			if(an.getLocation() == Location.GETTER){
				an.createAnnotation(mv);
			}
		}
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, className, getName(), getPropertyType());
		if(getPropertyType().equals("I") || getPropertyType().equals("B") || getPropertyType().equals("Z") || getPropertyType().equals("C") || getPropertyType().equals("S"))
			mv.visitInsn(IRETURN);
		else if(getPropertyType().equals("J"))
			mv.visitInsn(LRETURN);
		else if(getPropertyType().equals("F"))
			mv.visitInsn(FRETURN);
		else if(getPropertyType().equals("D"))
			mv.visitInsn(DRETURN);
		else
			mv.visitInsn(ARETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
	}
	
	private void createField(ClassWriter cw) {
		FieldVisitor fv = cw.visitField(ACC_PRIVATE, getName(), getPropertyType(), null, null);
		for(Annotation an : annotations.values()){
			if(an.getLocation() == null || an.getLocation() == Location.FIELD){
				an.createAnnotation(fv);
			}
		}
		fv.visitEnd();
	}
	
	private String getPropertyType() {
		String propType = Type.getType(getType()).getDescriptor();
		return propType;
	}
}
