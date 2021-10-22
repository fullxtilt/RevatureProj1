package reimbsystem.service;

import java.util.List;

import reimbsystem.model.Reimbursement;
import reimbsystem.model.User;

public interface ReimbursementService {

	// Insert methods
	boolean insertReimbursement(double amount, String description, String typeString, User author);
	
	// Read methods
	List<Reimbursement> selectAllReimbursements(); 
	List<Reimbursement> selectReimbursementsByEmployee(int userID);
	Reimbursement selectReimbursementByID(int reimbID);
	
	// Update methods
	boolean updateReimbursement(int reimbID, String statusString, User resolver);
}
