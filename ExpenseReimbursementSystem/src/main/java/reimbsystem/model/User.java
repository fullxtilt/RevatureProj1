package reimbsystem.model;

public class User {
	
	/**
	 * Differentiates between user roles. 
	 * <p>
	 * String values correspond to the ers_user_roles table in ReimbursementDB
	 * @author 12066
	 *
	 */
	public enum UserRole {
		INVALID ("INVALID"),
		EMPLOYEE ("EMPLOYEE"),
		FINANCE_MANAGER ("FINANCE MANAGER");
		
		private final String eString;
		
		private UserRole(String eString) {
			this.eString = eString;
		}
		
		public String getAsString() {
			return eString;
		}
	}
	
	// Attributes
	//--------------------------------------
	private int myID;
	private String username, password,
	firstName, lastName, email;
	private UserRole role;

	// Constructors
	//--------------------------------------------------
	public User() {
		
	}
	
	public User(int myID, String username, String password, String firstName, String lastName, String email, UserRole role) {
		super();
		this.myID = myID;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.role = role;
	}

	// Getters/Setters
	//-------------------------------------------------------------------
	public int getMyID() { return myID; }
	public void setMyID(int myID) { this.myID = myID; }
	
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }

	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public UserRole getRole() { return role; }
	public void setRole(UserRole role) { this.role = role; }

	// Other Methods
	//---------------------------------
	@Override
	public String toString() {
		return "User [myID=" + myID + ", username=" + username + ", password=" + password + ", firstName=" + firstName + ", lastName="
				+ lastName + ", email=" + email + ", role=" + role + "]\n";
	}
	
}
