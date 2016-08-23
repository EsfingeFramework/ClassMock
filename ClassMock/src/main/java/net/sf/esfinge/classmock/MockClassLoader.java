package net.sf.esfinge.classmock;

public class MockClassLoader extends ClassLoader {
	
	private static MockClassLoader instance;
	
	public static MockClassLoader getInstance() {
		if(instance == null)
			instance = new MockClassLoader();
		return instance;
	}

	public Class defineClass(String name, byte[] b){
		return defineClass(name, b, 0, b.length);
	}

}
