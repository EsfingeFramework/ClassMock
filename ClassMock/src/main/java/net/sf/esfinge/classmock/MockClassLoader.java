package net.sf.esfinge.classmock;

class MockClassLoader extends ClassLoader {

    private static MockClassLoader instance;

    /**
     * @return singleton
     */
    public static MockClassLoader getInstance() {

        if (MockClassLoader.instance == null) {

            MockClassLoader.instance = new MockClassLoader();
        }

        return MockClassLoader.instance;
    }

    /**
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