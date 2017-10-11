package net.sf.esfinge.classmock.example.basic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import net.sf.esfinge.classmock.ClassMock;
import net.sf.esfinge.classmock.api.IAnnotationReader;
import net.sf.esfinge.classmock.api.IClassWriter;
import net.sf.esfinge.classmock.api.IMethodReader;
import net.sf.esfinge.classmock.api.IMethodWriter;
import net.sf.esfinge.classmock.api.enums.LocationEnum;
import net.sf.esfinge.classmock.imp.AnnotationImp;
import net.sf.esfinge.classmock.imp.MethodImp;

public class TesteGeneration {

    private Class<?> classe;

    @BeforeTest
    private void setUp() {

        final AnnotationImp label1 = new AnnotationImp(Label.class);
        label1.property("1");

        final AnnotationImp label2 = new AnnotationImp(Label.class);
        label2.property("2");

        final IAnnotationReader[] arrayLabels = { label1, label2 };

        final IMethodWriter method = new MethodImp("testar");
        method.annotation(Teste.class)
                        .property("OK")
                        .property("array", new String[] { "12", "34" });
        method.returnType(String.class);
        method.parameter("op1", int.class)
                        .annotation(Domain.class)
                        .property("domain")
                        .property("outra", 23);
        method.parameter("op2", int.class);

        final IClassWriter mock = ClassMock.of(FactoryIt.getName());
        mock.superclass(Superclasse.class);
        mock.interfaces(Interface.class);
        mock.annotation(Label.class).property("Crasse");
        mock.field("idade", long.class);
        mock.field("nome", String.class)
                        .annotation(Teste.class, LocationEnum.GETTER)
                        .property("teste")
                        .property("valor", 23)
                        .property("array", new String[] { "A", "B", "C" })
                        .property("enumeration", TesteEnum.TESTE2)
                        .property("classe", ClassMock.class)
                        .property("label", label1)
                        .property("labels", arrayLabels);

        mock.method((IMethodReader) method);
        this.classe = mock.build();
    }

    @Test
    public void generationClass() {

        Assert.assertEquals(Superclasse.class, this.classe.getSuperclass());
        Assert.assertEquals("Crasse", this.classe.getAnnotation(Label.class).value());

        for (final Class<?> interf : this.classe.getInterfaces()) {

            Assert.assertEquals(Interface.class, interf);
        }

    }

    @Test
    public void generationField() {

        for (final Field f : this.classe.getDeclaredFields()) {

            if ("nome".equals(f.getName())) {

                final Teste teste = f.getAnnotation(Teste.class);
                Assert.assertEquals(teste.value(), "teste");
                Assert.assertEquals(teste.valor(), 23);
                Assert.assertEquals(teste.array(), new String[] { "A", "B", "C" });
                Assert.assertEquals(teste.enumeration(), TesteEnum.TESTE2);
                Assert.assertEquals(teste.classe(), ClassMock.class);
                Assert.assertEquals(teste.label().value(), "1");

                final Label[] labels = teste.labels();
                Assert.assertEquals(labels[0].value(), "1");
                Assert.assertEquals(labels[1].value(), "2");
            }
        }
    }

    @Test
    public void generationMethod() {

        for (final Method m : this.classe.getMethods()) {

            if ("testar".equals(m.getName())) {

                // Annotation
                final Teste methodAnnotation = m.getAnnotation(Teste.class);
                Assert.assertEquals(methodAnnotation.value(), "OK");
                Assert.assertEquals(methodAnnotation.array(), new String[] { "12", "34" });

                // Return type
                Assert.assertEquals(m.getReturnType(), String.class);

                // Parameters
                for (final Parameter parameter : m.getParameters()) {

                    if ("arg0".equals(parameter.getName())) {

                        final Domain domain = parameter.getAnnotation(Domain.class);
                        Assert.assertEquals(domain.value(), "domain");
                        Assert.assertEquals(domain.outra(), 23);
                        Assert.assertEquals(int.class, parameter.getType());

                    } else {

                        Assert.assertEquals("arg1", parameter.getName());
                        Assert.assertEquals(int.class, parameter.getType());
                    }
                }
            }
        }
    }
}