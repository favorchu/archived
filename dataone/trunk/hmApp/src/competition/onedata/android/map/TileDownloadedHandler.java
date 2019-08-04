package competition.onedata.android.map;

import android.os.Handler;
import android.os.Message;

public class TileDownloadedHandler extends Handler {

	public static final int MSG_ASY_TILE_DOWNLOAD_SUCCEED = 1;

	private BasicMapView mapview;

	public TileDownloadedHandler(BasicMapView mapview) {
		this.mapview = mapview;
	}

	@Override
	public void handleMessage(final Message msg) {

		switch (msg.what) {
		case MSG_ASY_TILE_DOWNLOAD_SUCCEED:
			mapview.invalidate();
			break;
		}

	}
}
