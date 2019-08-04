package competition.onedata.android.map.tile.online;

import java.io.IOException;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import competition.onedata.android.map.ViewService;
import competition.onedata.android.map.tile.AbstractAsyncHelper;
import competition.onedata.android.map.tile.PreloaderTileSource;
import competition.onedata.android.map.tile.TileCache;
import competition.onedata.android.map.tile.TileData;
import competition.onedata.android.map.tile.online.cache.TileCacheSqlite;
import competition.onedata.android.util.UtilCollection;

public class AsyncHttpTileHelper extends AbstractAsyncHelper {
	private static final String TAG = AsyncHttpTileHelper.class.getName();

	private UrlTranslator translator;
	private PreloaderTileSource preloaderTileSource;
	private String tileUrl;
	private TileCacheSqlite sqlite;
	private HttpClient httpClient;

	public AsyncHttpTileHelper(PreloaderTileSource preloaderTileSource,
			TileCache tileCache, Handler mapViewHandler, ViewService viewInfo,
			String tileUrl, Context context) {
		super(preloaderTileSource, tileCache, mapViewHandler, viewInfo);
		translator = new UrlTranslator();
		this.preloaderTileSource = preloaderTileSource;
		setTileUrl(tileUrl);

		sqlite = new TileCacheSqlite(context);
		httpClient = getNewHttpClient();
	}

	private void setTileUrl(String tileUrl) {
		if (!tileUrl.endsWith("?"))
			tileUrl += "?";
		this.tileUrl = tileUrl;
	}

	@Override
	protected boolean doInRange(String key) {
		URLConnection yc = null;
		try {
			TileData tileData = new TileData(key);

			byte[] bytes = sqlite.get(tileData);
			if (bytes == null) {
				Log.d(TAG, "No cache, get tile from web : " + key);
				bytes = downloadTileFormWeb(tileData);
			} else {
				Log.d(TAG, "Tile obtained from sqlite " + key);
			}

			final Bitmap bitmap = bytes == null ? preloaderTileSource
					.getErrorTile() : BitmapFactory.decodeByteArray(bytes, 0,
					bytes.length);
			getTileCache().putTile(key, bitmap);
			Log.i(TAG, "@byte: " + bytes.length);
			return true;
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
			Bitmap bitmap = preloaderTileSource.getErrorTile();
			getTileCache().putTile(key, bitmap);
			return true;
		}

	}

	private byte[] downloadTileFormWeb(TileData tileData) throws IOException,
			ClientProtocolException {
		byte[] bytes = null;
		String add = tileUrl + translator.getUrl(tileData);
		Log.e(TAG, "Url : " + add);
		final HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
		HttpGet getMethod = new HttpGet(add);
		HttpResponse response = new DefaultHttpClient(httpParams).execute(getMethod);

		if (response.getStatusLine().getStatusCode() == 200) {
			bytes = UtilCollection.toBytes(response.getEntity().getContent());
			// Save the tile at sqlite
			try {
				if (bytes != null && bytes.length > 0)
					sqlite.insertTile(tileData, bytes);
			} catch (Exception e) {
				Log.e(TAG, Log.getStackTraceString(e));
			}
		}
		return bytes;
	}

	@Override
	protected boolean doOutOfRange(String key) {
		try {
			getTileCache().putTile(key, preloaderTileSource.getEmptyTile());
			Log.i(TAG, "requestAsyncDownload Not in range, quite.");
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
		return false;
	}

	public static HttpClient getNewHttpClient() {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 1000 * 30);
		HttpConnectionParams.setSoTimeout(httpParameters, 1000 * 30);
		HttpClient httpClient = new DefaultHttpClient(httpParameters);
		return httpClient;
	}

}
