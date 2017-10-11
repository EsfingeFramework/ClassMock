package net.sf.esfinge.classmock.example.basic;

public class FactoryIt {

    private static int counter = 0;

    public static String getName() {

        return "DynamicClass_" + FactoryIt.counter++;
    };
}
