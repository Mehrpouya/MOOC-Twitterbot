
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class Tweet {
	public long id = 0;
	public long userId = 0;
	public String userURL = "a";
	public String userName = "a";
	public String userScreenName = "a";
	public String userStatus = "a";
	public String userTimezone = "CURRENT_TIMESTAMP";
	public String tweetText = "q";
	public java.sql.Date tweetDate;
	public double tweetLongitude = 0;
	public double tweetLatitude = 0;
	public long tweetId = 0;
	public String tweetCountry = "sdfsf";
	public String tweetPlaceFullName = "sdfsdf";

	public Tweet() {

	}
	/*
	 * Class to hold database values.
	 * 
	 * @param _userId twitter Id of the user.
	 * @param _userURL user profile link
	 * @param _userName username!
	 * @param _userScreenName twitter screen name
	 * @param _userStatus twitter status text
	 * @param _userTimezone time of the message
	 * @param _tweetLatitude is the user allowed geolocation this is where they tweeted from
	 * @param _tweetId id of this tweet
	 * @param _tweetCountry which country the user tweeted from
	 * @param _tweetPlaceFullName full name of the location the user tweeted from.
	 * @return instance of Tweet class. this is identical to the MOOCEDC.tweets table structure.
	 */
	public Tweet(long _id, long _userId,String _userURL, String _userName, String _userScreenName, String _userStatus, String _userTimezone,
				 String _tweetText, java.sql.Date _tweetDate, float _tweetLongitude, 
				 float _tweetLatitude, long _tweetId, String _tweetCountry, String _tweetPlaceFullName) {
		id=_id;
		userId=_userId;
		userURL=_userURL;
		userName=_userName;
		userScreenName=_userScreenName;
		userStatus=_userStatus;
		userTimezone=_userTimezone;
		tweetText=_tweetText;
		tweetDate=_tweetDate;
		tweetLatitude=_tweetLatitude;
		tweetLongitude=_tweetLongitude;
		tweetId=_tweetId;
		tweetCountry=_tweetCountry;
		tweetPlaceFullName=_tweetPlaceFullName;

	}

	public String getAllValues() {
		String allVals = "'" + userId + "','" + userName + "','"
				+ userScreenName + "','" + userStatus + "'," + "'" + tweetText
				+ "','" + tweetDate + "','" + tweetId + "',1";
		return allVals;
	}

	public String getAllNames() {
		/*
		 * String allNames = "'userId','userName'," +
		 * "'userScreenName','userStatus'," +
		 * "'userTimezone','tweetText','tweetDate'," +
		 * "'tweetLongitude','tweetLatitude','tweetId'," +
		 * "'tweetCountry','tweetPlaceFullName'";
		 */
		String allNames = "userId,userName," + "userScreenName,userStatus,"
				+ "tweetText,tweetDate," + "tweetId," + "action";

		return allNames;
	}

}
