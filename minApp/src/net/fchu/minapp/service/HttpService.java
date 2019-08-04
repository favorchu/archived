package net.fchu.minapp.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HttpService.class);

	private static final int WAIT_TIME = 5 * 1000;
	private static String TAG = HttpService.class.getSimpleName();
	private static final int BYTE_BUFFER_SIZE = 1024 * 4;

	private static void close(Closeable is) {
		if (is != null) {
			try {
				is.close();
			} catch (Exception e) {
				;
			}
		}
	}

	private static byte[] toBytes(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = null;
		byte[] output = null;
		try {
			buf = new byte[BYTE_BUFFER_SIZE];
			int len;
			while ((len = is.read(buf, 0, BYTE_BUFFER_SIZE)) != -1)
				bos.write(buf, 0, len);
			bos.flush();
			output = bos.toByteArray();
		} finally {
			close(bos);
			close(is);
		}
		return output;
	}

	private HttpClient httpClient;

	public HttpService() {
		httpClient = getNewHttpClient();
	}

	private HttpClient getNewHttpClient() {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, WAIT_TIME);
		HttpConnectionParams.setSoTimeout(httpParameters, WAIT_TIME);
		HttpClient httpClient = new DefaultHttpClient(httpParameters);
		return httpClient;
	}

	public String post(HttpPost httppost) throws ClientProtocolException,
			IOException {
		return httpPost(httppost);
	}

	public String get(HttpGet httpGet) throws ClientProtocolException,
			IOException {
		return httpGet(httpGet);
	}

	public String post(HttpPost httppost, List<NameValuePair> params)
			throws ClientProtocolException, IOException {
		HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		httppost.setEntity(entity);
		return httpPost(httppost);
	}

	private String httpPost(HttpPost httppost) throws ClientProtocolException,
			IOException {
		LOGGER.debug("url:{}", httppost.getURI());
		HttpResponse response = httpClient.execute(httppost);

		return getString(response);
	}

	private String httpGet(HttpGet httpGet) throws ClientProtocolException,
			IOException {
		LOGGER.debug("url:{}", httpGet.getURI());
		HttpResponse response = httpClient.execute(httpGet);

		return getString(response);
	}

	private String getString(HttpResponse response)
			throws ClientProtocolException, IOException {
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200)
			throw new ClientProtocolException("Status code:" + statusCode);

		InputStreamReader is = new InputStreamReader(response.getEntity()
				.getContent());
		BufferedReader reader = new BufferedReader(is);
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append((line + "\n"));
		}
		IOUtils.closeQuietly(reader);
		return sb.toString();
	}

	public byte[] getBytes(HttpGet httpGet) throws ClientProtocolException,
			IOException {
		LOGGER.debug("url:{}", httpGet.getURI());
		HttpResponse response = httpClient.execute(httpGet);
		return getBytes(response);
	}

	private byte[] getBytes(HttpResponse response)
			throws IllegalStateException, IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200)
			throw new ClientProtocolException("Status code:" + statusCode);

		InputStreamReader is = new InputStreamReader(response.getEntity()
				.getContent());

		IOUtils.copy(is, bos);

		byte[] bytes = bos.toByteArray();
		IOUtils.closeQuietly(bos);
		IOUtils.closeQuietly(is);
		return bytes;
	}

	public byte[] getBytesByHttpPost(HttpPost httppost)
			throws ClientProtocolException, IOException {
		HttpResponse response = httpClient.execute(httppost);

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200)
			throw new ClientProtocolException("Status code:" + statusCode);
		return toBytes(response.getEntity().getContent());
	}
}
