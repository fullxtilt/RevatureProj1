package reimbsystem.frontcontroller;

import static io.javalin.apibuilder.ApiBuilder.*;

import io.javalin.Javalin;
import reimbsystem.controller.ReimbursementController;
import reimbsystem.controller.UserController;

public class Dispatcher {
	
	public Dispatcher(Javalin app) {
		setupPaths(app);
	}
	
	public static void setupPaths(Javalin app) {
		app.routes(()->{
			path("/api", ()->{
				
				path("/user/current", ()->{
					get(UserController::getCurrentUser);
				});
				
				path("/login", ()->{
					post(UserController::login);
				});
				
				path("/logout", ()->{
					get(UserController::logout);
				});
				
				path("/reimbursements", ()->{
					get(ReimbursementController::getAllReimbursements);
					patch(ReimbursementController::updateReimbursement);
					post(ReimbursementController::submitReimbursement);
					
				});
			});;
			
		});
	}
}
