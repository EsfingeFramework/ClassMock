package net.sf.esfinge.classmock;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.signature.SignatureWriter;

import net.sf.esfinge.classmock.api.IAnnotationLocationReader;
import net.sf.esfinge.classmock.api.IAnnotationReader;
import net.sf.esfinge.classmock.api.IClassReader;
import net.sf.esfinge.classmock.api.IFieldReader;
import net.sf.esfinge.classmock.api.IMethodReader;
import net.sf.esfinge.classmock.api.enums.LocationEnum;
import net.sf.esfinge.classmock.api.enums.ModifierEnum;
import net.sf.esfinge.classmock.api.enums.VisibilityEnum;
import net.sf.esfinge.classmock.imp.AnnotationImp;
import net.sf.esfinge.classmock.imp.FieldImp;

class ParseASM {

    private static final String ENUM_VALUES = "ENUM$VALUES";

    private final IClassReader reader;

    private final boolean visibleAtRuntime = true;

    private final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

    /**
     * @param reader
     *            to build
     */
    public ParseASM(final IClassReader reader) {

        this.reader = reader;
    }

    /**
     * @return the data from the entity
     */
    public byte[] parse() {

        this.addEntity();
        this.addClassDefaultConstructor();
        this.addClassLevelAnnotations();
        this.addFields();
        this.addMethods();

        this.cw.visitEnd();

        return this.cw.toByteArray();
    }

    private void addEnumFieldValues() {

        final FieldImp field = new FieldImp(ParseASM.ENUM_VALUES, Enum.class);
        field.visibility(VisibilityEnum.PRIVATE);
        field.modifiers(ModifierEnum.FINAL, ModifierEnum.STATIC, ModifierEnum.SYNTHETIC);
        field.hasGetter(false);
        field.hasSetter(false);

        this.reader.fields().add(field);
    }

