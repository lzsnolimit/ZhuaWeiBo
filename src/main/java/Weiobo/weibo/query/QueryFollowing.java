package Weiobo.weibo.query;

import java.io.IOException;

import org.json.JSONArray;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Log.Log;

public class QueryFollowing {

	JSONArray following = new JSONArray();
	int timeOut = 2;
	String url;
	int guanzhuSize;

	public QueryFollowing(String url, int guanzhuSize) {
		this.url = url;
		this.guanzhuSize = guanzhuSize;
	}

	public String queryGuanzhu() {
		if (guanzhuSize == 0) {
			return "";
		}
		int pages = guanzhuSize / 9;
		if (pages%9!=0) {
			pages++;
		}
		System.out.println(url+"  "+String.valueOf(pages));
		if (pages > 20) {
			pages = 20;
		}
		for (int i = 1; i <= pages; i++) {
			getByPage(i);
		}
		return following.toString();
	}

	public void getByPage(int position) {
		Connection connection = null;
		Document doc = null;
		try {
			connection = Base.getConnection(url, "HttpConfig/following");
			doc = connection.data("mp", "20").data("page", String.valueOf(position)).post();
			
			Elements tables = doc.getElementsByTag("table");
			
			//System.out.println(doc);
			if (tables.size() <= 0) {
				if (timeOut >= 0) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					timeOut--;
					getByPage(position);
					Log.write("log.txt", "Following " + url + " at position " + position + " error; try again!");
				} else {
					System.out.println("Following " + url + " at position " + position + " error; time out!");
					Log.write("log.txt", "Following " + url + " at position " + position + " error; time out!");
					return;
				}
			}
			
			for (Element element : tables) {
				String parts[] = element.getElementsByTag("a").first().attr("href").split("/");
				//System.out.println(element.getElementsByTag("a").first().attr("href"));
				String url=parts[parts.length - 1];
				if (url.length()>=25) {
					System.out.println("invalid url "+url+" please update following configeration file!");
				}else {
					following.put(url);
				}
				
			}

		} catch (IOException e) {
			if (timeOut >= 0) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				timeOut--;
				getByPage(position);
				Log.write("log.txt", "Following " + url + " at position " + position + " error; try again!");
			} else {
				System.out.println("Following " + url + " at position " + position + " error; time out!");
				Log.write("log.txt", "Following " + url + " at position " + position + " error; time out!");
				return;
			}
		}
	}

}
