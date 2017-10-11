package net.sf.esfinge.classmock.example.basic;

import org.testng.Assert;
import org.testng.annotations.Test;

import net.sf.esfinge.classmock.ClassMock;
import net.sf.esfinge.classmock.api.IClassWriter;

public class GenerateInterface {

    @Test
    public void isInterface() {

        final IClassWriter mock = ClassMock.of(FactoryIt.getName()).asInterface();

        Assert.assertTrue(mock.build().isInterface());
    }
}