package net.sf.esfinge.classmock.example.property;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

@SuppressWarnings("unused")
public class PropertyMapFactoryTest {

    @Test
    public void mapCreation() {

        class Example {

            private String prop1;

            private int prop2;

            public String getProp1() {

                return this.prop1;
            }

            public void setProp1(final String prop1) {

                this.prop1 = prop1;
            }

            public int getProp2() {

                return this.prop2;
            }

            public void setProp2(final int prop2) {

                this.prop2 = prop2;
            }
        }

        final Example example = new Example();
        example.setProp1("test");
        example.setProp2(13);

        final Map<String, String> map = PropertyMapFactory.getPropertyMap(example);

        Assert.assertEquals("test", map.get("prop1"));
        Assert.assertEquals("13", map.get("prop2"));
    }

    @Test
    public void mapCreationWithIgnore() {

        class Example {

            private String prop1;

            private int prop2;

            public String getProp1() {

                return this.prop1;
            }

            public void setProp1(final String prop1) {

                this.prop1 = prop1;
            }

            @Ignore
            public int getProp2() {

                return this.prop2;
            }

            public void setProp2(final int prop2) {

                this.prop2 = prop2;
            }
        }

        final Example example = new Example();
        example.setProp1("test");
        example.setProp2(13);

        final Map<String, String> map = PropertyMapFactory.getPropertyMap(example);

        Assert.assertEquals("test", map.get("prop1"));
        Assert.assertNull(map.get("prop2"));
    }
}