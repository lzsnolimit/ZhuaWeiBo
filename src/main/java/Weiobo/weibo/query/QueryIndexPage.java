package Weiobo.weibo.query;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import Log.Log;
import RestClient.PostData;

public class QueryIndexPage {
	static int threadNum = 2;
	int timeOut = 2;
//	static {
//		Thread[] pool = new Thread[threadNum];
//		for (int i = 1; i <= threadNum; i++) {
//			pool[i - 1] = new Thread(new MysqlWritingPool(String.valueOf(i)));
//			pool[i - 1].start();
//		}
//	}
	
	
	public void parse(String id) {
		// System.out.println(id);
		String url = "http://weibo.cn/" + id;
		System.out.println(url);
		try {
			Connection connection = Base.getConnection(url, "HttpConfig/zhuye");
			Document doc = connection.get();
			// System.out.println(doc);
			// System.out.println( doc.getElementsByAttributeValue("valign",
			// "top").text());
			while (doc.getElementsByAttributeValue("valign", "top").size() < 2) {
				if (timeOut > 0) {
					System.out.println("Query index "+id + " error,try again!");
					Log.write("log.txt", id + " error,try again!");
					timeOut--;
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					System.out.println("Query index " + id + " time out!");
					Log.write("log.txt", "Query index " + id + " time out!");
					timeOut = 5;
					return;
					// MysqlWritingPool.generateUpdateSql(id, "", "", "", "",
					// "", "", "", "", "");
				}
			}

			Element contentPart = doc.getElementsByAttributeValue("valign", "top").get(1);
			try {
				String[] parts = contentPart.getElementsByClass("ctt").text().split(" ");
				String[] genderAndLocation = parts[1].split("/");
				// for (String string : parts) {
				// System.out.println(string+"\n");
				// }
				String name = parts[0];
				String gender = genderAndLocation[0];
				String location = genderAndLocation[1];
				String comment = "";
				if (parts.length >= 3) {
					for (int i = 2; i < parts.length; i++) {
						comment += parts[i] + " ";
					}
				}

				if (comment.contains("加关注")) {
					comment = comment.replace("加关注", "").trim();
				}
				if (comment.contains("已关注")) {
					comment = comment.replace("已关注", "").trim();
				}
				Element infoDiv = doc.getElementsByClass("tip2").first();
				String weiboSize = infoDiv.getElementsByClass("tc").first().text();
				weiboSize = weiboSize.substring(weiboSize.indexOf("[") + 1, weiboSize.indexOf("]"));

				String guanzhuSize = infoDiv.getElementsByTag("a").first().text();
				guanzhuSize = guanzhuSize.substring(guanzhuSize.indexOf("[") + 1, guanzhuSize.indexOf("]"));

				String fensiSize = infoDiv.getElementsByTag("a").get(1).text();
				fensiSize = fensiSize.substring(fensiSize.indexOf("[") + 1, fensiSize.indexOf("]"));

				String guanzhuUrl = "http://weibo.cn" + infoDiv.getElementsByTag("a").first().attr("href");

				QueryFollowing guanzhu = new QueryFollowing(guanzhuUrl, Integer.valueOf(guanzhuSize));
				JSONArray guanzhuArray = new JSONArray(guanzhu.queryGuanzhu());

				JSONObject indexInfoJson = new JSONObject();
				indexInfoJson.put("id", id);
				indexInfoJson.put("fensi", "");
				indexInfoJson.put("guanzhu", guanzhuArray.toString());
				indexInfoJson.put("gender", gender);
				indexInfoJson.put("location", location);
				indexInfoJson.put("comment", comment);
				indexInfoJson.put("name", name);
				indexInfoJson.put("weiboSize", weiboSize);
				indexInfoJson.put("guanzhuSize", guanzhuSize);
				indexInfoJson.put("fensiSize", fensiSize);
				PostData.sendUpdate(indexInfoJson.toString());
				PostData.sendUnread(guanzhuArray.toString());
//				MysqlWritingPool.generateUpdateSql(id, "", guanzhuArray.toString(), gender, location, comment, name,
//						weiboSize, guanzhuSize, fensiSize);

//				for (int i = 0; i < guanzhuArray.length(); i++) {
//					MysqlWritingPool.generateUnreadSql(guanzhuArray.getString(i));
//				}

			} catch (Exception e) {
				//MysqlWritingPool.generateUpdateSql(id, "", "", "", "", "", "", "", "", "");
				// TODO: handle exception
			}
			// System.out.println(id);
		} catch (IOException e) {
			//e.printStackTrace();
			if (timeOut > 0) {
				Log.write("log.txt", id + " error,try again!");
				timeOut--;
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				parse(id);
			} else {
				System.out.println("Query index " + id + " time out!");
				Log.write("log.txt", "Query index " + id + " time out!");
				timeOut = 5;
				// MysqlWritingPool.generateUpdateSql(id, "", "", "", "", "",
				// "", "", "", "");
			}
		}
	}
}
