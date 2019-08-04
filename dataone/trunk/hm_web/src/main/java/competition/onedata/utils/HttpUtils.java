package competition.onedata.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HttpUtils.class);

	public static int downloadFile(File file, String url)
			throws ClientProtocolException, IOException {
		LOGGER.info("downloadFile({},{})",
				file == null ? "null" : file.getAbsolutePath(), url);

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = client.execute(httpGet);

		int statusCode = response.getStatusLine().getStatusCode();
		LOGGER.info("Status code: {} ", statusCode);

		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		try {
			FileOutputStream os = new FileOutputStream(file);
			try {
				IOUtils.copy(is, os);
			} finally {
				IOUtils.closeQuietly(os);
			}
		} finally {
			IOUtils.closeQuietly(is);
		}

		return statusCode;
	}
}
