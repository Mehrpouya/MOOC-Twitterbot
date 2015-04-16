import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class dbManager {
	private static final String URL = "";
	private static final String USER = "";
	private static final String PASSWORD = "";
	private Statement statement = null;
	private ResultSet resultSet = null;
	private static Connection connection;
	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

	public dbManager() throws ClassNotFoundException, SQLException {
		// setup connection with the db
		Connection connection = getConnection();
		// statements allow to issue SQL queries to the database
		statement = (Statement) connection.createStatement();
		resultSet = statement.executeQuery("select * from MOOCEDC.tweets");
	}
	
	public static Connection getConnection()
	{
				
		try {
			Class.forName
			(DRIVER_CLASS);	//Class.forName("myDriver.ClassName"); ?

		} catch(java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		try {
			connection = (Connection) DriverManager.getConnection(URL,
					USER, PASSWORD);

		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		
		return connection;
	}
	
	public ResultSet getAllRules(){
		Connection connection = getConnection();
		// statements allow to issue SQL queries to the database
		ResultSet myResultSet=null;
		try {
			Statement myStatement = (Statement) connection.createStatement();
			myResultSet = myStatement.executeQuery("select * from MOOCEDC.tweetRules");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myResultSet;
	}

	public String writeResultSet(ResultSet resultSet) throws SQLException {
		// resultSet is initialised before the first data set
		while (resultSet.next()) {
			String comment = resultSet.getString("tweetText");
			return(comment);
		}
		return null;
	}
	
	public ArrayList<Tweet> getUnansweredTweets(){
		ArrayList<Tweet> ut = new ArrayList<Tweet>();
		
		try {
			statement = (Statement) connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM MOOCEDC.`tweets` WHERE `action` =1");
			while (resultSet.next()) {
				System.out.println(resultSet.getString("tweetText"));
				Tweet t = new Tweet(resultSet.getLong("id"),resultSet.getLong("userId"), resultSet.getString("userURL"), 
									resultSet.getString("userName"), resultSet.getString("userScreenName"),
									resultSet.getString("userStatus"), resultSet.getString("userTimezone"),
									resultSet.getString("tweetText"), resultSet.getDate("tweetDate"),
									resultSet.getFloat("tweetLongitude"), resultSet.getFloat("tweetLatitude"), 
									resultSet.getLong("tweetId"),resultSet.getString("tweetCountry"),
									resultSet.getString("tweetPlaceFullName"));
				System.out.println(t);
				System.out.println("££££££££££££££££££££££££££££££££££££££££ " + resultSet.getString("userScreenName"));
				ut.add(t);
//						resultSet.getString("thenTweet"), ",");
//				myParent.addToRules(tr);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return ut;
	}
	
	public void changeTweetAction(long _id, int _action){
		try {
			statement = (Statement) connection.createStatement();
			statement.executeUpdate("UPDATE `tweets` SET `action`="+ _action+" WHERE `id`= " + _id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}