package reimbsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import reimbsystem.MyLogger;
import reimbsystem.model.User;
import reimbsystem.model.User.UserRole;

public class UserDaoImpl implements UserDao {

	@Override
	public boolean insertUser(User newUser) {

		int numRowsChanged = 0;

		try (Connection conn = DBConnectionFactory.getConnection()) {

			String sql = "INSERT INTO ers_users(" + "ers_username, ers_password," + "user_first_name, user_last_name,"
					+ "user_email, user_role_id) " + "VALUES (" + "?, ?, ?, ?, ?, ?" + ");";

			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, newUser.getUsername());
			ps.setString(2, newUser.getPassword());
			ps.setString(3, newUser.getFirstName());
			ps.setString(4, newUser.getLastName());
			ps.setString(5, newUser.getEmail());
			ps.setInt(6, newUser.getRole().ordinal());

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
	public List<User> selectAllUsers() {

		List<User> userList = new ArrayList<>();

		try (Connection conn = DBConnectionFactory.getConnection()) {

			String sql = "SELECT * FROM ers_complete_user_view";

			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			// fill with users
			while (rs.next()) {
				UserRole role = UserRole.values()[rs.getInt("ers_user_role_id")];

				userList.add(new User(rs.getInt("ers_users_id"), rs.getString("ers_username"),
						rs.getString("ers_password"), rs.getString("user_first_name"), rs.getString("user_last_name"),
						rs.getString("user_email"), role));
			}

		} catch (SQLException e) {
			Logger log = MyLogger.getLoggerForClass(this);
			log.error(e);
			e.printStackTrace();
		}

		return userList;
	}

	@Override
	public User selectUserByUsername(String username) {

		try (Connection conn = DBConnectionFactory.getConnection()) {

			String sql = "SELECT * FROM ers_complete_user_view WHERE ers_username = ?";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);

			ResultSet rs = ps.executeQuery();

			// fill user object with information
			if (rs.next()) {
				UserRole role = UserRole.values()[rs.getInt("ers_user_role_id")];

				User targetUser = new User(rs.getInt("ers_users_id"), rs.getString("ers_username"),
						rs.getString("ers_password"), rs.getString("user_first_name"), rs.getString("user_last_name"),
						rs.getString("user_email"), role);

				return targetUser;
			}

		} catch (SQLException e) {
			Logger log = MyLogger.getLoggerForClass(this);
			log.error(e);
			e.printStackTrace();
		}

		return null;
	}

}
