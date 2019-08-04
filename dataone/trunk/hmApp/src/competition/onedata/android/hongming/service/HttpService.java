package competition.onedata.android.hongming.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import competition.onedata.android.util.UtilCollection;

public class HttpService {

	private static String TAG = HttpService.class.getSimpleName();
	private HttpClient httpClient;

	public HttpService() {
		httpClient = getNewHttpClient();
	}

	private HttpClient getNewHttpClient() {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 20 * 1000);
		HttpConnectionParams.setSoTimeout(httpParameters, 20 * 1000);
		HttpClient httpClient = new DefaultHttpClient(httpParameters);
		return httpClient;
	}

	public String post(HttpPost httppost) throws ClientProtocolException,
			IOException {
		return httpPost(httppost);
	}

	public String post(HttpPost httppost, List<NameValuePair> params)
			throws ClientProtocolException, IOException {
		HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		httppost.setEntity(entity);
		return httpPost(httppost);
	}

	private String httpPost(HttpPost httppost) throws ClientProtocolException,
			IOException {
		HttpResponse response = httpClient.execute(httppost);

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
		return sb.toString();
	}

	public byte[] getBytesByHttpPost(HttpPost httppost)
			throws ClientProtocolException, IOException {
		HttpResponse response = httpClient.execute(httppost);

		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200)
			throw new ClientProtocolException("Status code:" + statusCode);
		return UtilCollection.toBytes(response.getEntity().getContent());
	}
}
