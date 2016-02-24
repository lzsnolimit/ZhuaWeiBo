package Weiobo.weibo.query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class Base {
	public static Connection getConnection(String url, String fileName)
			throws IOException {
		Connection con = Jsoup.connect(url).ignoreContentType(true);
		File httpHeaders = new File(fileName);
		if (httpHeaders.exists()) {
			BufferedReader in = new BufferedReader(new FileReader(httpHeaders));
			String line = in.readLine();
			while (line != null) {
				if (line.contains("Cookie: ")) {
					String[] cookies = line.substring(line.indexOf(":") + 1)
							.trim().split(";");
					for (String cookie : cookies) {
						con.cookie(cookie.substring(0, cookie.indexOf("="))
								.trim(),
								cookie.substring(cookie.indexOf("=") + 1)
										.trim());
					}
				} else {
					con.header(line.substring(0, line.indexOf(":")).trim(),
							line.substring(line.indexOf(":") + 1).trim());
				}
				line = in.readLine();
			}
			in.close();
		}
		return con;
	}

	
}
