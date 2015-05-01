package Business;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.UserMentionEntity;
import twitter4j.conf.ConfigurationBuilder;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Statistics {

	private static ConfigurationBuilder configBuilder;
	private DB db;
	private DBCollection dbColl;

	public Statistics() {
		try {
			configBuilderInitialization();
			mongoDBInitialization();
		} catch (MongoException ex) {
			System.out.println("Mong Exception: " + ex.getMessage());
		} catch (UnknownHostException e) {
			System.out.println("UnKnown Exception" + e.getMessage());
		}
	}

	private void configBuilderInitialization() {
		configBuilder = new ConfigurationBuilder();
		configBuilder.setOAuthConsumerKey("kzIAfvYaZH8ItEoYnHPjgGx10");
		configBuilder
				.setOAuthConsumerSecret("4YdRd3aS65h8SlvFRHr4bbr7frZRhMVbUFtizSwiPjgSJkcTvO");
		configBuilder
				.setOAuthAccessToken("3180342006-rszIO9DvHVDUO4XfeY6wRR1MMqIWnTzWUXIpcvW");
		configBuilder
				.setOAuthAccessTokenSecret("hRyHiqBSy0XhW6fyg5ZyFBrTebdCFlKgHEdXMICAk9ND0");
		configBuilder.setDebugEnabled(true);
	}

	private void mongoDBInitialization() throws MongoException,
			UnknownHostException {
		try {
			Mongo mongo;
			mongo = new Mongo("127.0.0.1");
			db = mongo.getDB("XceedTutDB");
			dbColl = db.getCollection("TopTweets");
		} catch (UnknownHostException ex) {
			System.out.println("Unknown Host Exception in connection: "
					+ ex.getMessage());
			throw ex;
		} catch (MongoException me) {
			System.out.println("DB Mongo Exception in connection: "
					+ me.getMessage());
			throw me;
		}
	}

	public List<TweetInfo> getTopTweets(boolean isNewDb) {

		List<TweetInfo> tweetInfo = new ArrayList();

		if (configBuilder != null) {
			List<Status> tweets = tweetsSetting();
			for (Status tweet : tweets) {
				BasicDBObject basicDbObj = new BasicDBObject();
				basicDbObj.put("tweet_iD", tweet.getId());
				basicDbObj.put("tweet_text", tweet.getText());
				basicDbObj.put("user_name", tweet.getUser().getScreenName());
				basicDbObj.put("retweet_count", tweet.getRetweetCount());
				basicDbObj.put("tweet_followers_count", tweet.getUser()
						.getFollowersCount());
				UserMentionEntity[] mentioned = tweet.getUserMentionEntities();
				basicDbObj.put("tweet_mentioned_count", mentioned.length);
				try {
					dbColl.insert(basicDbObj);
				} catch (Exception e) {
					System.out.println("MongoDB Connection Error : "
							+ e.getMessage());
				}
			}

			if (isNewDb) {
				tweetInfo = fitchTweets();

			}
		}
		return tweetInfo;
	}

	private List<Status> tweetsSetting() {
		TwitterFactory twitterFactory = new TwitterFactory(
				configBuilder.build());
		Twitter twitter = twitterFactory.getInstance();
		try {
			Query query = new Query("Islam invitation");
			query.setCount(100);
			QueryResult result;
			result = twitter.search(query);
			return result.getTweets();
		} catch (TwitterException te) {
			System.out.println("Exception in twitter account \n error code# "
					+ te.getErrorCode());
			return null;
		}
	}

	public List<TweetInfo> getTopRetweet() {
		List<TweetInfo> retweetInfo = searchTweets("retweet_count");
		return retweetInfo;
	}

	public List<TweetInfo> getTopMentioned() {
		List<TweetInfo> retweetInfo = searchTweets("tweet_mentioned_count");
		return retweetInfo;
	}

	public List<TweetInfo> getTopFollowers() {
		List<TweetInfo> retweetInfo = searchTweets("tweet_followers_count");
		return retweetInfo;
	}

	public List<TweetInfo> searchTweets(String creteria) {
		if (dbColl.count() <= 0) {
			getTopTweets(false);
		}
		BasicDBObject dbObj = new BasicDBObject();
		dbObj.put(creteria, -1);
		DBCursor dbCursor = dbColl.find().sort(dbObj).limit(5);

		List<TweetInfo> retweetInfo = fillTweetInfo(dbCursor);
		return retweetInfo;

	}

	public List<TweetInfo> fitchTweets() {

		BasicDBObject tweetDbObj = new BasicDBObject("_id", false).append(
				"user_name", true).append("tweet_text", true);
		DBCursor tweetDbCursor = dbColl.find(new BasicDBObject(), tweetDbObj)
				.limit(100);

		List<TweetInfo> tweets = fillTweetInfo(tweetDbCursor);
		return tweets;
	}

	private List<TweetInfo> fillTweetInfo(DBCursor tweetDbCursor) {

		List<TweetInfo> tweets = new ArrayList();
		JSONParser parser = new JSONParser();
		while (tweetDbCursor.hasNext()) {

			try {
				Object obj = parser.parse((String) tweetDbCursor.next()
						.toString());
				JSONObject jsonObj = (JSONObject) obj;

				String name = (String) jsonObj.get("user_name");
				String text = (String) jsonObj.get("tweet_text");

				TweetInfo tweetInfo = new TweetInfo();
				tweetInfo.setUserName(name);
				tweetInfo.setText(text);

				tweets.add(tweetInfo);
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
		return tweets;
	}

}
