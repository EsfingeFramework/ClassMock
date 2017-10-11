package net.sf.esfinge.classmock.example.method;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class AuthorizationProxy implements InvocationHandler {

    private final User user;

    private final Object obj;

    public AuthorizationProxy(final User user, final Object obj) {

        this.user = user;
        this.obj = obj;
    }

    public static Object createProxy(final Object obj, final User user) {

        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                        obj.getClass().getInterfaces(),
                        new AuthorizationProxy(user, obj));
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args)
                    throws Throwable {

        if (method.isAnnotationPresent(AuthorizedRoles.class)) {

            final AuthorizedRoles roles = method.getAnnotation(AuthorizedRoles.class);
            final int find = Arrays.binarySearch(roles.value(), this.user.getRoleName());

            if (find < 0) {

                throw new AuthorizationException("User not authorized");
            }
        }

        return method.invoke(this.obj, args);
    }
}