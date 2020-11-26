/* Programmer: Courtney Brown
 * Date: Fall 2017, modified 2019
 * This class uses the twitter4j library to update a twitter status via code and perform limited searches.
 * Using API & modfied from examples here: http://twitter4j.org/en/
 */

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import java.util.*;


public class TwitterInteraction {

	Twitter twitter; //holds the twitter API

    //logs into twitter using OAuth
	TwitterInteraction() {

		try {
			//find the keys here: https://developer.twitter.com/en/apps/
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true).setOAuthConsumerKey("RpqHouyDHWubB3MXE7ozJhz0A") //API Key here
					.setOAuthConsumerSecret("63mc8UnB7Z5blz5UaVkJ5V1eyCwxFEuDY6gaNYMmZ51etJ9QVT") //Secret key here
					.setOAuthAccessToken("911747143299403776-JldNjA5mWUyjYtL5FjSZJjY1aCzOBKC") //access token here
					.setOAuthAccessTokenSecret("Yfui2eWWNWSFpEWd8LbiGjGAe6rS4ICW4CzkSENphJvR5"); //secret access token here
			TwitterFactory tf = new TwitterFactory(cb.build());
			twitter = tf.getInstance();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to get timeline: " + e.getMessage());
		}

	}

	//updates twitter status with the update_str
	public void updateTwitter(String update_str) {
		try {

			Status status = twitter.updateStatus(update_str);
			System.out.println("Successfully updated the status to [" + status.getText() + "].");
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to read the system input.");
		}
	}

	//returns a list of tweets with the given search term
	public ArrayList<String> searchForTweets(String searchTerm) {
		ArrayList<String> res = new ArrayList(); 
		try {
			Query query = new Query(searchTerm);
			query.count(100);
			
			QueryResult result = twitter.search(query);
			for (Status status : result.getTweets()) {
//				System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
				res.add(status.getText()); 
			}
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to read the system input.");
		}
		return res; 
	}

}
