package reimbSystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

import reimbsystem.dao.UserDao;
import reimbsystem.model.User;
import reimbsystem.service.UserServiceImpl;

public class UserServiceImplTest {

	UserDao userDaoMock;
	UserServiceImpl userService;
	User correctUser;
	
	@BeforeEach
	void setUp() throws Exception {

		userDaoMock = Mockito.mock(UserDao.class);
		
		userService = new UserServiceImpl();
		userService.setUserDao(userDaoMock);
		
		correctUser = new User();
	}
	
	@Test
	void testSelectUserByUsername() {
		Mockito.when(userDaoMock.selectUserByUsername("testUsername")).thenReturn(correctUser);
		
		// Vanilla test
		User testUser = userService.selectUserByUsername("testUsername");
		assertEquals(correctUser, testUser, "Failed vanilla test");
	}
}
