package net.sf.classmock.example.property;

import static net.sf.classmock.ClassMockUtils.*;

import java.util.Map;

import net.sf.classmock.ClassMock;
import net.sf.classmock.Location;

import org.junit.Assert;
import org.junit.Test;

public class PropertyMapFactoryTest {
	
	@Test
	public void mapCreation(){
		
		class Example{
			private String prop1;
			private int prop2;
			public String getProp1() {
				return prop1;
			}
			public void setProp1(String prop1) {
				this.prop1 = prop1;
			}
			public int getProp2() {
				return prop2;
			}
			public void setProp2(int prop2) {
				this.prop2 = prop2;
			}
		}
		
		Example example = new Example();
		example.setProp1("test");
		example.setProp2(13);
		
		Map<String, String> map = PropertyMapFactory.getPropertyMap(example);
		
		Assert.assertEquals("test", map.get("prop1"));
		Assert.assertEquals("13", map.get("prop2"));
	}
	
	
	@Test
	public void mapCreationWithIgnore(){
		
		class Example{
			private String prop1;
			private int prop2;
			public String getProp1() {
				return prop1;
			}
			public void setProp1(String prop1) {
				this.prop1 = prop1;
			}
			@Ignore
			public int getProp2() {
				return prop2;
			}
			public void setProp2(int prop2) {
				this.prop2 = prop2;
			}
		}
		
		Example example = new Example();
		example.setProp1("test");
		example.setProp2(13);
		
		Map<String, String> map = PropertyMapFactory.getPropertyMap(example);
		
		Assert.assertEquals("test", map.get("prop1"));
		Assert.assertNull(map.get("prop2"));
	}

	
}
