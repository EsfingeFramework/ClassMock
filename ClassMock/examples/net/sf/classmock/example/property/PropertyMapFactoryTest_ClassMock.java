package net.sf.classmock.example.property;

import static net.sf.classmock.ClassMockUtils.newInstance;
import static net.sf.classmock.ClassMockUtils.set;

import java.util.Map;

import net.sf.classmock.ClassMock;
import net.sf.classmock.Location;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PropertyMapFactoryTest_ClassMock {
	
	private ClassMock mockClass;
	
	@Before
	public void createMockClass(){
		mockClass = new ClassMock("ExampleClassMock");
		mockClass.addProperty("prop1", String.class)
		    .addProperty("prop2", int.class);
	}

	@Test
	public void mapCreation(){
		Object instance = createMockClassInstance();
		Map<String, String> map = PropertyMapFactory.getPropertyMap(instance);
		
		Assert.assertEquals("test", map.get("prop1"));
		Assert.assertEquals("13", map.get("prop2"));
	}
	
	@Test
	public void mapCreationWithIgnore(){
	    mockClass.addAnnotation("prop2", Ignore.class, Location.GETTER);
		Object instance = createMockClassInstance();
		Map<String, String> map = PropertyMapFactory.getPropertyMap(instance);
		
		Assert.assertEquals("test", map.get("prop1"));
		Assert.assertNull(map.get("prop2"));
	}
	
	private Object createMockClassInstance() {
		Object instance = newInstance(mockClass);
		set(instance,"prop1","test");
		set(instance,"prop2",13);
		return instance;
	}

}
