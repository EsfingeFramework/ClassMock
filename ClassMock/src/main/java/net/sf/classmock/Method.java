package net.sf.classmock;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class Method extends AbstractMethod {
	
	public Method() {
		super();
	}

	public Method(String name, Class returnType, Class[] parameters) {
		super(name, returnType, parameters);
	}

	public void createMethod(ClassWriter cw){
		MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, name, getMethodSignature(), null, null);
		for(Annotation an : annotations.values()){
				an.createAnnotation(mv);
		}
		for(Integer i : paramAnnotations.keySet()){
			for(Annotation an : paramAnnotations.get(i).values()){
				an.createParameterAnnotation(mv, i);
			}
		}
		mv.visitCode();
		if(returnType == void.class){
			mv.visitInsn(RETURN);
			mv.visitMaxs(0, 1);	
		}else if(returnType == int.class
				|| returnType == short.class
				|| returnType == byte.class
				|| returnType == char.class
				|| returnType == boolean.class){
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitMaxs(1, 1);
		}else if(returnType == long.class){
			mv.visitInsn(LCONST_0);
			mv.visitInsn(LRETURN);
			mv.visitMaxs(2, 1);
		}else if(returnType == float.class){
			mv.visitInsn(FCONST_0);
			mv.visitInsn(FRETURN);
			mv.visitMaxs(1, 1);
		}else if(returnType == double.class){
			mv.visitInsn(DCONST_0);
			mv.visitInsn(DRETURN);
			mv.visitMaxs(2, 1);
		}else{
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);
			mv.visitMaxs(1, 1);
		}
		mv.visitEnd();
	}
	
	public boolean isAbstract(){
		return false;
	}
}
