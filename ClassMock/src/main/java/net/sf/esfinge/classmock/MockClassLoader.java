package net.sf.esfinge.classmock;

/**
 * Class responsible for act as ClassLoader necessary to load the dynamic class.
 */
public class MockClassLoader extends ClassLoader {

    private static MockClassLoader instance;

    /**
     * Access an instance of our class loader.
     *
     * @return singleton
     */
    public static MockClassLoader getInstance() {

        if (MockClassLoader.instance == null) {

            MockClassLoader.instance = new MockClassLoader();
        }

        return MockClassLoader.instance;
    }

    /**
     * Used to mount the class in our class loader.
     *
     * @param name
     *            of the entity
     * @param b
     *            the data
     * @return class built
     */
    public Class<?> defineClass(final String name, final byte[] b) {

        return this.defineClass(name, b, 0, b.length);
    }
}