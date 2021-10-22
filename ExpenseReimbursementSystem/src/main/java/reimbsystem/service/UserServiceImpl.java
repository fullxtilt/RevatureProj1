package reimbsystem.service;

import reimbsystem.dao.UserDao;
import reimbsystem.dao.UserDaoImpl;
import reimbsystem.model.User;

public class UserServiceImpl implements UserService {
	
	private UserDao userDao;
	
	{
		userDao = new UserDaoImpl();
	}
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User selectUserByUsername(String username) {
		User targetUser = userDao.selectUserByUsername(username);
		return targetUser;
	}
}
