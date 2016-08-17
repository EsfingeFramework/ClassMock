package net.sf.classmock.example.method;

import static net.sf.classmock.ClassMockUtils.invoke;
import net.sf.classmock.ClassMock;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class AuthorizationProxyTest {
	
	Mockery context = new JUnit4Mockery(){{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    
    private Object mock;
    
    @Before
    public void createMock(){
    	ClassMock classMock  = new ClassMock("DummyInterface",true);
    	classMock.addMethod(void.class,"execute")
    		.addMethodAnnotation("execute", AuthorizedRoles.class,new String[]{"admin"});
    	Class interf = classMock.createClass();
    	
		mock = context.mock(interf);
    }
	
    @Test
	public void authorizedMethod() throws Throwable {
		Object proxy = AuthorizationProxy.createProxy(mock, new User("john","admin"));
		context.checking(new Expectations() {{
        	invoke(one (mock),"execute");
        }});
		invoke(proxy,"execute");
	}
    
    @Test(expected=AuthorizationException.class)
    public void unauthorizedMethod() throws Throwable {
		Object proxy = AuthorizationProxy.createProxy(mock, new User("john","operator"));
		
		context.checking(new Expectations() {{
        	invoke(never (mock),"execute");
        }});
		
		invoke(proxy,"execute");
	}

}
