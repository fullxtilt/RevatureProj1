package reimbsystem.model;

import java.sql.Timestamp;

public class Reimbursement {

	/**
	 * Denotes the three stages of reimbursement.
	 * <p>
	 * String values correspond to the ers_reimbursement_status table in ReimbursementDB
	 * @author 12066
	 *
	 */
	public enum ReimbursementStatus {
		INVALID("INVALID"),
		PENDING("PENDING"),
		APPROVED("APPROVED"),
		DENIED("DENIED");
		
		private String eString;
		
		private ReimbursementStatus(String eString) {
			this.eString = eString;
		}
		
		public String getAsString() { return eString; }
	}
	
	/**
	 * Denotes the types of reimbursement
	 * <p>
	 * String values correspond to the ers_reimbursement_type table in ReimbursementDB
	 * @author 12066
	 *
	 */
	public enum ReimbursementType {
		INVALID("INVALID"),
		LODGING("LODGING"),
		TRAVEL("TRAVEL"),
		FOOD("FOOD"),
		OTHER("OTHER");
		
		private String eString;
		
		private ReimbursementType(String eString) {
			this.eString = eString;
		}
		
		public String getAsString() { return eString; }
	}
	
	// Attributes
	//-----------------------------------------------
	private int myID;
	private double amount;
	private String description;
	private Timestamp submittedTime, resolvedTime;
	private User author, resolver;
	private ReimbursementStatus reimbStatus;
	private ReimbursementType reimbType;
	
	// Constructors
	//--------------------------------------------
	public Reimbursement() {
		
	};
	
	public Reimbursement(int myID, double amount, Timestamp submittedTime, Timestamp resolvedTime, String description,
			User author, User resolver, ReimbursementStatus reimbStatus, ReimbursementType reimbType) {
		super();
		this.myID = myID;
		this.amount = amount;
		this.submittedTime = submittedTime;
		this.resolvedTime = resolvedTime;
		this.description = description;
		this.author = author;
		this.resolver = resolver;
		this.reimbStatus = reimbStatus;
		this.reimbType = reimbType;
	}

	// Getters/Setters
	//-------------------------------------------------
	public int getMyID() { return myID; }
	public void setMyID(int myID) { this.myID = myID; }

	public double getAmount() { return amount; }
	public void setAmount(double amount) { this.amount = amount; }

	public Timestamp getSubmittedTime() { return submittedTime; }
	public void setSubmittedTime(Timestamp submittedTime) { this.submittedTime = submittedTime; }

	public Timestamp getResolvedTime() { return resolvedTime; }
	public void setResolvedTime(Timestamp resolvedTime) { this.resolvedTime = resolvedTime; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public User getAuthor() { return author; }
	public void setAuthor(User author) { this.author = author; }
	
	public User getResolver() { return resolver; }
	public void setResolver(User resolver) { this.resolver = resolver; }

	public ReimbursementStatus getReimbStatus() { return reimbStatus; }
	public void setReimbStatus(ReimbursementStatus reimbStatus) { this.reimbStatus = reimbStatus; }

	public ReimbursementType getReimbType() { return reimbType; }
	public void setReimbType(ReimbursementType reimbType) { this.reimbType = reimbType; }

	
	// Other Methods
	//----------------------------------------------------
	@Override
	public String toString() {
		return "Reimbursement [myID=" + myID + ", amount=" + amount + ", description=" + description
				+ ", submittedTime=" + submittedTime + ", resolvedTime=" + resolvedTime + ", author=" + author
				+ ", resolver=" + resolver + ", reimbStatus=" + reimbStatus + ", reimbType=" + reimbType + "]";
	}
	
	
	
}
