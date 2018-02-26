package net.sf.esfinge.classmock.example.general;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.testng.Assert;
import org.testng.annotations.Test;

import net.sf.esfinge.classmock.ClassMock;
import net.sf.esfinge.classmock.api.enums.LocationEnum;
import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;
import net.sf.esfinge.classmock.example.basic.FactoryIt;
import net.sf.esfinge.classmock.imp.AnnotationImp;
import net.sf.esfinge.classmock.imp.FieldImp;
import net.sf.esfinge.classmock.imp.MethodImp;

public class TestClone {

    @Test
    public void cloneAnnotation() throws CloneNotSupportedException {

        final AnnotationImp annotationImp = new AnnotationImp(Column.class);
        annotationImp.property("name", "TX_NAME");
        annotationImp.location(LocationEnum.FIELD);

        final AnnotationImp clone = (AnnotationImp) annotationImp.clone();
        Assert.assertFalse(clone == annotationImp);
        Assert.assertEquals(clone, annotationImp);
        Assert.assertEquals(clone.location(), annotationImp.location());
        Assert.assertEquals(clone.annotation(), annotationImp.annotation());
        Assert.assertEquals(clone.properties(), annotationImp.properties());
        Assert.assertEquals(clone.and(), annotationImp.and());
    }

    @Test
    public void cloneField() throws CloneNotSupportedException {

        final FieldImp fieldImp = new FieldImp("age", Integer.class);
        fieldImp.visibility(VisibilityEnum.PROTECTED);
        fieldImp.modifiers(ModifierEnum.FINAL);
        fieldImp.hasGetter(true).hasSetter(false);
        fieldImp.annotation(Column.class, LocationEnum.FIELD)
                        .property("name", "NU_AGE");

        final FieldImp clone = (FieldImp) fieldImp.clone();
        Assert.assertFalse(clone == fieldImp);
        Assert.assertEquals(clone, fieldImp);
        Assert.assertEquals(clone.name(), fieldImp.name());
        Assert.assertEquals(clone.type(), fieldImp.type());
        Assert.assertEquals(clone.generics(), fieldImp.generics());
        Assert.assertEquals(clone.hasGetter(), fieldImp.hasGetter());
        Assert.assertEquals(clone.hasSetter(), fieldImp.hasSetter());
        Assert.assertEquals(clone.visibility(), fieldImp.visibility());
        Assert.assertEquals(clone.modifiers(), fieldImp.modifiers());
        Assert.assertEquals(clone.annotations(), fieldImp.annotations());
    }

    @Test
    public void cloneMethod() throws CloneNotSupportedException {

        final MethodImp methodImp = new MethodImp("calculateTax");
        methodImp.visibility(VisibilityEnum.PUBLIC);
        methodImp.modifiers(ModifierEnum.FINAL);
        methodImp.returnTypeAsVoid();
        methodImp.exceptions(Exception.class);
        methodImp.annotation(Override.class);
        methodImp.parameter("year", Integer.class);
        methodImp.parameter("daysDelayed", Integer.class);

        final MethodImp clone = (MethodImp) methodImp.clone();
        Assert.assertFalse(clone == methodImp);
        Assert.assertEquals(clone, methodImp);
        Assert.assertEquals(clone.visibility(), methodImp.visibility());
        Assert.assertEquals(clone.modifiers(), methodImp.modifiers());
        Assert.assertEquals(clone.returnType(), methodImp.returnType());
        Assert.assertEquals(clone.name(), methodImp.name());
        Assert.assertEquals(clone.annotations(), methodImp.annotations());
        Assert.assertEquals(clone.parameters(), methodImp.parameters());
        Assert.assertEquals(clone.exceptions(), methodImp.exceptions());
        Assert.assertEquals(clone.value(), methodImp.value());
    }

    @Test
    public void cloneClass() throws CloneNotSupportedException {

        final ClassMock writer = (ClassMock) ClassMock.of(FactoryIt.getName());
        writer.annotation(Entity.class)
                        .and()
                        .annotation(Table.class)
                        .property("schema", "public")
                        .property("name", "MY_TABLE");
        writer.fieldByParse("private String id")
                        .hasGetter(true)
                        .hasSetter(true)
                        .annotation(Id.class)
                        .and()
                        .annotation(Column.class)
                        .property("name", "MY_PRIMARY_KEY");
        writer.methodByParse("@Override public final void calc(Integer age, Float weight)");

        final ClassMock clone = (ClassMock) writer.clone(writer.name());
        Assert.assertFalse(clone == writer);
        Assert.assertEquals(clone, writer);
        Assert.assertEquals(clone.visibility(), writer.visibility());
        Assert.assertEquals(clone.modifiers(), writer.modifiers());
        Assert.assertEquals(clone.interfaces(), writer.interfaces());
        Assert.assertEquals(clone.name(), writer.name());
        Assert.assertEquals(clone.annotations(), writer.annotations());
        Assert.assertEquals(clone.fields(), writer.fields());
        Assert.assertEquals(clone.methods(), writer.methods());
        Assert.assertEquals(clone.superclass(), writer.superclass());
        Assert.assertEquals(clone.version(), writer.version());
    }
}