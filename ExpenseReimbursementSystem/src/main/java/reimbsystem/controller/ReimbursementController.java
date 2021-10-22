package reimbsystem.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import io.javalin.http.Context;
import reimbsystem.MyLogger;
import reimbsystem.model.Reimbursement;
import reimbsystem.model.User;
import reimbsystem.model.User.UserRole;
import reimbsystem.service.ReimbursementService;
import reimbsystem.service.ReimbursementServiceImpl;

public class ReimbursementController {
	
	private static Logger log;
	private static ReimbursementService reimbService;
	
	static {
		log = MyLogger.getLoggerForClass(ReimbursementController.class);
		reimbService = new ReimbursementServiceImpl();
	}
	
	public static void setReimbursementService(ReimbursementService newReimbService) {
		reimbService = newReimbService;
	}
	
	public static void getAllReimbursements(Context context) { 
		log.info("Retrieving Reimbursements");
		
		// Get my reimbursements from DB
		List<Reimbursement> reimbList = new ArrayList<>();
		
		// Get all reimbursements or just a specific user's?
		User currentUser = (User)context.sessionAttribute("currentUser");
		if (currentUser.getRole() == UserRole.FINANCE_MANAGER) {
			reimbList = reimbService.selectAllReimbursements();
		}
		else
			reimbList = reimbService.selectReimbursementsByEmployee(currentUser.getMyID());
		
		context.json(reimbList);
		
	}
	
	public static void updateReimbursement(Context context) {
		
		// Retrieve information from form & session
		int reimbID = Integer.parseInt(context.formParam("myID"));
		String status = context.formParam("reimbStatus");
		User currentUser = (User)context.sessionAttribute("currentUser");
		
		// Log 
		log.info("Updating reimbursement resolved by: " + currentUser.getUsername());
		
		// Update reimbursement
		reimbService.updateReimbursement(reimbID, status, currentUser);
		
		// Retrieve updated reimbursement
		Reimbursement updatedReimb = reimbService.selectReimbursementByID(reimbID);
		context.json(updatedReimb);
	}
	
	public static void submitReimbursement(Context context) {
		
		// Retrieve information from submitted form
		// TODO: put try-catch block around parses
		double amount = Double.parseDouble(context.formParam("reimbAmount"));
		String description = context.formParam("reimbDescription");
		String type = context.formParam("reimbType");
		
		// Retrieve the user from our current session
		User currentUser = (User)context.sessionAttribute("currentUser");

		// Log
		log.info("Submitting reimbursement authored by: " + currentUser.getUsername());
		
		// Insert our new reimbursement into the DB
		reimbService.insertReimbursement(amount, description, type, currentUser);
		
		context.redirect("/html/reimbursements-employee.html");
	}
	
}