package competition.onedata.android.map.tile;

import competition.onedata.android.map.BaseMapConfig;
import competition.onedata.android.map.BaseMapLevel;

import android.graphics.Bitmap;


public interface TileSource {


	public abstract void setCacheListSize(int size);

	public abstract void setPreloader(Bitmap bitmap);

	public abstract Bitmap getTile(String tileUrl);

	public abstract BaseMapConfig getBaseMapConfig();

	public abstract BaseMapLevel[] GetBaseMapLevelList();

	public abstract void commitCache();

	public abstract void close();
}