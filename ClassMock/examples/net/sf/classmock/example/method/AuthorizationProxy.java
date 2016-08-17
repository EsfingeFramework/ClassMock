package net.sf.classmock.example.method;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class AuthorizationProxy implements InvocationHandler {
	
	public static Object createProxy(Object obj, User user){
		return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
									  obj.getClass().getInterfaces(),
									  new AuthorizationProxy (user, obj));
	}
	
	private User user;
	private Object obj;
	
	public AuthorizationProxy(User user, Object obj) {
		this.user = user;
		this.obj = obj;
	}
	
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if(method.isAnnotationPresent(AuthorizedRoles.class)){
			AuthorizedRoles roles = method.getAnnotation(AuthorizedRoles.class);
			int find = Arrays.binarySearch(roles.value(),user.getRoleName());
			if(find < 0)
				throw new AuthorizationException("User not authorized");
		}
		return method.invoke(obj,args);
	}
}

