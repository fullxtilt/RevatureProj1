package reimbsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import reimbsystem.MyLogger;
import reimbsystem.model.Reimbursement;
import reimbsystem.model.Reimbursement.ReimbursementStatus;
import reimbsystem.model.Reimbursement.ReimbursementType;
import reimbsystem.model.User;

public class ReimbursementDaoImpl implements ReimbursementDao {

	@Override
	public boolean insertReimbursement(Reimbursement reimb) {

		int numRowsChanged = 0;

		try (Connection conn = DBConnectionFactory.getConnection()) {

			String sql = "INSERT INTO ers_reimbursement("
					+ "reimb_amount"
					+ ", reimb_submitted"
					+ ", reimb_description"
					+ ", reimb_author"
					+ ", reimb_status_id"
					+ ", reimb_type_id)" 
				+ "VALUES (?,?,?,?,?,?);";

			PreparedStatement ps = conn.prepareStatement(sql);
			
			ps.setDouble(1, reimb.getAmount());
			ps.setTimestamp(2, reimb.getSubmittedTime());
			ps.setString(3, reimb.getDescription());
			ps.setInt(4, reimb.getAuthor().getMyID());
			ps.setInt(5, reimb.getReimbStatus().ordinal());
			ps.setInt(6, reimb.getReimbType().ordinal());
			

			numRowsChanged = ps.executeUpdate();

		} catch (SQLException e) {
			Logger log = MyLogger.getLoggerForClass(this);
			log.error(e);
			e.printStackTrace();
			return false;
		}

		return (numRowsChanged > 0);

	}
	
	@Override
	public List<Reimbursement> selectAllReimbursements() {
		
		List<Reimbursement> reimbursementList = new ArrayList<>();

		try (Connection conn = DBConnectionFactory.getConnection()) {

			String sql = "SELECT * FROM ers_complete_reimbursement_view";

			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			// fill with users
			while (rs.next()) {
				reimbursementList.add(createReimbursementFromRS(rs));
			}

		} catch (SQLException e) {
			Logger log = MyLogger.getLoggerForClass(this);
			log.error(e);
			e.printStackTrace();
		}

		return reimbursementList;
	}

	@Override
	public List<Reimbursement> selectReimbursementsByEmployee(int userID) {
		
		List<Reimbursement> reimbursementList = new ArrayList<>();

		try (Connection conn = DBConnectionFactory.getConnection()) {

			String sql = "SELECT * FROM ers_complete_reimbursement_view WHERE reimb_author_id = ?";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, userID);

			ResultSet rs = ps.executeQuery();

			// fill with users
			while (rs.next()) {
				reimbursementList.add(createReimbursementFromRS(rs));
			}

		} catch (SQLException e) {
			Logger log = MyLogger.getLoggerForClass(this);
			log.error(e);
			e.printStackTrace();
		}

		return reimbursementList;
	}
	
	@Override
	public Reimbursement selectReimbursementByID(int reimbID) {
		
		try (Connection conn = DBConnectionFactory.getConnection()) {

			String sql = "SELECT * FROM ers_complete_reimbursement_view WHERE reimb_id = ?";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, reimbID);

			ResultSet rs = ps.executeQuery();

			// fill with users
			if (rs.next()) {
				return createReimbursementFromRS(rs);
			}

		} catch (SQLException e) {
			Logger log = MyLogger.getLoggerForClass(this);
			log.error(e);
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean updateReimbursement(int reimbID, ReimbursementStatus newStatus, Timestamp resolvedTime, User resolver) {
		
		int numRowsChanged = 0;
		
		try (Connection conn = DBConnectionFactory.getConnection()) {

			String sql = "UPDATE ers_reimbursement SET "
					+ "reimb_resolved = ?"
					+ ", reimb_resolver = ?"
					+ ", reimb_status_id = ?"
					+ " WHERE reimb_id = ?;";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setObject(1, resolvedTime);
			ps.setInt(2, resolver.getMyID());
			ps.setInt(3, newStatus.ordinal());
			ps.setInt(4, reimbID);
			
			numRowsChanged = ps.executeUpdate();

		} catch (SQLException e) {
			Logger log = MyLogger.getLoggerForClass(this);
			log.error(e);
			e.printStackTrace();
			return false;
		}
		
		return (numRowsChanged > 0);
	}
	
	private Reimbursement createReimbursementFromRS(ResultSet rs) throws SQLException {
		ReimbursementStatus reimbStatus = ReimbursementStatus.values()[rs.getInt("reimb_status_id")];
		ReimbursementType reimbType = ReimbursementType.values()[rs.getInt("reimb_type_id")];

		User author = new User();
		author.setMyID(rs.getInt("reimb_author_id"));
		author.setUsername(rs.getString("ers_author_username"));
		author.setFirstName(rs.getString("ers_author_firstname"));
		author.setLastName(rs.getString("ers_author_lastname"));
		
		User resolver = new User();
		resolver.setMyID(rs.getInt("reimb_resolver_id"));
		resolver.setUsername(rs.getString("ers_resolver_username"));
		resolver.setFirstName(rs.getString("ers_resolver_firstname"));
		resolver.setLastName(rs.getString("ers_resolver_lastname"));

		return new Reimbursement(
				rs.getInt("reimb_id"),
				rs.getDouble("reimb_amount"),
				rs.getTimestamp("reimb_submitted"),
				rs.getTimestamp("reimb_resolved"),
				rs.getString("reimb_description"),
				author,
				resolver,
				reimbStatus,
				reimbType);
	}

}
