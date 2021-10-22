package reimbsystem.service;

import java.util.List;

import reimbsystem.model.User;

public interface UserService {
	
	User selectUserByUsername(String username);
	
}
