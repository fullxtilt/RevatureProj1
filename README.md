# Expense Reimbursement System (ERS)
A reimbursement system created for a faux company. Employees can log in, view their reimbursement requests, and submit new requests. Finance managers can log in, view reimbursement requests from ALL employees, and accept or deny them. Information on user accounts and reimbursements is stored in a GCP Postgres database and the system's server is deployed using Javalin. My ERS is themed after Monster Hunter World for some additional flavor.

# Technologies Used
* Java
* JavaScript 
* HTML 
* CSS
* JDBC 
* PostgreSQL 
* Javalin

# Features
* List of reimbursements viewable to user will display on login.
* Finance manager accounts can approve/deny requests.
* Users can submit reimbursement requests.
* Finance managers can filter requests by status.
To-do List:
* Employee accounts can submit an image file along with their request to verify their expenses. 
* Hash users' passwords to improve security.

# Getting Started
```
git clone https://github.com/fullxtilt/RevatureProj1.git
```
Environment Variables:
* TRAINING_DB_REIMB     | Database Name
* TRAINING_DB_HOST      | Database Host IP
* TRAINING_DB_USERNAME  | Username
* TRAINING_DB_PASSWORD  | Password  
Port:  
9006  
Existing Accounts (username / pass):  
* hunter1 / pass
* hunter2 / pass
* admin1 / pass
