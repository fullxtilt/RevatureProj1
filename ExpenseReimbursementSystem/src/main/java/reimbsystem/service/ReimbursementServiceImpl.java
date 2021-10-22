package reimbsystem.service;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

import org.apache.log4j.Logger;

import reimbsystem.MyLogger;
import reimbsystem.dao.ReimbursementDao;
import reimbsystem.dao.ReimbursementDaoImpl;
import reimbsystem.model.Reimbursement;
import reimbsystem.model.User;
import reimbsystem.model.Reimbursement.ReimbursementStatus;
import reimbsystem.model.Reimbursement.ReimbursementType;

public class ReimbursementServiceImpl implements ReimbursementService {

	private ReimbursementDao reimbDao;
	private Logger log;

	{
		reimbDao = new ReimbursementDaoImpl();
		log = MyLogger.getLoggerForClass(this);
	}

	public void setReimbDao(ReimbursementDao reimbDao) {
		this.reimbDao = reimbDao;
	}

	@Override
	public boolean insertReimbursement(double amount, String description, String typeString, User author) {

		// amount cannot be zero or less
		if (amount <= 0) {
			log.warn("Illegal amount: " + amount + " received. Reimbursement was not created");
			return false;
		}
		// author cannot be null
		if (author == null) {
			log.warn("Attempting to insert reimbursement with null author. Reimbursement was not created.");
			return false;
		}

		// Convert type from String to enum
		ReimbursementType reimbType = ReimbursementType.INVALID;
		for (ReimbursementType type : ReimbursementType.values()) {
			if (typeString.equals(type.getAsString())) {
				reimbType = type;
			}
		}

		// If we couldn't find a matching type
		if (reimbType == ReimbursementType.INVALID) {
			System.out.println();
			log.warn("Attempting to update with INVALID status");
		}

		// Get the current time
		Timestamp submittedTime = Timestamp.from(ZonedDateTime.now().toInstant());

		// Create the reimbursement that will be sent to DB
		Reimbursement toBeSubmitted = new Reimbursement();
		toBeSubmitted.setAmount(amount);
		toBeSubmitted.setDescription(description);
		toBeSubmitted.setSubmittedTime(submittedTime);
		toBeSubmitted.setAuthor(author);
		toBeSubmitted.setReimbType(reimbType);
		toBeSubmitted.setReimbStatus(ReimbursementStatus.PENDING);

		return reimbDao.insertReimbursement(toBeSubmitted);
	}

	@Override
	public List<Reimbursement> selectAllReimbursements() {
		List<Reimbursement> reimbList = reimbDao.selectAllReimbursements();

		return reimbList;
	}

	@Override
	public List<Reimbursement> selectReimbursementsByEmployee(int userID) {
		// userID cannot be zero or less (DB's SERIAL starts at 1)
		if (userID <= 0) {
			log.warn(
					"Attempting to select reimbursements authored by invalid user id: " + userID + ". Returning null.");
			return null;
		}

		List<Reimbursement> reimbList = reimbDao.selectReimbursementsByEmployee(userID);

		return reimbList;
	}

	@Override
	public Reimbursement selectReimbursementByID(int reimbID) {
		// reimbID cannot be zero or less (DB's SERIAL starts at 1)
		if (reimbID <= 0) {
			log.warn("Attempting to select reimbursement with invalid id: " + reimbID + ". Returning null.");
			return null;
		}

		Reimbursement reimb = reimbDao.selectReimbursementByID(reimbID);

		return reimb;
	}

	@Override
	public boolean updateReimbursement(int reimbID, String statusString, User resolver) {
		// reimbID cannot be zero or less (DB's SERIAL starts at 1)
		if (reimbID <= 0) {
			log.warn("Attempting to select reimbursement with invalid id: " + reimbID 
					+ ". Reimbursement was not created.");
			return false;
		}
		// resolver cannot be null
		if (resolver == null) {
			log.warn("Attempting to update reimbursement with null resolver. Reimbursement was not created.");
			return false;
		}

		// Convert status from String to enum
		ReimbursementStatus newStatus = ReimbursementStatus.INVALID;
		for (ReimbursementStatus status : ReimbursementStatus.values()) {
			if (statusString.equals(status.getAsString())) {
				newStatus = status;
			}
		}

		// If we couldn't find a matching status
		if (newStatus == ReimbursementStatus.INVALID) {
			System.out.println();
			log.warn("Attempting to update with INVALID status");
		}

		// Get the current time
		Timestamp submittedTime = Timestamp.from(ZonedDateTime.now().toInstant());

		return reimbDao.updateReimbursement(reimbID, newStatus, submittedTime, resolver);
	}

}
