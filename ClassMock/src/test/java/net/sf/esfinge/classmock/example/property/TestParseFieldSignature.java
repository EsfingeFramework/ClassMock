package net.sf.esfinge.classmock.example.property;

import javax.persistence.JoinColumn;

import org.testng.Assert;
import org.testng.annotations.Test;

import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;
import net.sf.esfinge.classmock.parse.ParseFieldSignature;

public class TestParseFieldSignature {

    private final ParseFieldSignature parserField = ParseFieldSignature.getInstance();

    private final String fieldName = "myFieldName";

    @Test
    public void name() {

        final String signature = "java.lang.String " + this.fieldName;
        Assert.assertEquals(this.parserField.parse(signature).name(), this.fieldName);
        Assert.assertEquals(this.parserField.parse(signature + ";").name(), this.fieldName);
    }

    @Test
    public void type() {

        Assert.assertEquals(this.parserField.parse("Float " + this.fieldName).type(), Float.class);
        Assert.assertEquals(this.parserField.parse("my.pack.Integer " + this.fieldName).type(), Object.class);
        Assert.assertEquals(this.parserField.parse("Integer " + this.fieldName).type(), Integer.class);
    }

    @Test
    public void visibility() {

        final String signature = "java.lang.String " + this.fieldName;
        Assert.assertEquals(this.parserField.parse("protected " + signature).visibility(), VisibilityEnum.PROTECTED);
        Assert.assertEquals(this.parserField.parse("private " + signature).visibility(), VisibilityEnum.PRIVATE);
        Assert.assertEquals(this.parserField.parse("public " + signature).visibility(), VisibilityEnum.PUBLIC);
        Assert.assertEquals(this.parserField.parse(signature).visibility(), VisibilityEnum.PRIVATE);
    }

    @Test
    public void modifiers() {

        Assert.assertTrue(this.parserField
                        .parse("final java.lang.String " + this.fieldName)
                        .modifiers()
                        .contains(ModifierEnum.FINAL));
        Assert.assertTrue(this.parserField
                        .parse("final static java.lang.String " + this.fieldName)
                        .modifiers()
                        .stream()
                        .allMatch(mod -> (mod == ModifierEnum.FINAL)
                                        || (mod == ModifierEnum.STATIC)));
        Assert.assertTrue(this.parserField
                        .parse("java.lang.String " + this.fieldName)
                        .modifiers()
                        .isEmpty());
    }

    @Test
    public void annotations() {

        final StringBuilder sb = new StringBuilder();
        sb.append("@javax.persistence.JoinColumn").append("\n");
        sb.append("private java.lang.String ").append(this.fieldName);

        Assert.assertTrue(this.parserField
                        .parse(sb.toString())
                        .annotations()
                        .stream()
                        .allMatch(wrapper -> wrapper.annotation().equals(JoinColumn.class)));
    }

    @Test
    public void annotationsWithProperties() {

        final StringBuilder sb = new StringBuilder();
        sb.append("@javax.persistence.JoinColumn(name = \"MY_COLUMN_NAME\", nullable = false)").append("\n");
        sb.append("private java.lang.String ").append(this.fieldName);

        Assert.assertTrue(this.parserField
                        .parse(sb.toString())
                        .annotations()
                        .stream()
                        .allMatch(wrapper -> wrapper.properties().get("name").equals("MY_COLUMN_NAME")
                                        || wrapper.properties().get("nullable").equals(false)));
    }
}