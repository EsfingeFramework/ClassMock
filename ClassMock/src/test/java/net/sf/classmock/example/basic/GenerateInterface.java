package net.sf.classmock.example.basic;
import java.lang.reflect.Method;

import net.sf.classmock.ClassMock;


public class GenerateInterface {

	public static void main(String[] args) {
		ClassMock mockInterf = new ClassMock("MinhaInterface",true);
		
		mockInterf.addInterface(Comparable.class)
		   .addMethod(String.class, "execute");
		
		Class classe = mockInterf.createClass();
		
		System.out.println("É interface? "+classe.isInterface());
		
		for(Method m : classe.getMethods()){
			System.out.println(m.getName());
		}

	}

}
