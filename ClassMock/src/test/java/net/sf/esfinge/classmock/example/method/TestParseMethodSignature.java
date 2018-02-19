package net.sf.esfinge.classmock.example.method;

import javax.persistence.Column;
import javax.persistence.JoinColumn;

import org.testng.Assert;
import org.testng.annotations.Test;

import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;
import net.sf.esfinge.classmock.imp.MethodImp;
import net.sf.esfinge.classmock.parse.ParseMethodSignature;

public class TestParseMethodSignature {

    private final ParseMethodSignature parserMethod = ParseMethodSignature.getInstance();

    private final String methodName = "myMethodName";

    @Test
    public void name() {

        final String signature = "void " + this.methodName;
        Assert.assertEquals(this.parserMethod.parse(signature).name(), this.methodName);
        Assert.assertEquals(this.parserMethod.parse(signature + "()").name(), this.methodName);
    }

    @Test
    public void returnType() {

        Assert.assertEquals(this.parserMethod.parse("void " + this.methodName).returnType(), void.class);
        Assert.assertEquals(this.parserMethod.parse("my.pack.Integer " + this.methodName).returnType(), void.class);
        Assert.assertEquals(this.parserMethod.parse("Integer " + this.methodName).returnType(), Integer.class);
    }

    @Test
    public void visibility() {

        final String signature = "void " + this.methodName;
        Assert.assertEquals(this.parserMethod.parse("protected " + signature).visibility(), VisibilityEnum.PROTECTED);
        Assert.assertEquals(this.parserMethod.parse("private " + signature).visibility(), VisibilityEnum.PRIVATE);
        Assert.assertEquals(this.parserMethod.parse("public " + signature).visibility(), VisibilityEnum.PUBLIC);
        Assert.assertEquals(this.parserMethod.parse(signature).visibility(), VisibilityEnum.PUBLIC);
    }

    @Test
    public void modifiers() {

        Assert.assertTrue(this.parserMethod
                        .parse("final void " + this.methodName)
                        .modifiers()
                        .contains(ModifierEnum.FINAL));
        Assert.assertTrue(this.parserMethod
                        .parse("final static void " + this.methodName)
                        .modifiers()
                        .stream()
                        .allMatch(mod -> (mod == ModifierEnum.FINAL)
                                        || (mod == ModifierEnum.STATIC)));
        Assert.assertTrue(this.parserMethod
                        .parse("void " + this.methodName)
                        .modifiers()
                        .isEmpty());
    }

    @Test
    public void exceptions() {

        final String signature = "void " + this.methodName + "() throws ";

        Assert.assertTrue(this.parserMethod
                        .parse(signature + "NumberFormatException")
                        .exceptions()
                        .contains(NumberFormatException.class));
        Assert.assertTrue(this.parserMethod
                        .parse(signature + "ArrayIndexOutOfBoundsException, NegativeArraySizeException")
                        .exceptions().stream()
                        .allMatch(ex -> (ArrayIndexOutOfBoundsException.class.isAssignableFrom(ex))
                                        || (NegativeArraySizeException.class.isAssignableFrom(ex))));
        Assert.assertTrue(this.parserMethod
                        .parse(signature + "WrongNameException")
                        .exceptions().isEmpty());
    }

    @Test
    public void annotations() {

        final StringBuilder sb = new StringBuilder();
        sb.append("@java.lang.Override").append("\n");
        sb.append("@javax.persistence.JoinColumn").append("\n");
        sb.append("public void ").append(this.methodName).append("(){}");

        Assert.assertTrue(this.parserMethod
                        .parse(sb.toString())
                        .annotations()
                        .stream()
                        .allMatch(wrapper -> wrapper.annotation().equals(Override.class)
                                        || wrapper.annotation().equals(JoinColumn.class)));
    }

    @Test
    public void annotationsWithProperties() {

        final StringBuilder sb = new StringBuilder();
        sb.append("@javax.persistence.JoinColumn(name = \"MY_COLUMN_NAME\", nullable = false)").append("\n");
        sb.append("public void ").append(this.methodName).append("(){}");

        Assert.assertTrue(this.parserMethod
                        .parse(sb.toString())
                        .annotations()
                        .stream()
                        .allMatch(wrapper -> wrapper.properties().get("name").equals("MY_COLUMN_NAME")
                                        || wrapper.properties().get("nullable").equals(false)));
    }

    @Test
    public void parameters() {

        final StringBuilder sb = new StringBuilder();
        sb.append("public void ").append(this.methodName).append("(");
        sb.append("java.lang.String firstName, @Column java.lang.Integer age");
        sb.append("){}");

        final MethodImp parse = this.parserMethod.parse(sb.toString());

        Assert.assertEquals(parse.parameters().size(), 2);
        Assert.assertEquals(parse.parameters()
                        .stream()
                        .filter(fr -> fr.name().equals("age"))
                        .findFirst()
                        .get()
                        .annotations()
                        .stream()
                        .findFirst()
                        .get()
                        .annotation(), Column.class);
    }
}