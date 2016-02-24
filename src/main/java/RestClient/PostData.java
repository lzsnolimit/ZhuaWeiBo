package RestClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;

public class PostData {
	public static String contributer;

	static {
		System.out.println("Input your name:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			contributer = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sendUnread(String unreadData) {
		PostMethod post = new PostMethod("http://104.236.213.130:9000/unread");
		post.addParameter("unread", unreadData);
		post.addParameter("contributer", contributer);
		HttpClient httpclient = new HttpClient();
		httpclient.getParams().setContentCharset("UTF-8");
		try {
			int result = 0;
			try {
				result = httpclient.executeMethod(post);
				
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println("Response status code: " + result);
			// System.out.println("Response body: ");
//			try {
//				//System.out.println("Unread: "+post.getResponseBodyAsString());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		} finally {
			// Release current connection to the connection pool once you are
			// done
			post.releaseConnection();
		}
	}

	public static void sendUpdate(String updateData) {
		PostMethod post = new PostMethod("http://104.236.213.130:9000/update");
		post.addParameter("user", updateData);
		HttpClient httpclient = new HttpClient();
		httpclient.getParams().setContentCharset("UTF-8");
		try {
			int result = 0;
			try {
				result = httpclient.executeMethod(post);
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println("Response status code: " + result);
			// System.out.println("Response body: ");
//			try {
//				System.out.println(post.getResponseBodyAsString());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		} finally {
			// Release current connection to the connection pool once you are
			// done
			post.releaseConnection();
		}
	}

	public static String getNextId() {
		GetMethod getMethod = new GetMethod("http://104.236.213.130:9000/nextsql/");
		HttpClient httpclient = new HttpClient();

		try {
			int result = 0;
			try {
				result = httpclient.executeMethod(getMethod);
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println("Response status code: " + result);
			// System.out.println("Response body: ");
			try {
				return getMethod.getResponseBodyAsString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			// Release current connection to the connection pool once you are
			// done
			getMethod.releaseConnection();
		}
		return getNextId();

	}

	private static String getStringFromInputStream(InputStream in) throws Exception {
		CachedOutputStream bos = new CachedOutputStream();
		IOUtils.copy(in, bos);
		in.close();
		bos.close();
		return bos.getOut().toString();
	}

}
