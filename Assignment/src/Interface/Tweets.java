package Interface;

import Business.Statistics;
import Business.TweetInfo;

import java.util.List;

import com.mongodb.MongoException;

public class Tweets {

	private static final long serialVersionUID = -1563141402085463680L;

	public List<TweetInfo> getTopTweets() {

		try {
			Statistics tweetStatis = new Statistics();
			return tweetStatis.getTopTweets(true);

		} catch (MongoException ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	public List<String> getTopRetweets() {

		try {
			Statistics tweetStatis = new Statistics();
			return tweetStatis.getTopRetweet();

		} catch (MongoException ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	public Object getTopFollowers() {
		try {
			Statistics tweetStatis = new Statistics();
			return tweetStatis.getTopFollowers();

		} catch (MongoException ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}

	public Object getTopMentioned() {
		try {
			Statistics tweetStatis = new Statistics();
			return tweetStatis.getTopMentioned();

		} catch (MongoException ex) {
			System.out.println(ex.getMessage());
		}
		return null;
	}
}