    private void addClassDefaultConstructor() {

        if (this.reader.isClass() || this.reader.isAbstract()) {

            final MethodVisitor mv = this.cw.visitMethod(VisibilityEnum.PUBLIC.getOpCodes(), "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, this.getInternalName(this.reader.superclass().superclass()), "<init>", "()V", false);
            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
    }

    private void addEntity() {

        final int opcodes = this.reader.visibility().getOpCodes()
                        + this.reader.modifiers().stream().mapToInt(m -> m.getOpCodes()).sum();

        this.cw.visit(this.reader.version().getOpCodes(),
                        opcodes,
                        this.getEntityName(),
                        this.addGenericSuperClassSignature(),
                        this.getInternalName(this.reader.superclass().superclass()),
                        this.reader.interfaces().stream().map(i -> this.getInternalName(i)).toArray(String[]::new));
    }

    private String getEntityName() {

        return this.reader.name().contains(".")
                        ? this.reader.name().replaceAll("\\.", "/")
                        : this.reader.name();
    }

    private String getEntityEnumName() {

        return "L" + this.getEntityName() + ";";
    }

    private String getEntityEnumNameArray() {

        return "[" + this.getEntityEnumName();
    }

    private void addClassLevelAnnotations() {

        final ClassVisitor cv = this.cw;

        for (final IAnnotationReader wrapper : this.reader.annotations()) {

            final AnnotationVisitor av = cv.visitAnnotation(this.getDescriptor(wrapper), this.visibleAtRuntime);
            this.createAnnotation(av, wrapper);
        }
    }

    private void addFields() {

        if (this.reader.isEnum()) {

            this.addEnumFieldValues();
        }

        for (final IFieldReader field : this.reader.fields()) {

            final FieldVisitor fv = this.toFieldVisitor(field);

            for (final IAnnotationReader wrapper : field.annotations()) {

                if (wrapper instanceof IAnnotationLocationReader) {

                    final IAnnotationLocationReader location = (IAnnotationLocationReader) wrapper;

                    if ((location.location() == null) || (location.location() == LocationEnum.FIELD)) {

                        this.createAnnotation(fv, wrapper);
                    }
                } else {

                    this.createAnnotation(fv, wrapper);
                }
            }

            fv.visitEnd();

            if (field.hasGetter()) {

                this.createGetter(field);
            }

            if (field.hasSetter()) {

                this.createSetter(field);
            }
        }
    }

    private void addMethods() {

        if (this.reader.isEnum()) {

            this.createClinitEnum();
            this.createInitEnum();
            this.createValuesEnum();
            this.createValueOfEnum();
        }

        for (final IMethodReader method : this.reader.methods()) {

            int parameterPosition = 0;
            final MethodVisitor mv = this.cw.visitMethod(
                            method.visibility().getOpCodes(),
                            method.name(),
                            this.getMethodSignature(method),
                            null,
                            method.exceptions().stream().map(clazz -> this.getInternalName(clazz)).toArray(String[]::new));

            for (final IAnnotationReader wrapper : method.annotations()) {

                this.createAnnotation(mv, wrapper);
            }

            for (final IFieldReader parameter : method.parameters()) {

                for (final IAnnotationReader wrapper : parameter.annotations()) {

                    this.createAnnotation(mv, wrapper, parameterPosition);
                }

                parameterPosition++;
            }

            mv.visitCode();

            if (method.returnType() == void.class) {

                mv.visitInsn(Opcodes.RETURN);
                mv.visitMaxs(0, 1);

            } else if (Arrays.asList(int.class, short.class, byte.class, char.class, boolean.class)
                            .contains(method.returnType())) {

                mv.visitInsn(Opcodes.ICONST_0);
                mv.visitInsn(Opcodes.IRETURN);
                mv.visitMaxs(1, 1);

            } else if (method.returnType() == long.class) {

                mv.visitInsn(Opcodes.LCONST_0);
                mv.visitInsn(Opcodes.LRETURN);
                mv.visitMaxs(2, 1);

            } else if (method.returnType() == float.class) {

                mv.visitInsn(Opcodes.FCONST_0);
                mv.visitInsn(Opcodes.FRETURN);
                mv.visitMaxs(1, 1);

            } else if (method.returnType() == double.class) {

                mv.visitInsn(Opcodes.DCONST_0);
                mv.visitInsn(Opcodes.DRETURN);
                mv.visitMaxs(2, 1);

            } else {

                mv.visitInsn(Opcodes.ACONST_NULL);
                mv.visitInsn(Opcodes.ARETURN);
                mv.visitMaxs(1, 1);
            }

            if (this.reader.isAnnotation() && (method.value() != null)) {

                final AnnotationVisitor av = mv.visitAnnotationDefault();
                av.visit(null, method.value());
                av.visitEnd();
            }

            mv.visitEnd();
        }
    }

    private void createClinitEnum() {

        final MethodVisitor mv = this.cw.visitMethod(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
        mv.visitCode();

        int counter = 0;
        final int[] array = { Opcodes.ICONST_0, Opcodes.ICONST_1,
                        Opcodes.ICONST_2, Opcodes.ICONST_3,
                        Opcodes.ICONST_4, Opcodes.ICONST_5
        };

        final List<IFieldReader> fields = this.reader.fields().stream()
                        .filter(f -> this.isConstantEnum(f) || this.isDefaultFieldEnumValues(f))
                        .collect(Collectors.toList());

        for (final IFieldReader field : fields) {

            if (!this.isDefaultFieldEnumValues(field)) {

                mv.visitTypeInsn(Opcodes.NEW, this.getEntityName());
                mv.visitInsn(Opcodes.DUP);
                mv.visitLdcInsn(field.name());

                if (counter < array.length) {
                    mv.visitInsn(array[counter++]);
                } else {
                    mv.visitIntInsn(Opcodes.BIPUSH, counter++);
                }

                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, this.getEntityName(), "<init>", "(Ljava/lang/String;I)V", false);
                mv.visitFieldInsn(Opcodes.PUTSTATIC, this.getEntityName(), field.name(), this.getEntityEnumName());
            }
        }

        boolean isFistTime = true;

        for (final IFieldReader field : fields) {

            if (this.isDefaultFieldEnumValues(field)) {

                if (isFistTime) {

                    isFistTime = false;
                    mv.visitInsn(Opcodes.ICONST_0);
                    mv.visitTypeInsn(Opcodes.ANEWARRAY, this.getEntityName());
                    mv.visitFieldInsn(Opcodes.PUTSTATIC, this.getEntityName(), field.name(), this.getEntityEnumNameArray());
                    mv.visitInsn(Opcodes.RETURN);
                    mv.visitMaxs(1, 0);
                    mv.visitEnd();

                } else {

                    mv.visitInsn(Opcodes.AASTORE);
                    mv.visitFieldInsn(Opcodes.PUTSTATIC, this.getEntityName(), field.name(), this.getEntityEnumNameArray());
                    mv.visitInsn(Opcodes.RETURN);
                    mv.visitMaxs(4, 0);
                    mv.visitEnd();
                }
            } else {

                if (isFistTime) {

                    isFistTime = false;
                    mv.visitIntInsn(Opcodes.BIPUSH, counter);

                    counter = 0;
                    mv.visitTypeInsn(Opcodes.ANEWARRAY, this.getEntityName());
                    mv.visitInsn(Opcodes.DUP);
                    mv.visitInsn(array[counter++]);
                    mv.visitFieldInsn(Opcodes.GETSTATIC, this.getEntityName(), field.name(), this.getEntityEnumName());

                } else if (counter < array.length) {

                    mv.visitInsn(Opcodes.AASTORE);
                    mv.visitInsn(Opcodes.DUP);
                    mv.visitInsn(array[counter++]);
                    mv.visitFieldInsn(Opcodes.GETSTATIC, this.getEntityName(), field.name(), this.getEntityEnumName());

                } else {

                    mv.visitInsn(Opcodes.AASTORE);
                    mv.visitInsn(Opcodes.DUP);
                    mv.visitIntInsn(Opcodes.BIPUSH, counter++);
                    mv.visitFieldInsn(Opcodes.GETSTATIC, this.getEntityName(), field.name(), this.getEntityEnumName());
                }
            }
        }
    }

    private void createInitEnum() {

        final MethodVisitor mv = this.cw.visitMethod(VisibilityEnum.PRIVATE.getOpCodes(), "<init>", "(Ljava/lang/String;I)V", null, null);
        mv.visitCode();

        final Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(3, l0);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitVarInsn(Opcodes.ALOAD, 1);
        mv.visitVarInsn(Opcodes.ILOAD, 2);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Enum", "<init>", "(Ljava/lang/String;I)V", false);
        mv.visitInsn(Opcodes.RETURN);

        final Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLocalVariable("this", this.getEntityEnumName(), null, l0, l1, 0);
        mv.visitMaxs(3, 3);
        mv.visitEnd();
    }

    private void createValuesEnum() {

        final int opcodes = VisibilityEnum.PUBLIC.getOpCodes() + ModifierEnum.STATIC.getOpCodes();
        final MethodVisitor mv = this.cw.visitMethod(opcodes, "values", "()" + this.getEntityEnumNameArray(), null, null);
        mv.visitCode();

        final Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(1, l0);
        mv.visitFieldInsn(Opcodes.GETSTATIC, this.getEntityName(), ParseASM.ENUM_VALUES, this.getEntityEnumNameArray());
        mv.visitInsn(Opcodes.DUP);
        mv.visitVarInsn(Opcodes.ASTORE, 0);
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitInsn(Opcodes.ARRAYLENGTH);
        mv.visitInsn(Opcodes.DUP);
        mv.visitVarInsn(Opcodes.ISTORE, 1);
        mv.visitTypeInsn(Opcodes.ANEWARRAY, this.getEntityName());
        mv.visitInsn(Opcodes.DUP);
        mv.visitVarInsn(Opcodes.ASTORE, 2);
        mv.visitInsn(Opcodes.ICONST_0);
        mv.visitVarInsn(Opcodes.ILOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "arraycopy", "(Ljava/lang/Object;ILjava/lang/Object;II)V", false);
        mv.visitVarInsn(Opcodes.ALOAD, 2);
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(5, 3);
        mv.visitEnd();
    }

    private void createValueOfEnum() {

        final int opcodes = VisibilityEnum.PUBLIC.getOpCodes() + ModifierEnum.STATIC.getOpCodes();
        final MethodVisitor mv = this.cw.visitMethod(opcodes, "valueOf", "(Ljava/lang/String;)" + this.getEntityEnumName(), null, null);
        mv.visitCode();

        final Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(1, l0);
        mv.visitLdcInsn(Type.getType(this.getEntityEnumName()));
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Enum", "valueOf", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;", false);
        mv.visitTypeInsn(Opcodes.CHECKCAST, this.getEntityName());
        mv.visitInsn(Opcodes.ARETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();
    }

    private void createGetter(final IFieldReader field) {

        final String typeDescriptor = this.getDescriptor(field.type());
        final String typeGenericDescriptor = this.getDescriptor(field.generics());

        final MethodVisitor mv = this.cw.visitMethod(
                        VisibilityEnum.PUBLIC.getOpCodes(),
                        this.getGetterName(field),
                        "()" + typeDescriptor,
                        typeGenericDescriptor,
                        null);

        for (final IAnnotationReader wrapper : field.annotations()) {

            if ((wrapper instanceof IAnnotationLocationReader)
                            && (((IAnnotationLocationReader) wrapper).location() == LocationEnum.GETTER)) {

                this.createAnnotation(mv, wrapper);
            }
        }

        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitFieldInsn(Opcodes.GETFIELD, this.reader.name(), field.name(), typeDescriptor);

        if (Arrays.asList("I", "B", "Z", "C", "S").contains(typeDescriptor)) {
            mv.visitInsn(Opcodes.IRETURN);
        } else if ("J".equals(typeDescriptor)) {
            mv.visitInsn(Opcodes.LRETURN);
        } else if ("F".equals(typeDescriptor)) {
            mv.visitInsn(Opcodes.FRETURN);
        } else if ("D".equals(typeDescriptor)) {
            mv.visitInsn(Opcodes.DRETURN);
        } else {
            mv.visitInsn(Opcodes.ARETURN);
        }

        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    private void createSetter(final IFieldReader field) {

        final String typeDescriptor = this.getDescriptor(field.type());
        final String typeGenericDescriptor = this.getDescriptor(field.generics());
        final MethodVisitor mv = this.cw.visitMethod(
                        VisibilityEnum.PUBLIC.getOpCodes(),
                        this.getSetterName(field),
                        "(" + typeDescriptor + ")V",
                        typeGenericDescriptor,
                        null);

        for (final IAnnotationReader wrapper : field.annotations()) {

            if ((wrapper instanceof IAnnotationLocationReader)
                            && (((IAnnotationLocationReader) wrapper).location() == LocationEnum.SETTER)) {

                this.createAnnotation(mv, wrapper);
            }
        }

        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);

        if (Arrays.asList("I", "B", "Z", "C", "S").contains(typeDescriptor)) {
            mv.visitVarInsn(Opcodes.ILOAD, 1);
        } else if ("J".equals(typeDescriptor)) {
            mv.visitVarInsn(Opcodes.LLOAD, 1);
        } else if ("F".equals(typeDescriptor)) {
            mv.visitVarInsn(Opcodes.FLOAD, 1);
        } else if ("D".equals(typeDescriptor)) {
            mv.visitVarInsn(Opcodes.DLOAD, 1);
        } else {
            mv.visitVarInsn(Opcodes.ALOAD, 1);
        }

        mv.visitFieldInsn(Opcodes.PUTFIELD, this.reader.name(), field.name(), typeDescriptor);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
    }

    private void createAnnotation(final MethodVisitor mv, final IAnnotationReader wrapper, final int parameterPosition) {

        final AnnotationVisitor av = mv.visitParameterAnnotation(parameterPosition, this.getDescriptor(wrapper), this.visibleAtRuntime);
        this.createAnnotation(av, wrapper);
    }

    private void createAnnotation(final MethodVisitor mv, final IAnnotationReader wrapper) {

        final AnnotationVisitor av = mv.visitAnnotation(this.getDescriptor(wrapper), this.visibleAtRuntime);
        this.createAnnotation(av, wrapper);
    }

    private void createAnnotation(final FieldVisitor fv, final IAnnotationReader wrapper) {

        final AnnotationVisitor av = fv.visitAnnotation(this.getDescriptor(wrapper), this.visibleAtRuntime);
        this.createAnnotation(av, wrapper);
    }

    private void createAnnotation(final AnnotationVisitor av, final IAnnotationReader wrapper) {

        for (final Entry<String, Object> entry : wrapper.properties().entrySet()) {

            final String propName = entry.getKey();
            final Object value = entry.getValue();

            if (value instanceof Class) {

                av.visit(propName, Type.getType((Class<?>) value));

            } else if (value.getClass().isArray()) {

                final boolean isAnnotationArray = value.getClass().getComponentType() == IAnnotationReader.class;
                final AnnotationVisitor avArray = av.visitArray(propName);

                for (int i = 0; i < Array.getLength(value); i++) {

                    if (isAnnotationArray) {

                        final IAnnotationReader innerWrapper = (IAnnotationReader) Array.get(value, i);
                        final AnnotationVisitor innerAV = avArray.visitAnnotation(null, this.getDescriptor(innerWrapper));
                        this.createAnnotation(innerAV, innerWrapper);

                    } else {

                        final Optional<IAnnotationReader> optional = this.toAnnotationWrapper(Array.get(value, i));

                        if (optional.isPresent()) {

                            final IAnnotationReader innerWrapper = optional.get();
                            final AnnotationVisitor innerAV = avArray.visitAnnotation(null, this.getDescriptor(innerWrapper));
                            this.createAnnotation(innerAV, innerWrapper);

                        } else {
                            avArray.visit(null, Array.get(value, i));
                        }
                    }
                }

                avArray.visitEnd();

            } else if (value.getClass().isEnum()) {

                av.visitEnum(propName, this.getDescriptor(value.getClass()), value.toString());

            } else if (IAnnotationReader.class.isAssignableFrom(value.getClass())) {

                final IAnnotationReader innerWrapper = (IAnnotationReader) value;
                final AnnotationVisitor innerAV = av.visitAnnotation(propName, this.getDescriptor(innerWrapper));
                this.createAnnotation(innerAV, innerWrapper);

            } else {

                av.visit(propName, value);
            }
        }

        av.visitEnd();
    }

    private FieldVisitor toFieldVisitor(final IFieldReader field) {

        FieldVisitor fv = null;
        final int opcodes = field.modifiers().stream().mapToInt(m -> m.getOpCodes()).sum();

        if (this.reader.isEnum() && field.modifiers().contains(ModifierEnum.STATIC)) {

            if (this.isDefaultFieldEnumValues(field)) {

                // Especial enum constant
                fv = this.cw.visitField(opcodes,
                                field.name(),
                                this.getEntityEnumNameArray(),
                                null,
                                null);

            } else {

                // Normal enum constants
                fv = this.cw.visitField(opcodes,
                                field.name(),
                                this.getEntityEnumName(),
                                null,
                                null);
            }
        } else {

            // Normal fields
            fv = this.cw.visitField(opcodes,
                            field.name(),
                            this.getDescriptor(field.type()),
                            this.getDescriptor(field.generics()), // TODO Implement
                            field.value());
        }

        return fv;
    }

    @SuppressWarnings("unchecked")
    private Optional<IAnnotationReader> toAnnotationWrapper(final Object innerAnnotation) {

        Optional<IAnnotationReader> optional = Optional.empty();

        try {
            String canonicalName = innerAnnotation.getClass().getCanonicalName();
            canonicalName = canonicalName.contains("$") ? canonicalName.split("\\$")[1].trim() : canonicalName;

            final Class<? extends Annotation> clazz = (Class<? extends Annotation>) Class.forName(canonicalName);
            final IAnnotationReader wrapper = new AnnotationImp(clazz);

            for (final Method method : clazz.getDeclaredMethods()) {

                final Method realMethod = innerAnnotation.getClass().getMethod(method.getName());
                final Object value = realMethod.invoke(innerAnnotation);

                if (value != null) {

                    wrapper.properties().put(method.getName(), value);
                }
            }

            optional = Optional.ofNullable(wrapper);

        } catch (SecurityException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e) {
        }

        return optional;
    }

    private String addGenericSuperClassSignature() {

        String signature = null;
        final Class<?> superclass = this.reader.superclass().superclass();
        final Collection<Class<?>> generics = this.reader.superclass().generics();

        if ((superclass != null) && (!generics.isEmpty())) {

            // Classe Atual
            final SignatureVisitor sv = new SignatureWriter().visitClassBound();

            // Super Classe
            final SignatureVisitor svsc = sv.visitSuperclass();
            svsc.visitClassType(this.getInternalName(superclass));

            generics.forEach(generic -> {

                // Tipo de Argumento da Super Classe
                final SignatureVisitor svpt = svsc.visitTypeArgument(SignatureVisitor.INSTANCEOF);
                svpt.visitClassType(this.getInternalName(generic));
                svpt.visitEnd();
            });

            svsc.visitEnd();
            sv.visitEnd();

            signature = sv.toString();
        }

        return signature;
    }

    private String getMethodSignature(final IMethodReader method) {

        final StringBuffer sb = new StringBuffer();
        sb.append("(");

        for (final IFieldReader field : method.parameters()) {

            sb.append(this.getDescriptor(field.type()));
        }

        sb.append(")");
        sb.append(this.getDescriptor(method.returnType()));

        return sb.toString();
    }

    private String getGetterName(final IFieldReader field) {

        final String name = field.name();

        return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private String getSetterName(final IFieldReader field) {

        final String name = field.name();

        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private String getInternalName(final Class<?> clazz) {

        return (clazz != null) ? Type.getInternalName(clazz) : null;
    }

    private String getDescriptor(final Class<?> clazz) {

        return (clazz != null) ? Type.getDescriptor(clazz) : null;
    }

    private String getDescriptor(final IAnnotationReader wrapper) {

        return (wrapper != null) ? Type.getDescriptor(wrapper.annotation()) : null;
    }

    private boolean isConstantEnum(final IFieldReader field) {

        return VisibilityEnum.PUBLIC.equals(field.visibility())
                        && field.modifiers().contains(ModifierEnum.FINAL)
                        && field.modifiers().contains(ModifierEnum.STATIC)
                        && field.modifiers().contains(ModifierEnum.ENUM);
    }

    private boolean isDefaultFieldEnumValues(final IFieldReader field) {

        return ParseASM.ENUM_VALUES.equals(field.name());
    }
}