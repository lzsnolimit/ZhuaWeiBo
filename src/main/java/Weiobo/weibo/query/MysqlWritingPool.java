package Weiobo.weibo.query;

import java.util.ArrayDeque;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONObject;

import Database.JDBC;
import Log.Log;

public class MysqlWritingPool implements Runnable {

	private Queue<String> pool = new ArrayDeque<>();
	String threadName;

	public MysqlWritingPool(String name) {
		threadName = name;
	}

	public String getNextSql() {
		synchronized (MysqlWritingPool.class) {
			if (pool.size() == 0) {
				try {
					Thread.sleep(1000);
					// Log.write("log.txt", "Thread wait!");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return getNextSql();
			} else {
				return pool.poll();
			}
		}
	}

	/**
	 * Generate unread sql
	 * 
	 * 
	 */
	public void generateUnreadSql(JSONArray unreadJson,String contributer) {
		
		//INSERT INTO `users` (`WeiBoId`, `done`, `contributer`) VALUES ('test3', '0', 'LU'), ('test4', '0',  'Lu');
		String sql = "INSERT INTO `users` (`WeiBoId`,`contributer`) VALUES " ;
		for (int i = 0; i < unreadJson.length(); i++) {
			//System.out.println(sql +"('"+unreadJson.getString(i)+"','"+contributer+"')");
			pool.offer(sql +"('"+unreadJson.getString(i)+"','"+contributer+"')");
		}
	}

	

	
	/**
	 * Generate update sql
	 * 
	 */
	public void generateUpdateSql(JSONObject userJson) {
		int genderInt = userJson.getString("gender").equals("男") ? 1 : 0;

		String sql = "UPDATE `users` SET `name` = '" + userJson.getString("name") + "', `time` = '" + System.currentTimeMillis()
				+ "', `fensi` = '" + userJson.getString("fensi") + "', `guanzhu` = '" + userJson.getString("guanzhu") + "', `gender` = '" + genderInt
				+ "', `location` = '" + userJson.getString("location") + "', `comment` = '" + userJson.getString("comment") + "', `done` = '1', `WeiboSize` = '"
				+ Integer.valueOf(userJson.getString("weiboSize")) + "', `FensiSize` = '" + Integer.valueOf(userJson.getString("fensiSize"))
				+ "', `FollowingSize` = '" + Integer.valueOf(userJson.getString("guanzhuSize")) + "' WHERE `users`.`WeiBoId` = '" + userJson.getString("id") + "';";
		pool.offer(sql);
	}

	public void run() {
		//System.out.println("Arrive at Run");
		// TODO Auto-generated method stub
		Log.write("log.txt", "Execute thread " + threadName);
		while (!pool.isEmpty()) {
			JDBC.execute(getNextSql());
		}
	}

}
