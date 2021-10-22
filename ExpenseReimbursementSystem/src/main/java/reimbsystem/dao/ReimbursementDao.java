package reimbsystem.dao;

import java.sql.Timestamp;
import java.util.List;

import reimbsystem.model.Reimbursement;
import reimbsystem.model.Reimbursement.ReimbursementStatus;
import reimbsystem.model.User;

public interface ReimbursementDao {
 
	// Insert methods
	boolean insertReimbursement(Reimbursement reimb);
	
	// Read methods
	List<Reimbursement> selectAllReimbursements();
	List<Reimbursement> selectReimbursementsByEmployee(int userID);
	Reimbursement selectReimbursementByID(int reimbID);
	
	// Update methods
	boolean updateReimbursement(int reimbID, ReimbursementStatus newStatus, Timestamp submittedTime, User resolver);
}
