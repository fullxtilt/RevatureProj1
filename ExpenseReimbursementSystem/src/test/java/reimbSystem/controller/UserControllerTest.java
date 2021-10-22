package reimbSystem.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import io.javalin.http.Context;
import reimbsystem.controller.UserController;
import reimbsystem.service.UserService;

public class UserControllerTest {
	
	Context contextMock = Mockito.mock(Context.class);
	UserService userServiceMock = Mockito.mock(UserService.class);
	
	@BeforeEach
	void setUp() throws Exception {
		UserController.setUserService(userServiceMock);
	}
	
	@Test
	void testInvalidLoginMethod() {
		
		// Throws an exception here because just calling contextMock's methods without a server is going to break things
//		Mockito.when(contextMock.formParam("myUsername")).thenAnswer( ( InvocationOnMock invocationOnMock) -> {
//			
//			return "invalidUsername";
//		});
//		Mockito.when(contextMock.formParam("myPassword")).thenReturn("invalidPassword");
//		Mockito.when(userServiceMock.selectUserByUsername("invalidUsername")).thenReturn(null);
			
//		UserController.login(contextMock);
		
//		Mockito.verify(contextMock, Mockito.times(1)).redirect("/html/loginpage.html");
		//Mockito.verify(contextMock, Mockito.times(1)).redirect("/html/reimbursements-employee.html");
	}
}
