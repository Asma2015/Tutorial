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
		configBuilder.setOAuthConsumerKey("ze8YFhq2UXoGneqNZaBy8YCsy");
		configBuilder
				.setOAuthConsumerSecret("mn1Y8gEw8Ea8VqaZRQSUmRV9njvsJgDeRC4x6vKVEpgMbLfODW");
		configBuilder
				.setOAuthAccessToken("393965681-FiqaGR1XbRMu1CgTVMW50cbaaA5jf7Wh4NYLB9CB");
		configBuilder
				.setOAuthAccessTokenSecret("oscpLNEV6NHneUGUIj8rWMU9Cp4pJg5zxHgokQ3u8mWF7");
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

	public List<TweetInfo> getTopTweets(boolean newDb) {

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

			if (newDb) {
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

	public List<String> getTopRetweet() {

		if (dbColl.count() <= 0) {
			getTopTweets(false);
		}
		BasicDBObject names = new BasicDBObject();
		names.put("retweet_count", -1);
		DBCursor nameCursor = dbColl.find().sort(names).limit(5);

		List<String> retweetInfo = new ArrayList();

		while (nameCursor.hasNext()) {
			retweetInfo.add((String) nameCursor.next().toString());
		}
		return retweetInfo;
	}

	public List<String> getTopMentioned() {

		if (dbColl.count() <= 0) {
			getTopTweets(false);
		}

		BasicDBObject names = new BasicDBObject();
		names.put("tweet_mentioned_count", -1);
		DBCursor nameCursor = dbColl.find().sort(names).limit(5);

		List<String> retweetInfo = new ArrayList();

		while (nameCursor.hasNext()) {

			retweetInfo.add((String) nameCursor.next().toString());
		}
		return retweetInfo;
	}

	public List<String> getTopFollowers() {

		if (dbColl.count() <= 0) {
			getTopTweets(false);
		}

		BasicDBObject names = new BasicDBObject();
		names.put("retweet_count", -1);
		DBCursor nameCursor = dbColl.find().sort(names).limit(5);
		List<String> retweetInfo = new ArrayList();
		while (nameCursor.hasNext()) {

			retweetInfo.add((String) nameCursor.next().toString());
		}
		return retweetInfo;
	}

	public List<TweetInfo> fitchTweets() {
		BasicDBObject names = new BasicDBObject("_id", false).append(
				"user_name", true);
		BasicDBObject texts = new BasicDBObject("_id", false).append(
				"tweet_text", true);
		DBCursor nameCursor = dbColl.find(new BasicDBObject(), names);
		DBCursor textCursor = dbColl.find(new BasicDBObject(), texts);

		List<TweetInfo> tweets = fillTweetInfo(nameCursor, textCursor);
		return tweets;
	}

	private List<TweetInfo> fillTweetInfo(DBCursor nameCursor,
			DBCursor textCursor) {

		List<TweetInfo> tweets = new ArrayList();

		while (nameCursor.hasNext() && textCursor.hasNext()) {
			TweetInfo tweetInfo = new TweetInfo();
			tweetInfo.setUserName((String) nameCursor.next().toString());
			tweetInfo.setText((String) textCursor.next().toString());

			tweets.add(tweetInfo);
		}
		return tweets;
	}

}
