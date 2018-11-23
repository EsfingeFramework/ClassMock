package net.sf.esfinge.classmock.example.general;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.powermock.api.mockito.PowerMockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import net.sf.esfinge.classmock.ClassMock;
import net.sf.esfinge.classmock.api.IAnnotationReader;
import net.sf.esfinge.classmock.api.IClassWriter;
import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;
import net.sf.esfinge.classmock.example.basic.FactoryIt;
import net.sf.esfinge.classmock.imp.AnnotationImp;
import net.sf.esfinge.classmock.imp.MethodImp;

public class TesteClassMock {

    @Test
    public void isConcreteClass() {

        final ClassMock mock = (ClassMock) ClassMock.of(FactoryIt.getName()).asClass();

        Assert.assertTrue(mock.isClass());
        Assert.assertFalse(mock.isAbstract());
        Assert.assertFalse(mock.isAnnotation());
        Assert.assertFalse(mock.isEnum());
        Assert.assertFalse(mock.isInterface());
    }

    @Test
    public void isAbstractClass() {

        final ClassMock mock = (ClassMock) ClassMock.of(FactoryIt.getName()).asAbstract();

        Assert.assertTrue(mock.isAbstract());
        Assert.assertFalse(mock.isClass());
        Assert.assertFalse(mock.isAnnotation());
        Assert.assertFalse(mock.isEnum());
        Assert.assertFalse(mock.isInterface());
    }

    @Test
    public void isAnnotation() {

        final ClassMock mock = (ClassMock) ClassMock.of(FactoryIt.getName()).asAnnotation();

        Assert.assertTrue(mock.isAnnotation());
        Assert.assertFalse(mock.isAbstract());
        Assert.assertFalse(mock.isClass());
        Assert.assertFalse(mock.isEnum());
        Assert.assertFalse(mock.isInterface());
    }

    @Test
    public void isEnum() {

        final ClassMock mock = (ClassMock) ClassMock.of(FactoryIt.getName()).asEnum();

        Assert.assertTrue(mock.isEnum());
        Assert.assertFalse(mock.isAnnotation());
        Assert.assertFalse(mock.isAbstract());
        Assert.assertFalse(mock.isClass());
        Assert.assertFalse(mock.isInterface());
    }

    @Test
    public void isInterface() {

        final ClassMock mock = (ClassMock) ClassMock.of(FactoryIt.getName()).asInterface();

        Assert.assertTrue(mock.isInterface());
        Assert.assertFalse(mock.isEnum());
        Assert.assertFalse(mock.isAnnotation());
        Assert.assertFalse(mock.isAbstract());
        Assert.assertFalse(mock.isClass());
    }

    @Test
    public void reloadClass() {

        final String name = "GenericTestClass";
        final Class<?> clazz1 = ClassMock.of(name).build();
        final Class<?> clazz2 = ClassMock.of(name).build();

        Assert.assertEquals(clazz2, clazz1);
    }

    @Test
    public void createMockPackageClass() {

        final String fullName = "net.sf.esfinge.classmock.fake.dynamic." + FactoryIt.getName();
        final Class<?> clazz = ClassMock.of(fullName).build();

        Assert.assertEquals(clazz.getCanonicalName(), fullName);
    }

    @Test
    public void createMockPackageClassWithField() {

        final String fullName = "net.sf.esfinge.classmock.fake.dynamic." + FactoryIt.getName();
        final IClassWriter mock = ClassMock.of(fullName);
        mock.field("name", String.class);

        final Class<?> clazz = mock.build();

        Assert.assertEquals(clazz.getCanonicalName(), fullName);
    }

    @Test
    public void createInterface() {

        final Class<?> clazz = ClassMock.of(FactoryIt.getName()).asInterface().build();

        Assert.assertTrue(clazz.isInterface());
    }

    @Test
    public void createAbstractClass() {

        final Class<?> clazz = ClassMock.of(FactoryIt.getName()).asAbstract().build();

        Assert.assertTrue(Modifier.isAbstract(clazz.getModifiers()));
    }

    @Test
    public void createEnumClass() {

        final Class<?> clazz = ClassMock.of(FactoryIt.getName()).asEnum().build();

        Assert.assertTrue(clazz.isEnum());
    }

    @Test
    public void createAnnotationClass() {

        final Class<?> clazz = ClassMock.of(FactoryIt.getName()).asAnnotation().build();

        Assert.assertTrue(clazz.isAnnotation());
    }

