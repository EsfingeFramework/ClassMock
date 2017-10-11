package net.sf.esfinge.classmock.example.property;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import net.sf.esfinge.classmock.ClassMock;
import net.sf.esfinge.classmock.ClassMockUtils;
import net.sf.esfinge.classmock.api.IClassWriter;
import net.sf.esfinge.classmock.api.enums.LocationEnum;
import net.sf.esfinge.classmock.example.basic.FactoryIt;

public class PropertyMapFactoryTest_ClassMock {

    private IClassWriter mockClass;

    @BeforeTest
    public void createMockClass() {

        this.mockClass = ClassMock.of(FactoryIt.getName());
        this.mockClass.field("prop1", String.class);
        this.mockClass.field("prop2", int.class);
    }

    @Test
    public void mapCreation() {

        final Object instance = this.createMockClassInstance();
        final Map<String, String> map = PropertyMapFactory.getPropertyMap(instance);

        Assert.assertEquals("test", map.get("prop1"));
        Assert.assertEquals("13", map.get("prop2"));
    }

    @Test
    public void mapCreationWithIgnore() {

        this.mockClass.field("prop3", Integer.class).annotation(Ignore.class, LocationEnum.GETTER);
        final Object instance = this.createMockClassInstance();
        final Map<String, String> map = PropertyMapFactory.getPropertyMap(instance);

        Assert.assertEquals("test", map.get("prop1"));
        Assert.assertNull(map.get("prop3"));
    }

    private Object createMockClassInstance() {

        this.mockClass.name(FactoryIt.getName());
        final Object instance = ClassMockUtils.newInstance(this.mockClass);

        ClassMockUtils.set(instance, "prop1", "test");
        ClassMockUtils.set(instance, "prop2", 13);

        return instance;
    }
}