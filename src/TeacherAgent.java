import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import jade.content.onto.basic.Done;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;

/*
 * FIXME:
 * develop some strategy for the agent when it can't access the database.
 * develop some strategy for the agent when there's a problem with tweeting.
 * */
public class TeacherAgent extends Agent {
	public TwitterFactory factory;
	private static final long serialVersionUID = 1L;
	private ArrayList<TweetingRule> listOfRules;

	protected void setup() {
		String[] args = (String[]) getArguments();
		listOfRules = new ArrayList<TweetingRule>();
		setupTwitter();
		addBehaviour(new LoadTweetingRules(this, 10000));

	}
/*TODO:
 * Add authentication keys from the config file
 * */
	private void setupTwitter() {
		twitter4j.conf.ConfigurationBuilder cb2 = new twitter4j.conf.ConfigurationBuilder();
		cb2.setDebugEnabled(true)
				.setOAuthConsumerKey("")
				.setOAuthConsumerSecret(
						"")
				.setOAuthAccessToken(
						"")
				.setOAuthAccessTokenSecret(
						"");
		factory = new TwitterFactory(cb2.build());

	}

	protected void takeDown() {
		System.out.println("I, " + getAID().getName()
				+ " is terminating. Goodbye!");
	}

	public ArrayList<TweetingRule> getRules() {
		return listOfRules;
	}

	public void addToRules(TweetingRule _rule) {
		listOfRules.add(_rule);
	}

	public void clearRules() {
		listOfRules.clear();
	}
}

class LoadTweetingRules extends TickerBehaviour {
	public LoadTweetingRules(Agent a, long period) {
		super(a, period);
		// TODO Auto-generated constructor stub
	}

	@Override
	/*
	 * TODO Change the way the rules gets updates, so you don't need to get the
	 * whole list every time this cyclic behaviour happens.
	 */
	protected void onTick() {
		TeacherAgent myParent = (TeacherAgent) myAgent;
		myParent.clearRules();
		System.out.println("I will now get the list of rules");
		try {
			dbManager mydb = new dbManager();
			ResultSet myResults = mydb.getAllRules();
			while (myResults.next()) {
				System.out.println(myResults.getString("thenTweet"));
				TweetingRule tr = new TweetingRule(myResults.getString("who"),
						myResults.getString("containsThis"),
						myResults.getString("containsThat"),
						myResults.getString("thenTweet"),
						myResults.getString("notThis"), ";");
				myParent.addToRules(tr);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Error in adding to the class!");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Error in adding to the class!");
			e.printStackTrace();
		}
		myAgent.addBehaviour(new RespondToTweets());
	}
}

class RespondToTweets extends SimpleBehaviour {

	/*
	 * TODO get list of all tweets that are unanswered check the tweets against
	 * the rules send a tweet if any rules matches.
	 */
	@Override
	public void action() {
		Twitter twitter ;
		System.out.println("inside respondtotweets!");
		ArrayList<Tweet> tweetsList = new ArrayList<Tweet>();
		dbManager mydb;
		try {
			mydb = new dbManager();
			tweetsList = mydb.getUnansweredTweets();
			System.out.println("number of tweets to respond to is : "
					+ tweetsList.size());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		boolean finishIt = false;
		ArrayList<TweetingRule> allRules = ((TeacherAgent) myAgent).getRules();
		for (Tweet t : tweetsList) {
			System.out.println("Checking this one now: " + t.tweetText);
			for (TweetingRule rule : allRules) {
				System.out.println("Checking this one now: " + t.tweetText
						+ "against this rule: " + rule.toString());
				if (rule.checkIfMatch(t.tweetText)) {
					System.out.println("yes this one matched!!!!!");
					 
					/*
					 * TODO: here put the code to tweet the message from rule.
					 */
					twitter = ((TeacherAgent) myAgent).factory.getInstance();
		         	try {
						twitter.updateStatus("@"+ t.userScreenName + " " + rule.getRandomTweet() + " #edcmooc " 	+ getRandomString() );//Twitter status update.
						mydb.changeTweetAction(t.id,2);//updating this record as a tweet that has already being dealt with.
						finishIt=true;
						break;
					} catch (TwitterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(finishIt)break;
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return true;
	}
	public String getRandomString(){
		String result;
		result=Double.toString(Math.random()).substring(0,9);
		result = result.replace("1", ".").replace("2", " ").replace("3", "-").replace("0", "_").replace("9", " ").replace("8", "_").replace("7", "-").replace("6", " ").replace("5", ".").replace("4", "_");
		return result;
	}

}

@SuppressWarnings("serial")
class GetListOfRulesBehaviour extends SimpleBehaviour {

	@Override
	public void action() {
		TeacherAgent myParent = (TeacherAgent) myAgent;
		System.out.println("I will now get the list of rules");
		try {
			dbManager mydb = new dbManager();
			ResultSet myResults = mydb.getAllRules();
			while (myResults.next()) {
				System.out.println(myResults.getString("thenTweet"));
				TweetingRule tr = new TweetingRule(myResults.getString("who"),
						myResults.getString("containsThis"),
						myResults.getString("containsThat"),
						myResults.getString("thenTweet"),
						myResults.getString("notThis"), ";");
				myParent.addToRules(tr);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Error in adding to the class!");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Error in adding to the class!");
			e.printStackTrace();
		}
	}

	@Override
	public boolean done() {
		// myAgent.addBehaviour(new checkDatabaseBehaviour(myAgent,1200000));
		return true;
	}

}

@SuppressWarnings("serial")
class updateTwitterBehaviour extends SimpleBehaviour {
	@Override
	/*
	 * check the tweet against all the rules. tweet regarding to the matching
	 * rules.
	 */
	public void action() {
		// TODO Auto-generated method stub

	}

	@Override
	/*
	 * FIXME: add a method that will keep an autoincremented number to be added
	 * to the end of each tweet to keep it unique. This will be stored in the
	 * database, so after each time the agent tweet the same message this will
	 * update the number in the database.
	 */
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
}


@SuppressWarnings("serial")
class checkDatabaseBehaviour extends TickerBehaviour {
	public checkDatabaseBehaviour(Agent a, long period) {
		super(a, period);
		// TODO Auto-generated constructor stub
	}

	@Override
	/*
	 * FIXME: check the database and if there's any new tweet then add a new
	 * behaviour for it.
	 */
	protected void onTick() {

	}

}
