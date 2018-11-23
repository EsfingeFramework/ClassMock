![](https://avatars0.githubusercontent.com/u/7748716)
![](https://img.shields.io/github/stars/EsfingeFramework/ClassMock.svg?longCache=true&style=for-the-badge) ![](https://img.shields.io/github/forks/EsfingeFramework/ClassMock.svg?longCache=true&style=for-the-badge) ![](https://img.shields.io/github/tag/EsfingeFramework/ClassMock.svg?longCache=true&style=for-the-badge) ![](https://img.shields.io/github/release/EsfingeFramework/ClassMock.svg?longCache=true&style=for-the-badge) ![](https://img.shields.io/github/issues/EsfingeFramework/ClassMock.svg?longCache=true&style=for-the-badge)

# ClassMock
Test Tool for Metadata-Based and Reflection-Based Components

# 1. Introduction
ClassMock is a framework that helps the creation of unit tests for components that use reflection or annotations. In this kind of classes, the behavior is dependent of the class structure. This way, each test case usually works with a different class created specifically for the test. With ClassMock is possible to define and generate classes in runtime, allowing a better test readability and logic sharing between tests.

# 2. Features
The ClassMock framework has a very intuitive API that allows you to:

01. create dynamic Annotation;
02. create dynamic Enum;
03. create dynamic Interface;
04. create dynamic Abstract Class;
05. create dynamic Concrete Class;
06. Add a superclass;
07. Add interfaces;
08. Add instance fields using fluent API or parsing String;
09. Add static fields using fluent API or parsing String;
10. Add instance methods using fluent API or parsing String;
11. Add static methods using fluent API or parsing String;
12. Set java version for JRE compilation;
13. Set visibility (PRIVATE, PUBLIC, PROTECTED);
14. Set modifiers (VOLATILE, STATIC, FINAL, SYNCHRONIZED...);
15. Apply annotations;
16. Apply generics at superclass; 

# 3. Install
The first step is to add the library file in your project.

## 3.1 Maven
Add in your pom.xml file

```xml
<dependency>
  <groupId>net.sf.esfinge</groupId>
  <artifactId>classmock</artifactId>
  <version>2.3</version>
</dependency>
```
## 3.2 Download Jar
The jar library can be download at [Maven Central](https://oss.sonatype.org/content/groups/staging/net/sf/esfinge/classmock/2.3/).

# 4. Concepts
This section is necessary to understand some keywords that you will find at javadoc.

 - **Entity:** Is the final product that you aims to create, it can be: *Concrete Class, Abstract Class, Enum, Interface or an Annotation*
 - **Modifiers:** Are properties responsible for modify the definition of your Entity, methods or field. All this properties are well-known such as final, transient, static, abstract...
 - **Visibility:** Is a kind of Modifier, that is responsible for the level of access/visibility that you define as public, private and protected. It was separated from Modifiers to facilitate the usage.
 
**_WARNING:_** At byte code level all things should be informed as it would be by the compiler. Example all java classes extend Object even not explicit by the programmer. This is implicitly resolved by the compiler. But you must remember that you are creating entities at runtime without the compiler. 

# 5. How to
## 5.1 Create an Entity
As you can see bellow this project tries to act like the compiler would do it for you.

```java
// Optionally you can declare the package of your entity.
// This allows you to create two or more entities with the same name, but in a different package
Class<?> yourClass = ClassMock.of("my.pack.org.fake.domain.YoursUniqueDynamicClassName").build();

// Creates a Concrete Class
// Automatic add the SUPER modifier
// Automatic set Object.class as your superclass()
final Class<?> clazz = ClassMock.of("MyEspecialClass").build(); // Implicitly defines a concrete class (Default)
final Class<?> clazz = ClassMock.of("MyEspecialClass2").asClass().build(); // Explicit defines a concrete class

// Creates an Abstract Class
// Automatic add the SUPER and ABSTRACT modifiers
// Automatic set Object.class as your superclass()
final Class<?> absClazz = ClassMock.of("AbstractMyEspecialClass")
                                       .asAbstract() 
                                       .build();
// Creates an Interface
// Automatic add the INTERFACE and ABSTRACT modifiers
// Automatic set Object.class as your superclass()
final Class<?> interf = ClassMock.of("IMyEspecialClass")
                                       .asInterface()
                                       .build();
// Creates an Annotation
// Automatic add the INTERFACE, ABSTRACT, and ANNOTATION modifiers
// Automatic set Object.class as your superclass()
// Automatic set Annotation.class as one of yours interfaces()
final Class<?> annotationClazz = ClassMock.of("MyEspecialAnnotationClass")
                                       .asAnnotation()
                                       .build();
// Creates an Enum
// Automatic add the SUPER, FINAL, and ENUM modifiers
// Automatic set Enum.class as your superclass()
final Class<?> enumClazz = ClassMock.of("MyEspecialAnnotationClass")
                                       .asEnum()
                                       .build();
```

## 5.2 Add fields to the Entity
The default properties for fields are:
- Visibility on PRIVATE.
- Always generate getter method.
- Always generate setter method.

### 5.2.1 Instance Field
As this is the default you don't have to specify nothing.

```java
final IClassWriter mock = ClassMock.of("SingleClassName"); // Concrete Class
// Add field
mock.field("id", Integer. class)                           // Name and class type of the field
                        .annotation(Id.class)              // JPA Annotation for Primary Key
                        .and()                             // And add another annotation to this field
                        .annotation(Column.class)          // JPA Annotation for column
                        .property("name", "ID_CODE");      // On the annotation above set the property "name" with the value "ID_CODE"
// Add field
mock.field("name", String.class)                           // Name and class type of the field
                        .annotation(Column.class)          // JPA Annotation for column
                        .property("name", "TX_NAME");      // On the annotation above set the property "name" with the value "TX_NAME"

// Add field of Self Type of the entity
mock.field("mySelfType")                                   // Name and self type of the entity for the field (Self.class)
                        .annotation(JoinColumn.class)      // JPA Annotation for column (FK)
                        .property("name", "TX_NAME");      // On the annotation above set the property "name" with the value "TX_NAME"
```

### 5.2.2 Class Field
Note in the example bellow the presence of the **STATIC** modifier.

```java
final IClassWriter mock = ClassMock.of("SingleClassName");                  // Concrete Class
mock.interfaces(Serializable.class);                                        // Implements Serializable

// Add field
mock.field("serialVersionUID", long.class)                                  // Name and class type of the field
                        .hasGetter(false)                                   // Disable the generation of the getter method
                        .hasSetter(false)                                   // Disable the generation of the getter method
                        .modifiers(ModifierEnum.STATIC, ModifierEnum.FINAL) // MANDATORY - The STATIC modifier
                        .value(4376923548604344061L)                        // Initialize the constant
                        .visibility(VisibilityEnum.PRIVATE);                // Visibility of the field
```

### 5.2.3 Enum Field
This is used when you want to create an Enum constants. It is possible to add to the enum all kinds of field from the section 5.2.1 and 5.2.2

```java
// Define Enum type as your output entity.
final IClassWriter mock = ClassMock.of(this.getClassName()).asEnum();

// Makes a collection from an array
Arrays.asList("BMW", "BENTLEY", "PORSCHE", "CADILLAC", "LEXUS", "FERRARI", "MERCEDES", "FORD")
            .forEach(car -> { // Java 8 lambda
                // Create enum constants
                mock.field(car, Enum.class)                        // Name and class type of the field
                                .hasGetter(false)                  // MANDATORY - Disable the generation of the getter method
                                .hasSetter(false)                  // MANDATORY - Disable the generation of the setter method
                                .visibility(VisibilityEnum.PUBLIC) // MANDATORY - Visibility on PUBLIC
                                .modifiers(ModifierEnum.FINAL,     // MANDATORY - Modifier FINAL
                                  ModifierEnum.STATIC,             // MANDATORY - Modifier FINAL
                                  ModifierEnum.ENUM);              // MANDATORY - Modifier ENUM
            });

// Creates an Enum type
final Class<?> clazzEnum = mock.build();

// Get an array of your enum constants
final Object[] enumConstants = clazzEnum.getEnumConstants();
```

### 5.2.4 Field from String
It is possible to define a field from a String input. You can define: the class type, the name, the modifiers and even add annotations to the field. You can omit the ";"

**_WARNING:_** When you declare the class type always prefer to declare it with the package. It's possible to declare without the package, but we can't guarantee that the one found is the one you imagine to be loading. Because is possible to have the same name in different packages.  

```java
// Field with JoinColumn annotation
final StringBuilder sb = new StringBuilder();
sb.append("@javax.persistence.JoinColumn(name = \"MY_COLUMN_NAME\", nullable = false)").append("\n");
sb.append("private String ").append(this.fieldName);

final IClassWriter mock = ClassMock.of("SingleClassName");   // Concrete Class

// Add field
mock.fieldByParse(sb.toString())                             // Parse the definition
                        .hasGetter(true)                     // As is the default can be omitted
                        .hasSetter(true)                     // As is the default can be omitted
                        .annotation(Id.class)                // JPA Annotation for Primary Key
                        .and()                               // And add another annotation to this field
                        .annotation(Column.class)            // JPA Annotation for column
                        .property("name", "MY_PRIMARY_KEY"); // On the annotation above set the property "name" with the value "MY_PRIMARY_KEY"

// Creates a Concrete Class
final Class<?> clazz = mock.build();
```

## 5.3 Add methods to the Entity
The default properties for methods are:
- Visibility on PUBLIC.
- Void as the default return type.

**_WARNING:_** ClassMock only creates a method signature, at this moment it is not possible to implement the method signature, but you can bypass this with techniques like proxies and mocks. 

### 5.3.1 Instance Method
As this is the default you don't have to specify nothing.

```java
final IClassWriter mock = ClassMock.of("SingleClassName");   // Concrete Class

// Example without parameters
mock.method("testIt")                                        // The method name
                   .returnTypeAsVoid()                       // As is the default can be omitted
                   .exceptions(Exception.class)              // Throws Exception
                   .annotation(Override.class);              // Annotated with Override annotation

// Example returning the type of the entity
mock.method("testIt")                                        // The method name
                   .returnTypeAsSelfType()                   // Self.class Type of the entity
                   .exceptions(Exception.class)              // Throws Exception
                   .annotation(Override.class);              // Annotated with Override annotation

// Creates a Concrete Class
final Class<?> clazz = mock.build();
```

### 5.3.2 Class Method
Note in the example bellow the presence of the **STATIC** modifier.

```java
final IClassWriter mock = ClassMock.of("SingleClassName");   // Concrete Class
mock.method("testIt")                                        // The method name
                   .modifiers(ModifierEnum.STATIC)           // MANDATORY - The STATIC modifier
                   .returnTypeAsVoid()                       // As is the default can be omitted
                   .exceptions(Exception.class)              // Throws Exception
                   .annotation(Override.class);              // Annotated with Override annotation

// Creates a Concrete Class
final Class<?> clazz = mock.build();
```

### 5.3.3 Annotation Method
This is used when you want to create an Annotation

```java
// Define Annotation type as your output entity.
final IClassWriter mock = ClassMock.of(this.getClassName()).asAnnotation();

// MANDATORY This line because makes possible to infer by reflection all the class definition
mock.annotation(Retention.class)
                .property(RetentionPolicy.RUNTIME);
// Add Method
mock.method("alias")                                  // The method name
                .returnType(String.class)             // String as the return type
                .visibility(VisibilityEnum.PUBLIC)    // MANDATORY - Visibility on PUBLIC
                .modifiers(ModifierEnum.ABSTRACT);    // MANDATORY - Modifier ABSTRACT 
// Add Method
mock.method("query")                                  // The method name
                .returnType(String.class)             // String as the return type
                .visibility(VisibilityEnum.PUBLIC)    // MANDATORY - Visibility on PUBLIC
                .modifiers(ModifierEnum.ABSTRACT);    // MANDATORY - Modifier ABSTRACT
// Add Method
mock.method("active")                                 // The method name
                .returnType(Boolean.class)            // Boolean as the return type
                .visibility(VisibilityEnum.PUBLIC)    // MANDATORY - Visibility on PUBLICs
                .modifiers(ModifierEnum.ABSTRACT)     // MANDATORY - Modifier ABSTRACT
                .value(Boolean.TRUE);                 // Set a default value for this method

// Creates an Annotation
final Class<?> annotation = mock.build();
```

### 5.3.4 Method from String
It is possible to define a method from a String input. You can define: the class type, the name, the modifiers and even add annotations to the field. You can omit () and {}.

**_WARNING:_** When you declare the class type always prefer to declare it with the package. It's possible to declare without the package, but we can't guarantee that the one found is the one you imagine to be loading. Because is possible to have the same name in different packages.

```java
// Method with JoinColumn annotation, note the JoinColumn package!
final StringBuilder sb = new StringBuilder();
sb.append("@jabax.annotation.example.JoinColumn(name = \"MY_COLUMN_NAME\", nullable = false)").append("\n");
sb.append("public void ").append(this.methodName).append("(){}");

final IClassWriter mock = ClassMock.of("SingleClassName");   // Concrete Class
mock.methodByParse(sb.toString())                            // Parse the definition

// Creates a Concrete Class
final Class<?> annotation = mock.build();
```

## 5.4 More examples
See our [tests section](https://github.com/EsfingeFramework/ClassMock/blob/master/ClassMock/src/test/java/net/sf/esfinge/classmock/example/general/TesteClassMock.java) in the project.

# 6. License
[MIT Licensed](https://github.com/EsfingeFramework/ClassMock/blob/master/LICENSE)