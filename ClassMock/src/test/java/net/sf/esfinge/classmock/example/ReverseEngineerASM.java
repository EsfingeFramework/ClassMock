package net.sf.esfinge.classmock.example;

import java.io.IOException;
import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.TraceClassVisitor;

import net.sf.esfinge.classmock.example.method.User;

public class ReverseEngineerASM {

    public static void main(final String[] args) throws IOException {

        // Change the target class for any other class to see the ASM Code
        final String target = User.class.getCanonicalName();
        final ClassReader cr = new ClassReader(target);
        final TraceClassVisitor tcv = new TraceClassVisitor(null, new ASMifier(), new PrintWriter(System.out));

        cr.accept(tcv, 0);
    }
}