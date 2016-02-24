package Weiobo.weibo;

import java.io.IOException;

import RestClient.PostData;
import Weiobo.weibo.query.QueryIndexPage;
import Weiobo.weibo.query.SearchQuery;

/**
 * Hello world!
 *
 */
public class Main {
	public static void searchByKeyWords(String keys) {
		while (SearchQuery.continueTag) {
			try {
				SearchQuery newThread = new SearchQuery(keys);
				newThread.run();
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		
		QueryIndexPage queryIndexPage = new QueryIndexPage();
		long startTime=System.currentTimeMillis();
		while (true) {
			queryIndexPage.parse(PostData.getNextId());
			long currentTime=System.currentTimeMillis();
			System.out.println(currentTime-startTime);
			if ((currentTime-startTime)>=4*60*1000) {
				try {
					System.out.println("Sleep 6 minutes:");
					Thread.sleep(6*60*1000);//sleep 10 minutes
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startTime=System.currentTimeMillis();
			}
		}
		
//		String[] keys = { "创新" };
//		System.out.println("Input your command:");
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		String str = br.readLine();
//		switch (str) {
//		case "search":
//			for (String key : keys) {
//				searchByKeyWords(key);
//				System.out.println(key + " done!");
//				SearchQuery.init();
//			}
//			break;
//		case "relation":
//			QueryIndexPage queryIndexPage = new QueryIndexPage();
//			while (true) {
//				queryIndexPage.parse(PostData.getNextId());
//			}
//		default:
//			System.out.println("Wrong input");
//			break;
//		}
	}
}