    @Test
    public void createAnnotationWithProperties() throws NoSuchMethodException, SecurityException {

        final IClassWriter mock = ClassMock.of(FactoryIt.getName()).asAnnotation();
        mock.method("alias")
                        .returnType(String.class)
                        .visibility(VisibilityEnum.PUBLIC)
                        .modifiers(ModifierEnum.ABSTRACT);
        mock.method("query")
                        .returnType(String.class)
                        .visibility(VisibilityEnum.PUBLIC)
                        .modifiers(ModifierEnum.ABSTRACT);
        mock.method("active")
                        .returnType(Boolean.class)
                        .visibility(VisibilityEnum.PUBLIC)
                        .modifiers(ModifierEnum.ABSTRACT)
                        .value(Boolean.TRUE); // default value

        final Class<?> clazz = mock.build();

        Assert.assertNotNull(clazz.getDeclaredMethod("alias"));
        Assert.assertNotNull(clazz.getDeclaredMethod("query"));
        Assert.assertNotNull(clazz.getDeclaredMethod("active"));
        Assert.assertEquals(clazz.getDeclaredMethod("active").getDefaultValue(), Boolean.TRUE);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void createAnnotationWithoutMethods() {

        // Create annotation
        IClassWriter mock = ClassMock.of(FactoryIt.getName()).asAnnotation();
        mock.annotation(Retention.class).property(RetentionPolicy.RUNTIME); // You must have this to be reflected by other classes
        final Class<? extends Annotation> annotation = (Class<? extends Annotation>) mock.build();

        // Create a class to use the annotation
        mock = ClassMock.of(FactoryIt.getName());
        mock.annotation(annotation);
        final Class<?> clazz = mock.build();

        Assert.assertTrue(clazz.isAnnotationPresent(annotation));
    }

    @Test
    public void createEnumClassWithContansts() {

        int counter = -1;
        final List<String> cars = Arrays.asList("BMW", "BENTLEY", "PORSCHE",
                        "CADILLAC", "LEXUS", "FERRARI", "MERCEDES", "FORD");

        // Test an enum creation by different number of constants
        while (counter < cars.size()) {

            counter++;
            final IClassWriter mock = ClassMock.of(FactoryIt.getName()).asEnum();

            for (int i = 0; i < counter; i++) {

                final String car = cars.get(i);
                mock.field(car, Enum.class)
                                .hasGetter(false)
                                .hasSetter(false)
                                .visibility(VisibilityEnum.PUBLIC)
                                .modifiers(ModifierEnum.FINAL, ModifierEnum.STATIC, ModifierEnum.ENUM);
            }

            final Class<?> clazz = mock.build();
            final Object[] enumConstants = clazz.getEnumConstants();

            // Valid size
            Assert.assertEquals(enumConstants.length, counter);

            for (int i = 0; i < enumConstants.length; i++) {

                // Valid name
                Assert.assertEquals(enumConstants[i].toString(), cars.get(i));
            }
        }
    }

    @Test
    public void createEnumClassWithContanstsAndFields() throws NoSuchFieldException, SecurityException {

        int counter = -1;
        final List<String> cars = Arrays.asList("BMW", "BENTLEY", "PORSCHE",
                        "CADILLAC", "LEXUS", "FERRARI", "MERCEDES", "FORD");

        // Test an enum creation by different number of constants
        while (counter < cars.size()) {

            counter++;
            final IClassWriter mock = ClassMock.of(FactoryIt.getName()).asEnum();

            for (int i = 0; i < counter; i++) {

                final String car = cars.get(i);
                mock.field(car, Enum.class)
                                .hasGetter(false)
                                .hasSetter(false)
                                .visibility(VisibilityEnum.PUBLIC)
                                .modifiers(ModifierEnum.FINAL, ModifierEnum.STATIC, ModifierEnum.ENUM);
            }

            mock.field("cylinder", Integer.class);
            mock.field("valve", Integer.class);
            mock.field("horsePower", Float.class);

            final Class<?> clazz = mock.build();
            final Object[] enumConstants = clazz.getEnumConstants();

            // Valid size
            Assert.assertEquals(enumConstants.length, counter);

            for (int i = 0; i < enumConstants.length; i++) {

                // Valid name
                Assert.assertEquals(enumConstants[i].toString(), cars.get(i));
            }

            Assert.assertNotNull(clazz.getDeclaredField("cylinder"));
            Assert.assertNotNull(clazz.getDeclaredField("valve"));
            Assert.assertNotNull(clazz.getDeclaredField("horsePower"));
        }
    }

    @Test
    public void createConcreteClass() {

        final String name = FactoryIt.getName();
        final Class<?> clazz = ClassMock.of(name).build();

        Assert.assertEquals(clazz.getSimpleName(), name);
    }

    @Test
    public void createClassWithSuperClass() {

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.superclass(Assert.class);

        final Class<?> clazz = mock.build();
        Assert.assertEquals(clazz.getSuperclass(), Assert.class);
    }

    @Test
    public void createClassWithSuperClassAndInterfaces() {

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.superclass(Assert.class);
        mock.interfaces(Serializable.class, Cloneable.class);

        final Class<?> clazz = mock.build();
        final List<Class<?>> interfaces = Arrays.asList(clazz.getInterfaces());

        Assert.assertEquals(clazz.getSuperclass(), Assert.class);
        Assert.assertTrue(interfaces.contains(Serializable.class));
        Assert.assertTrue(interfaces.contains(Cloneable.class));
    }

    @Test
    public void createClassWithOneAnnotations() {

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.annotation(Entity.class);

        final Class<?> clazz = mock.build();

        Assert.assertTrue(clazz.isAnnotationPresent(Entity.class));
    }

    @Test
    public void createClassWithAnnotationsAndProperties() {

        final String schemaName = "auth";
        final String tableName = "ANY_TABLE_NAME_MOCK";

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.annotation(Entity.class)
                        .and()
                        .annotation(Table.class)
                        .property("schema", schemaName)
                        .property("name", tableName);

        final Class<?> clazz = mock.build();
        final Table table = clazz.getAnnotation(Table.class);

        Assert.assertTrue(clazz.isAnnotationPresent(Entity.class));
        Assert.assertEquals(table.schema(), schemaName);
        Assert.assertEquals(table.name(), tableName);
    }

    @Test
    public void createClassWithInnerAnnotations() {

        final String schemaName = "auth";
        final String tableName = "ANY_TABLE_NAME_MOCK";

        final String name1 = "findByName";
        final String sql1 = "Select x From Something x.name = :name";

        final String name2 = "findByAge";
        final String sql2 = "Select x From Something x.age = :age";

        final NamedQuery namedQuery1 = PowerMockito.mock(NamedQuery.class);
        PowerMockito.when(namedQuery1.name()).thenReturn(name1);
        PowerMockito.when(namedQuery1.query()).thenReturn(sql1);

        final NamedQuery namedQuery2 = PowerMockito.mock(NamedQuery.class);
        PowerMockito.when(namedQuery2.name()).thenReturn(name2);
        PowerMockito.when(namedQuery2.query()).thenReturn(sql2);

        final NamedQuery[] arrayQueries = { namedQuery1, namedQuery2 };

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.annotation(NamedQueries.class)
                        .property(arrayQueries)
                        .and()
                        .annotation(Entity.class)
                        .and()
                        .annotation(Table.class)
                        .property("schema", schemaName)
                        .property("name", tableName);

        final Class<?> clazz = mock.build();
        final Table table = clazz.getAnnotation(Table.class);

        Assert.assertTrue(clazz.isAnnotationPresent(Entity.class));
        Assert.assertEquals(table.schema(), schemaName);
        Assert.assertEquals(table.name(), tableName);

        for (final NamedQuery namedQuery : clazz.getAnnotation(NamedQueries.class).value()) {

            if (name1.equals(namedQuery.name())) {

                Assert.assertEquals(namedQuery.query(), sql1);

            } else if (name2.equals(namedQuery.name())) {

                Assert.assertEquals(namedQuery.query(), sql2);
            }
        }
    }

    @Test
    public void createClassWithInnerAnnotationWrappers() {

        final String schemaName = "auth";
        final String tableName = "ANY_TABLE_NAME_MOCK";

        final String name1 = "findByName";
        final String sql1 = "Select x From Something x.name = :name";

        final String name2 = "findByAge";
        final String sql2 = "Select x From Something x.age = :age";

        final AnnotationImp namedQuery1 = new AnnotationImp(NamedQuery.class);
        namedQuery1.property("name", name1).property("query", sql1);

        final AnnotationImp namedQuery2 = new AnnotationImp(NamedQuery.class);
        namedQuery2.property("name", name2).property("query", sql2);

        final IAnnotationReader[] arrayQueries = { namedQuery1, namedQuery2 };

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.annotation(NamedQueries.class)
                        .property(arrayQueries)
                        .and()
                        .annotation(Entity.class)
                        .and()
                        .annotation(Table.class)
                        .property("schema", schemaName)
                        .property("name", tableName);

        final Class<?> clazz = mock.build();
        final Table table = clazz.getAnnotation(Table.class);

        Assert.assertTrue(clazz.isAnnotationPresent(Entity.class));
        Assert.assertEquals(table.schema(), schemaName);
        Assert.assertEquals(table.name(), tableName);

        for (final NamedQuery namedQuery : clazz.getAnnotation(NamedQueries.class).value()) {

            if (name1.equals(namedQuery.name())) {
                Assert.assertEquals(namedQuery.query(), sql1);
            } else if (name2.equals(namedQuery.name())) {
                Assert.assertEquals(namedQuery.query(), sql2);
            }
        }
    }

    @Test
    public void createClassWithStaticFields() {

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.field("name", String.class)
                        .value("NAME")
                        .modifiers(ModifierEnum.STATIC)
                        .visibility(VisibilityEnum.PRIVATE);
        mock.field("api", float.class)
                        .value(1.1F)
                        .modifiers(ModifierEnum.STATIC)
                        .visibility(VisibilityEnum.PUBLIC);
        mock.field("serialVersionUID", long.class)
                        .hasGetter(false)
                        .hasSetter(false)
                        .value(123456789L)
                        .modifiers(ModifierEnum.STATIC)
                        .visibility(VisibilityEnum.PUBLIC);

        for (final Field field : mock.build().getDeclaredFields()) {

            Assert.assertTrue(Modifier.isStatic(field.getModifiers()));
        }
    }

    @Test
    public void createClassWithInstanceFields() {

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.field("api", Float.class);
        mock.field("version", String.class);

        for (final Field field : mock.build().getDeclaredFields()) {

            Assert.assertFalse(Modifier.isStatic(field.getModifiers()));
            Assert.assertTrue(Modifier.isPrivate(field.getModifiers()));
        }
    }

    @Test
    public void createClassWithInstanceSelfType() {

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.field("selfMe");
        mock.field("selfMeTwo");

        final Class<?> clazz = mock.build();

        for (final Field field : clazz.getDeclaredFields()) {

            Assert.assertEquals(field.getType(), clazz);
        }
    }

    @Test
    public void createClassWithInstanceFieldWithAnnotations() {

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.field("visibility", Date.class).annotation(Column.class)
                        .property("name", "EN_VISIBILITY")
                        .property("nullable", true)
                        .and()
                        .annotation(Enumerated.class)
                        .property(EnumType.STRING);

        mock.field("birthDay", Date.class).annotation(Column.class)
                        .property("name", "DT_BIRTHDAY")
                        .property("nullable", true)
                        .and()
                        .annotation(Temporal.class)
                        .property(TemporalType.TIMESTAMP);

        mock.field("name", String.class)
                        .annotation(Column.class)
                        .property("name", "TX_NAME");

        mock.field("age", Integer.class)
                        .annotation(Column.class)
                        .property("name", "NU_AGE")
                        .property("nullable", false);

        for (final Field field : mock.build().getDeclaredFields()) {

            final Column column = field.getAnnotation(Column.class);
            Assert.assertNotNull(column);

            if ("name".equals(field.getName())) {

                Assert.assertEquals(column.name(), "TX_NAME");
                Assert.assertEquals(column.nullable(), true);

            } else if ("age".equals(field.getName())) {

                Assert.assertEquals(column.name(), "NU_AGE");
                Assert.assertEquals(column.nullable(), false);

            } else if ("visibility".equals(field.getName())) {

                Assert.assertEquals(column.name(), "EN_VISIBILITY");
                Assert.assertEquals(column.nullable(), true);

            } else {

                Assert.assertEquals(column.name(), "DT_BIRTHDAY");
                Assert.assertEquals(column.nullable(), true);
            }
        }
    }

    @Test
    public void createClassWithInstanceFieldWithGetter() {

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.field("name", String.class).hasSetter(false);

        for (final Method method : mock.build().getDeclaredMethods()) {

            Assert.assertEquals(method.getName(), "getName");
        }
    }

    @Test
    public void createClassWithInstanceFieldWithSetter() {

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.field("name", String.class).hasGetter(false);

        for (final Method method : mock.build().getDeclaredMethods()) {

            Assert.assertEquals(method.getName(), "setName");
        }
    }

    @Test
    public void createClassWithInstanceFieldWithGetterSetter() {

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.field("name", String.class);

        for (final Method method : mock.build().getDeclaredMethods()) {

            if ("getName".equals(method.getName())) {
                Assert.assertEquals(method.getName(), "getName");
            } else {
                Assert.assertEquals(method.getName(), "setName");
            }
        }
    }

    @Test
    public void createClassWithMethods() {

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.method("testIt")
                        .returnTypeAsVoid()
                        .exceptions(Exception.class)
                        .annotation(Override.class);

        for (final Method method : mock.build().getDeclaredMethods()) {

            Assert.assertEquals(method.getName(), "testIt");
        }
    }

    @Test
    public void createClassWithReturningSelfType() {

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.method("testIt")
                        .returnTypeAsSelfType()
                        .exceptions(Exception.class)
                        .annotation(Override.class);

        final Class<?> clazz = mock.build();

        for (final Method method : clazz.getDeclaredMethods()) {

            Assert.assertEquals(method.getReturnType(), clazz);
        }
    }

    @Test
    public void createClassWithMethodReader() {

        final MethodImp reader = new MethodImp("testIt");
        reader.returnType(void.class)
                        .exceptions(Exception.class)
                        .annotation(Override.class)
                        .and()
                        .annotation(Override.class);
        reader.parameter("param1", String.class)
                        .annotation(Override.class)
                        .and()
                        .annotation(Override.class);
        reader.parameter("param2", Integer.class)
                        .annotation(Override.class)
                        .and()
                        .annotation(Override.class);
        reader.parameter("param3")
                        .annotation(Override.class);

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.method(reader);

        final Class<?> clazz = mock.build();

        for (final Method method : clazz.getDeclaredMethods()) {

            Assert.assertEquals(method.getName(), "testIt");
            Assert.assertEquals(method.getReturnType(), void.class);
        }

        for (final Field field : clazz.getDeclaredFields()) {

            if ("param1".equals(field.getName())) {

                Assert.assertEquals(field.getType(), String.class);

            } else if ("param2".equals(field.getName())) {

                Assert.assertEquals(field.getType(), Integer.class);

            } else {

                Assert.assertEquals(field.getType(), clazz);
            }
        }
    }

    @Test
    public void createClassWithOneGeneric() {

        final int posicaoGenerics = 0;

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.superclass(ModelOld.class).generics(String.class);

        final Class<?> clazz = mock.build();
        final ParameterizedType parametrizedType = (ParameterizedType) clazz.getGenericSuperclass();
        final Type type = parametrizedType.getActualTypeArguments()[posicaoGenerics];

        Assert.assertTrue(String.class.isAssignableFrom((Class<?>) type));
    }

    @Test
    public void createClassWithTwoGeneric() {

        final int posicaoGenericsZero = 0;
        final int posicaoGenericsOne = 1;

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.superclass(ModelNew.class).generics(String.class).generics(Integer.class);

        final Class<?> clazz = mock.build();
        final ParameterizedType parametrizedType = (ParameterizedType) clazz.getGenericSuperclass();

        final Type type0 = parametrizedType.getActualTypeArguments()[posicaoGenericsZero];
        final Type type1 = parametrizedType.getActualTypeArguments()[posicaoGenericsOne];

        Assert.assertTrue(String.class.isAssignableFrom((Class<?>) type0));
        Assert.assertTrue(Integer.class.isAssignableFrom((Class<?>) type1));
    }

    @Test
    public void createClassWithDefaultConstructor() {

        for (final VisibilityEnum visibility : VisibilityEnum.values()) {

            final Class<?> clazz = ClassMock.of(FactoryIt.getName()).visibility(visibility).build();
            final Constructor<?> constructor = clazz.getDeclaredConstructors()[0];

            if (visibility == VisibilityEnum.PUBLIC) {

                Assert.assertTrue(Modifier.isPublic(constructor.getModifiers()));

            } else if (visibility == VisibilityEnum.PRIVATE) {

                Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));

            } else {

                Assert.assertTrue(Modifier.isProtected(constructor.getModifiers()));
            }
        }

        for (int i = 0; i < 3; i++) {

        }
    }
}