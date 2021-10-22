package reimbsystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import io.javalin.http.Context;
import reimbsystem.MyLogger;
import reimbsystem.model.User;
import reimbsystem.service.UserService;
import reimbsystem.service.UserServiceImpl;

public class UserController {
	
	private static UserService userService;
	private static Logger log;
	
	static {
		userService = new UserServiceImpl();
		log = MyLogger.getLoggerForClass(ReimbursementController.class);
	}
	
	public static void setUserService(UserService newUserService) {
		userService = newUserService;
	}

	public static void login(Context context) {
		
		// Read information from form
		String username = context.formParam("myUsername");
		String password = context.formParam("myPassword");
		
		// Grab user from DB
		User targetUser = userService.selectUserByUsername(username);

		// If username/pass were correct
		if (targetUser != null && targetUser.getPassword().equals(password)) {
			context.sessionAttribute("currentUser", targetUser);
			context.redirect("/html/reimbursements-employee.html");
			log.info("user " + username + " logged in");
		}	
		// else, return to login
		else {
			context.redirect("/html/loginpage.html");
			log.warn("received invalid username or password, redirecting back to login");
		}
	}
	
	public static void logout(Context context) {
		log.info("user " + ((User)context.sessionAttribute("currentUser")).getUsername() + "has logged out.");
		context.sessionAttribute("currentUser", null);
		context.redirect("/html/loginpage.html");
	}
	
	public static void getCurrentUser(Context context) {
		
		context.json(context.sessionAttribute("currentUser"));
	}
}
