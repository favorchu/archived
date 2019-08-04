package competition.onedata.android.map.tile;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import android.graphics.Bitmap;
import android.util.Log;

public class TileCache {
	private String TAG = this.getClass().getSimpleName();

	protected TileHash softPool;
	protected LinkedHashMap<String, Bitmap> mainPool;
	protected LinkedHashMap<String, Bitmap> usedTiles;

	public TileCache(final int size) {
		this.softPool = new TileHash(size);
		this.mainPool = new LinkedHashMap<String, Bitmap>(size);
		this.usedTiles = new LinkedHashMap<String, Bitmap>(size);
	}

	public synchronized Bitmap getMapTile(final String tileUrl) {
		// Get the tiles from main pool
		final Bitmap bmpHard = this.mainPool.get(tileUrl);
		if (bmpHard != null) {
			if (!bmpHard.isRecycled()) {
				this.usedTiles.put(tileUrl, bmpHard);
				return bmpHard;
			}
		}
		// check if cache still exist
		final SoftReference<Bitmap> ref = this.softPool.get(tileUrl);
		if (ref == null)
			return null;
		final Bitmap bmp = ref.get();
		if (bmp == null) {
			this.softPool.remove(ref);
		} else if (bmp.isRecycled()) {
			this.softPool.remove(ref);
		}
		this.usedTiles.put(tileUrl, bmp);
		return bmp;
	}

	public synchronized void putTile(final String tileUrl, final Bitmap tileByte) {
		this.softPool.put(tileUrl, new SoftReference<Bitmap>(tileByte));
		this.usedTiles.put(tileUrl, tileByte);
	}

	public synchronized void flushUnused() {
		// delete all the unsed tiles from main pool

		final LinkedHashMap<String, Bitmap> tmp = this.mainPool;
		this.mainPool = this.usedTiles;
		this.usedTiles = tmp;
		this.usedTiles.clear();
	}

	public void dispose() {
		softPool.clear();
		mainPool.clear();
		usedTiles.clear();
	}

	private class TileHash extends LinkedHashMap<String, SoftReference<Bitmap>> {
		private String TAG = this.getClass().getSimpleName();
		private final int capacity;

		public TileHash(final int size) {
			super(size + 2, 0.1f, true);
			capacity = size;
		}

		@Override
		public SoftReference<Bitmap> remove(Object pKey) {
			final SoftReference<Bitmap> ref = super.remove(pKey);
			if (ref != null) {
				final Bitmap bm = ref.get();
				if (bm != null) {
					Log.i(TAG, "Recycle bitmap:" + bm);
					bm.recycle();
				}
			}
			return ref;
		}



		@Override
		protected boolean removeEldestEntry(
				Entry<String, SoftReference<Bitmap>> pEldest) {
			return size() > capacity;
		}

	}

}
