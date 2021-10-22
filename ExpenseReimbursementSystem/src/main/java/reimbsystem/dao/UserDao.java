package reimbsystem.dao;

import java.util.List;

import reimbsystem.model.User;

public interface UserDao {

	// Insert methods
	boolean insertUser(User newUser);
	
	// Read methods
	List<User> selectAllUsers();
	User selectUserByUsername(String username);
	
}
