package net.sf.classmock.example.basic;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.objectweb.asm.Type;

import net.sf.classmock.ClassMock;
import net.sf.classmock.ClassMockUtils;
import net.sf.classmock.Location;


public class TesteGeneration {


	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		System.out.println(Type.getType(Object.class).getInternalName());
		ClassMock mock = new ClassMock("Mock");	
		mock.setSuperclass(Superclasse.class)
		    .addInterface(Interface.class)
		    //.addInterface(Comparable.class)
		    .addAnnotation(Label.class,"Crasse")
		    .addProperty("nome", String.class)
		    .addAnnotation("nome", Teste.class,Location.GETTER,"teste")
		    .addAnnotationProperty("nome", Teste.class, "valor", 23)
		    .addAnnotationProperty("nome", Teste.class, "array", new String[]{"A","B","C"})
		    .addAnnotationProperty("nome", Teste.class, "enumeration", TesteEnum.TESTE2)
	        .addAnnotationProperty("nome", Teste.class, "label",new net.sf.classmock.Annotation(Label.class,"1"))
	        .addAnnotationProperty("nome", Teste.class, "labels",
	        		new net.sf.classmock.Annotation[]{
	        	     new net.sf.classmock.Annotation(Label.class,"A"),
	        	     new net.sf.classmock.Annotation(Label.class,"B")
	        	    })
		    .addProperty("idade", long.class)
		    .addMethod(String.class, "testar", int.class, int.class)
		    .addMethodAnnotation("testar", Teste.class,"OK")
		    .addMethodAnnotationProperty("testar", Teste.class, "array", new String[]{"12","34"})
		    .addMethodParamAnnotation(0, "testar", Domain.class, "domain")
		    .addMethodParamAnnotationProperty(0, "testar", Domain.class, "outra", 23);
		
		Class classe = mock.createClass();
		
		classe.newInstance();
		
		System.out.println("Superclasse: "+classe.getSuperclass().getName());
		
		for(Class interf : classe.getInterfaces()){
			System.out.println("Interface: "+interf.getName());
		}
		
		for(Annotation a : classe.getAnnotations()){
			System.out.println("   "+a.annotationType());
			if(a.annotationType() == Label.class){
				System.out.println("   value="+((Label)a).value());
			}
		}
		System.out.println("--------");
		for(Method m : classe.getMethods()){
			System.out.println(m.getName());
			for(Annotation a : m.getAnnotations()){
				System.out.println("   "+a.annotationType());
				if(a.annotationType() == Teste.class){
					System.out.println("   valor="+((Teste)a).valor());
					System.out.println("   value="+((Teste)a).value());
					System.out.println("   enumeration="+((Teste)a).enumeration());
					System.out.println("   array="+Arrays.toString(((Teste)a).array()));
					System.out.println("   label="+((Teste)a).label().value());
					System.out.println("   labels=");
					for(Label l : ((Teste)a).labels()){
						System.out.println("      @Label("+l.value()+")");
					}
				}
			}
			for (int i = 0; i < m.getParameterTypes().length; i++) {
				for (int j = 0; j < m.getParameterAnnotations()[i].length; j++) {
					System.out.println("      Param "+i+" annotation @"+m.getParameterAnnotations()[i][j].annotationType().getName());
					if(m.getParameterAnnotations()[i][j].annotationType() == Domain.class){
						System.out.println("      value="+((Domain)m.getParameterAnnotations()[i][j]).value());
						System.out.println("      outra="+((Domain)m.getParameterAnnotations()[i][j]).outra());
					}
				}
			}
		}
		System.out.println("--------");
		for(Field f : classe.getDeclaredFields()){
			System.out.println(f.getType() +" "+ f.getName());
			for(Annotation a : f.getAnnotations()){
				System.out.println(a.annotationType());
			}
		}

	}

}
