import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.ToDo;

public class MysqlConnector {

	private static String dbName = null;
	private static String dbTable = "todo";
	private static String dbUser = null;
	private static String dbPassword = null;

	public MysqlConnector(Scanner s, String db) {
		try {
			// The newInstance() call is a work around for some
			// broken Java implementations
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			System.out.println("JDBC driver registered");
			dbName = db;
			s = new Scanner(System.in);  // Reading from System.in
			System.out.println("Enter the database username: ");
			dbUser = s.next();
			System.out.println("Enter the database password: ");
			dbPassword = s.next();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public MysqlConnector(Scanner s, String db, String user, String pass) {
		try {
			// The newInstance() call is a work around for some
			// broken Java implementations
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			dbName = db;
			dbUser = user;
			dbPassword = pass;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public String getUser() {
		return dbUser;
	}
	
	public String getPassword() {
		return dbPassword;
	}
	
	public Connection getConnection() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/?" + 
					"user=" + dbUser + "&password=" + dbPassword + "&useSSL=false");
			//System.out.println("Got Mysql database connection");
			return conn;
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return null;
	}

	public void createDB(Connection conn, String dbname) {
		try {
			PreparedStatement createDB = conn.prepareStatement("CREATE DATABASE IF NOT EXISTS " + dbname);
			createDB.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Database creation failed");
			e.printStackTrace();
		}
	}

	public void createTable(Connection conn) {
		try {
			PreparedStatement createDB = conn.prepareStatement("CREATE TABLE IF NOT EXISTS " 
					+ dbName + "." + dbTable + "("
					+ "id 			INT NOT NULL AUTO_INCREMENT, "
					+ "todo_message VARCHAR(100) NOT NULL, "
					+ "timestamp 	DATE NOT NULL, "
					+ "PRIMARY KEY (id) " + ")");
			createDB.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Table creation failed");
			e.printStackTrace();
		}
	}

/*
 * ​POST [id] [todo message]” : 
 * Stores the string ‘todo message’ in the database with the supplied integer ‘id’ 
 * and the client’s timestamp. Overwrite any existing values.
 */
	public boolean insertToDo(ToDo todo) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			// Get the connection to the database
			con = getConnection();
			// Execute the query
			stmt = con.prepareStatement("INSERT INTO " + dbName + "." + dbTable + " VALUES(?,?,?)");
			stmt.setInt(1, todo.getId());
			stmt.setString(2, todo.getMessage());
			stmt.setTimestamp(3, todo.getTimestamp());
			//System.out.println(stmt);
			return stmt.executeUpdate() > 0;
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} finally {
			// it is a good idea to release
			// resources in a finally{} block
			// in reverse-order of their creation
			// if they are no-longer needed
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
				} // ignore
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore
				stmt = null;
			}
			if(con != null){
				try {
					con.close();
				} catch (SQLException sqlEx) {
				} // ignore
				con = null;
			}
		}
		return false;
	};

	public ToDo getToDoMessage(int id) {
		// Get the connection to the database
		Connection conn = getConnection();
		ToDo message = new ToDo();
		if (conn != null) {
			PreparedStatement stmt = null;
			ResultSet rs = null;
			// Execute the query
			try {
				stmt = conn.prepareStatement("SELECT * FROM " + dbName + "." + dbTable + " WHERE id = ?");
				stmt.setInt(1, id);
				//System.out.println(stmt);
				rs = stmt.executeQuery();
				rs.next();
				message.setId(rs.getInt("id"));
				message.setToDoMessage(rs.getString("todo_message"));
				message.setTimestamp(rs.getTimestamp("timestamp"));
				return message;
			} catch (SQLException ex) {
				// handle any errors
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			} finally {
				// it is a good idea to release
				// resources in a finally{} block
				// in reverse-order of their creation
				// if they are no-longer needed

				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException sqlEx) {
					} // ignore

					rs = null;
				}

				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException sqlEx) {
					} // ignore

					stmt = null;
				}
				if(conn != null){
					try {
						conn.close();
					} catch (SQLException sqlEx) {
					} // ignore

					conn = null;
				}

			}
		}
		return message;
	};

	public List<ToDo> getAllMessages() {
		// Get the connection to the database
		Connection conn = getConnection();
		List<ToDo> messages = new ArrayList<ToDo>();

		if (conn != null) {
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				// Execute the query
				stmt = conn.prepareStatement("SELECT * FROM " + dbName + "." + dbTable);
				rs = stmt.executeQuery();
				while (rs.next()) {
					ToDo message = new ToDo();
					message.setId(rs.getInt("id"));
					message.setToDoMessage(rs.getString("todo_message"));
					message.setTimestamp();
					messages.add(message);
				}
				return messages;
			} catch (SQLException ex) {
				// handle any errors
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			} finally {
				// it is a good idea to release
				// resources in a finally{} block
				// in reverse-order of their creation
				// if they are no-longer needed

				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException sqlEx) {
					} // ignore

					rs = null;
				}

				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException sqlEx) {
					} // ignore

					stmt = null;
				}
				if(conn != null){
					try {
						conn.close();
					} catch (SQLException sqlEx) {
					} // ignore

					conn = null;
				}

			}
		}
		return messages;
	}

	public boolean deleteMessage(int id) {
		// Get the connection to the database
		Connection conn = getConnection();
		if (conn != null) {
			PreparedStatement stmt = null;
			// Execute the query
			try {
				stmt = conn.prepareStatement("DELETE FROM " + dbName + "." + dbTable + " WHERE id = ?");
				stmt.setInt(1, id);
				//System.out.println(stmt);
				stmt.executeUpdate();
				return true;
			} catch (SQLException ex) {
				// handle any errors
				System.out.println("SQLException: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
			} finally {
				// it is a good idea to release
				// resources in a finally{} block
				// in reverse-order of their creation
				// if they are no-longer needed
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException sqlEx) {
					} // ignore
					stmt = null;
				}
				if(conn != null){
					try {
						conn.close();
					} catch (SQLException sqlEx) {
					} // ignore
					conn = null;
				}
			}
		}
		return false;
	}

}
