package Weiobo.weibo.query;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.naming.InitialContext;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;

public class SearchQuery implements Runnable {
	public static boolean continueTag = true;
	public static int currentPosition = 1;
	String keyword;

	public SearchQuery(String keyword) {
		this.keyword = keyword;
	}
	public static void init()
	{
		continueTag = true;
		currentPosition = 1;
	}

	public void run() {
		String url = "";
		synchronized (SearchQuery.class) {
			if (currentPosition>=100) {
				continueTag=false;
				return;
			}
			url = "http://weibo.cn/search/mblog?hideSearchFrame=&keyword="+keyword;
			
		}
		try {
			Connection connection=Base.getConnection(url, "HttpConfig/search");
			connection.data("mp", "100");
			connection.data("page", String.valueOf(currentPosition));
			org.jsoup.nodes.Document doc = connection.post();

			Writer writerJson = null,writerHtml = null;

			try {
				writerJson = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(keyword+"Json.txt",true)));
				writerHtml = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(keyword+"Html.txt",true)));
				Elements allContent=doc.getElementsByClass("c");
				for (Element element : allContent) {
					if (element.hasAttr("id")) {
						JSONObject jObject=new JSONObject();
						jObject.put("Id", element.attr("id"));
						jObject.put("Content", element.getElementsByClass("ctt").text());
						jObject.put("TimeAndDevice", element.getElementsByClass("ct").text());
						writerJson.write(jObject.toString()+"\n");
						writerHtml.write(element.toString()+"\n");

					}
				}
				System.out.println(currentPosition+" done!");
				currentPosition++;
			} catch (IOException ex) {
				// report
			} finally {
				try {
					writerJson.close();
					writerHtml.close();
				} catch (Exception ex) {
					/* ignore */}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(currentPosition + " failed!");
		}
	}
}
