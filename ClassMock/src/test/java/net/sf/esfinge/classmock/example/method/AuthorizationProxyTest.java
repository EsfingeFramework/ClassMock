package net.sf.esfinge.classmock.example.method;

import org.powermock.api.mockito.PowerMockito;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import net.sf.esfinge.classmock.ClassMock;
import net.sf.esfinge.classmock.ClassMockUtils;
import net.sf.esfinge.classmock.api.IClassWriter;
import net.sf.esfinge.classmock.api.enums.ModifierEnum;

public class AuthorizationProxyTest {

    private Object mock;

    @BeforeTest
    public void createMock() {

        final IClassWriter classMock = ClassMock.of("DummyInterface").asInterface();
        classMock.method("execute")
                        .returnType(void.class)
                        .modifiers(ModifierEnum.ABSTRACT)
                        .annotation(AuthorizedRoles.class)
                        .property(new String[] { "admin" });

        final Class<?> interf = classMock.build();
        this.mock = PowerMockito.mock(interf);
    }

    @Test(enabled = false)
    public void authorizedMethod() throws Throwable {

        Object proxy = AuthorizationProxy.createProxy(this.mock, new User("john", "admin"));
        proxy = PowerMockito.spy(proxy);

        PowerMockito.when(proxy, "execute").thenCallRealMethod();
        ClassMockUtils.invoke(proxy, "execute");
    }

    @Test(enabled = false, expectedExceptions = AuthorizationException.class)
    public void unauthorizedMethod() throws Throwable {

        Object proxy = AuthorizationProxy.createProxy(this.mock, new User("john", "operator"));
        proxy = PowerMockito.spy(proxy);

        PowerMockito.doThrow(new AuthorizationException("ERROR")).when(proxy, "execute");
        ClassMockUtils.invoke(proxy, "execute");
    }
}