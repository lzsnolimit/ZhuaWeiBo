package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Log.Log;

public class JDBC {
	private static String url = "jdbc:mysql://localhost:3306/";
	// MySQL配置时的用户名
	private static String user = "root";
	// Java连接MySQL配置时的密码
	private static String password = "weibozhuaqu";
	private static Connection conn = null;

	private static String database = "weibo";

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url + database + "?useUnicode=true&characterEncoding=utf8", user,
					password);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 连续数据库

	}

	/**
	 * get a random unquery id
	 * 
	 * @return weibo id
	 */
	public static String getRandomId() {

		String countSql = "SELECT COUNT(WeiBoId) FROM `users` WHERE done=0";
		int count = 0;
		try {
			// statement用来执行SQL语句
			Statement statement = conn.createStatement();
			// 要执行的SQL语句
			ResultSet rs = statement.executeQuery(countSql);
			if (rs.next()) {
				count = Integer.valueOf(rs.getString(1));
				// return rs.getString(1);
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		int randomPosition = (int) (Math.random() * count);
		String querySql = "SELECT WeiBoId FROM `users` WHERE done=0 LIMIT " + randomPosition + ",1;";
		try {
			// statement用来执行SQL语句
			Statement statement = conn.createStatement();
			// 要执行的SQL语句
			ResultSet rs = statement.executeQuery(querySql);
			if (rs.next()) {
				return rs.getString(1);
				// return rs.getString(1);
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Execite a sql
	 * 
	 * @param sql
	 */
	public static void execute(String sql) {
		try {
			// statement用来执行SQL语句
			Statement statement = conn.createStatement();
			Log.write("Log.txt", sql);
			statement.execute(sql);
		} catch (SQLException e) {
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

}