package competition.onedata.android.map;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

import competition.onedata.android.map.config.MapViewConfigManager;
import competition.onedata.android.map.coordinate.HK80Coordinate;
import competition.onedata.android.map.overlap.Overlay;
import competition.onedata.android.map.overlap.ScaleBarOverlay;
import competition.onedata.android.map.sqlite.TileSQLiteFactory;
import competition.onedata.android.map.tile.EmptyTileSource;
import competition.onedata.android.map.tile.TileSource;
import competition.onedata.android.map.tile.offline.VirtualSqliteTileSource;
import competition.onedata.android.map.util.DoubleRactangular;
import competition.onedata.android.map.util.DrawTileData;
import competition.onedata.android.map.util.GeoPoint;
import competition.onedata.android.map.util.IntegerRectangular;
import competition.onedata.android.map.util.PixelPoint;

public class BasicMapView extends View implements
		TouchEventDetector.OnGestureListener, OnGestureListener,
		OnDoubleTapListener, ViewService {

	private String TAG = this.getClass().getSimpleName();
	Context context;
	private Paint debugRedPaint, paint, debugBluePaint, debugGreenPaint;

	// TODO make static database path to dynamic

	private final GestureDetector mGestureDetector = new GestureDetector(this);
	private final TouchEventDetector mMultiDetector = TouchEventDetector
			.newInstance(this);
	private int mTouchDownX;
	private int mTouchDownY;
	private int mTouchMapOffsetX;
	private int mTouchMapOffsetY;
	private int mTouchCentreX;
	private int mTouchCentreY;
	private int mUpperLeftTileOffsetX;
	private int mUpperLeftTileOffsetY;
	private double mTouchScale = 1;
	private double mTouchDiagonalSize;
	private boolean stopMoveDetecting = true;

	private boolean moved = false;
	private boolean drawOverlaysWhenTouching = false;
	private boolean inited = false;
	private boolean drawScaleBar = true;

	public boolean isDrawScaleBar() {
		return drawScaleBar;
	}

	public void setDrawScaleBar(boolean drawScaleBar) {
		this.drawScaleBar = drawScaleBar;
	}

	private int drawFirstOffsetX;
	private int drawFirstOffsetY;

	private boolean IS_DEBUG = false;
	MapViewConfigManager mapViewConfigManager;

	// Coordinate System
	private HK80Coordinate coordinate;
	private DrawTileData[] tiles;
	private int tileWidth = 256;
	private int tileHeight = 256;
	private int rotatedAngle = 0;
	private TileDownloadedHandler mapViewHandler;
	private IntegerRectangular displayPixelRange;
	private DoubleRactangular displayGeoare;

	private TileSource tileSource;

	// Overlay
	protected List<Overlay> overlayList;
	protected List<Overlay> inDisplayOverlayList;

	private ScaleBarOverlay scaleOverlay;

	public BasicMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		scaleOverlay = new ScaleBarOverlay(this);

		mapViewConfigManager = MapViewConfigManager.getInstance();
		IS_DEBUG = MapViewConfigManager.IS_DEBUG;

		setFocusable(true);
		setFocusableInTouchMode(true);
		mapViewHandler = new TileDownloadedHandler(this);

		overlayList = new ArrayList<Overlay>();
		inDisplayOverlayList = new ArrayList<Overlay>();

		TileSource tileSrc = getTileSource(context);
		if (tileSrc == null)
			setTilesource(new EmptyTileSource());
		else
			setTilesource(tileSrc);

		paint = new Paint();
		debugRedPaint = new Paint();
		debugRedPaint.setColor(Color.RED);
		debugRedPaint.setAntiAlias(true);
		debugBluePaint = new Paint();
		debugBluePaint.setColor(Integer.parseInt("6600FF", 16));
		debugBluePaint.setAlpha(100);
		debugGreenPaint = new Paint();
		debugGreenPaint.setColor(Integer.parseInt("009933", 16));
		debugGreenPaint.setAlpha(100);
	}

	protected TileSource getTileSource(Context context) {
		// return new EmptyTileSource();

		TileSource ts = null;

		try {
			ts = new VirtualSqliteTileSource(context, mapViewHandler,
					TileSQLiteFactory.getInstance(context), this);
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}

		return ts;
		//
		// return new OnlinTileSource(context, mapViewHandler, this,
		// "http://10.68.30.63/gismap2/en/getmap.ashx?");

	}

	public void setTilesource(TileSource tileSource) {
		try {
			this.tileSource = tileSource;
			GeoPoint geoCenter = null;
			double scale = -1;
			if (coordinate != null) {
				geoCenter = coordinate.getGeoCentre();
				scale = coordinate.getScale();
			}
			coordinate = new HK80Coordinate(this.tileSource.getBaseMapConfig(),
					this.tileSource.GetBaseMapLevelList());
			coordinate.setViewSize(getWidth(), getHeight());
			setTileWidth(tileSource.getBaseMapConfig().getTileWidth());
			setTileHeight(tileSource.getBaseMapConfig().getTileHeight());

			if (geoCenter != null) {
				coordinate.setGeoCentre(geoCenter);
			}
			if (scale > 0) {
				coordinate.setScale(scale);
			}
			inited = false;

		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
	}

	public void setPreloader(Bitmap bitmap) {
		tileSource.setPreloader(bitmap);
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		int screenWidth = getWidth();
		int screenHeight = getHeight();
		// Unknown reason, the width, height is been reverse.
		coordinate.setViewSize(screenHeight, screenWidth);
		reCalculateCoordinate();
	}

	private void updateViewSize() {
		int screenWidth = getWidth();
		int screenHeight = getHeight();
		coordinate.setViewSize(screenWidth, screenHeight);

	}

	private void init() {

		int bufferSize = (int) (Math.ceil((double) getWidth() / tileWidth + 1)
				* Math.ceil((double) getHeight()) / tileHeight + 1);
		// Four time more buffer is sufficient
		bufferSize *= 4;
		Log.i("Cache", "Cache list size : " + bufferSize);
		tileSource.setCacheListSize(bufferSize);
		inited = true;

		// Update the screen size
		updateViewSize();

		reCalculateCoordinate();
	}

	protected void beforeDrawBasemap(Canvas canvas) {
		;
	}

	protected void afterDrawBasemap(Canvas canvas) {
		;
	}

	protected void afterFirstOverlays(Canvas canvas) {
		;
	}

	protected void afterSecondOverlays(Canvas canvas) {
		;
	}

	protected void onDraw(Canvas canvas) {
		canvas.save();
		super.onDraw(canvas);
		canvas.drawRGB(255, 255, 255);

		if (!inited) {
			init();
		}

		beforeDrawBasemap(canvas);
		drawBasemap(canvas);
		afterDrawBasemap(canvas);

		if (IS_DEBUG) {
			final GeoPoint newCentrePt = viewToGeo((getWidth() >> 1),
					(getHeight() >> 1));

			final PixelPoint point = geoToViewOffset(newCentrePt);
			canvas.drawCircle(point.getX(), point.getY(), 10, debugBluePaint);
		}

		// flush the unused tiles
		tileSource.commitCache();
		boolean isStopped = isStopMoveDetecting();
		if (isStopped || drawOverlaysWhenTouching) {
			synchronized (inDisplayOverlayList) {
				// draw all overlay
				for (Overlay overlay : inDisplayOverlayList) {
					overlay.onDraw(canvas);
				}

				afterFirstOverlays(canvas);

				if (drawScaleBar) {
					scaleOverlay.onDraw(canvas);
				}

				for (Overlay overlay : inDisplayOverlayList) {
					overlay.onDrawFinished(canvas);
				}
				afterSecondOverlays(canvas);
			}
		}

		canvas.restore();

	}

	protected void drawBasemap(Canvas canvas) {

		final int drawTileWith = (int) Math.ceil(tileWidth * mTouchScale);
		final int drawTileHeight = (int) Math.ceil(tileHeight * mTouchScale);

		drawFirstOffsetX = (int) ((mUpperLeftTileOffsetX - mTouchCentreX)
				* (mTouchScale - 1) + mTouchMapOffsetX * mTouchScale);
		drawFirstOffsetY = (int) ((mUpperLeftTileOffsetY - mTouchCentreY)
				* (mTouchScale - 1) + mTouchMapOffsetY * mTouchScale);

		for (int i = 0, ii = tiles.length; i < ii; i++) {

			final DrawTileData tile = tiles[i];
			if (tile != null) {

				final Bitmap bitmap = tileSource.getTile(tile.tileUrl);
				if (!bitmap.isRecycled()) {

					final int drawX = drawFirstOffsetX
							+ (int) ((tile.viewOffsetX) * mTouchScale);
					final int drawY = drawFirstOffsetY
							+ (int) ((tile.viewOffsetY) * mTouchScale);

					final Rect r = new Rect(drawX, drawY, drawX + drawTileWith,
							drawY + drawTileHeight);

					canvas.drawBitmap(bitmap, null, r, paint);

					if (IS_DEBUG) {
						canvas.drawText("Z_Y_X : " + tiles[i].tileUrl, drawX,
								drawY + 12, paint);
					}

				}

			}
		}
	}

	/**
	 * Overlay control
	 */

	/**
	 * Add a overlay object here
	 * 
	 * @param overlay
	 */
	public void addOverlay(Overlay overlay) {
		overlayList.add(overlay);
	}

	public void clearAllOverlay() {
		overlayList.clear();
	}

	public List<Overlay> getOverlayer() {
		return overlayList;
	}

	public boolean removeOverlay(Overlay overlay) {
		return overlayList.remove(overlay);
	}

	/***
	 * Touch event handling
	 */
	@Override
	// Pass the touch event to different object
	public boolean onTouchEvent(MotionEvent event) {

		mMultiDetector.onTouchEvent(event);
		this.mGestureDetector.onTouchEvent(event);
		// dumpEvent(event);
		return true; // indicate event was handled

		// // Multi touch hanlding
		// // Separate different action to below methods
		// this.mMultiDetector.onTouchEvent(ev);
		// return super.onTouchEvent(ev);
	}

	@Override
	public boolean onDown(MotionEvent ev) {
		// flags to control the touch method
		setStopMoveDetecting(false);
		moved = false;
		// save the newest down positon
		this.mTouchDownX = (int) ev.getX();
		this.mTouchDownY = (int) ev.getY();

		mTouchCentreX = mTouchDownX;
		mTouchCentreY = mTouchDownY;

		// force repaint the view
		invalidate();
		return true;
	}

	@Override
	public boolean onMove(MotionEvent event, int count, float x1, float y1,
			float x2, float y2) {

		// if uer move less than 6 pixel, treat it as noise
		if (Math.max(Math.abs(mTouchDownX - event.getX()),
				Math.abs(mTouchDownY - event.getY())) > 20
				&& !isStopMoveDetecting()) {
			moved = true;
			// get real offset of the view
			final float aRotateToAngle = 360 - rotatedAngle;
			mTouchMapOffsetX = (int) (Math.sin(Math.toRadians(aRotateToAngle)) * (event
					.getY() - mTouchDownY))
					+ (int) (Math.cos(Math.toRadians(aRotateToAngle)) * (event
							.getX() - mTouchDownX));
			mTouchMapOffsetY = (int) (Math.cos(Math.toRadians(aRotateToAngle)) * (event
					.getY() - mTouchDownY))
					- (int) (Math.sin(Math.toRadians(aRotateToAngle)) * (event
							.getX() - mTouchDownX));

			// get the multi touch sacle
			if (count > 1) {
				final double DiagonalSize = Math.hypot((double) (x1 - x2),
						(double) (y1 - y2));
				mTouchScale = (DiagonalSize / mTouchDiagonalSize);
				mTouchCentreX = ((int) (x1 + x2)) >> 1;
				mTouchCentreY = ((int) (y1 + y2)) >> 1;
			} else {
				mTouchCentreX = (int) event.getX();
				mTouchCentreY = (int) event.getY();
			}

			// force to repaint the view
			invalidate();

		}

		return true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see competition.onedata.android.map.ViewInfo#getMaxZoomLevel()
	 */
	@Override
	public int getMaxZoomLevel() {
		return coordinate.getMaxZoomLevel();
	}

	public void zoomIn() {
		coordinate.zoomIn();
		reCalculateCoordinate();
	}

	public void zoomOut() {
		coordinate.zoomOut();
		reCalculateCoordinate();
	}

	protected void reCalculateCoordinate() {
		updateCoordinate(0, 0);
	}

	private void updateCoordinate(int offsetX, int offsetY) {

		// update the coordinate system
		coordinate.setViewOffset(offsetX, offsetY);

		// prepare the tile list to draw
		tiles = null;
		final IntegerRectangular displayTileRange = coordinate
				.getDisplayTileRange();
		displayPixelRange = coordinate.getPixelDisplayRange();
		final IntegerRectangular levelTileRange = coordinate
				.getLevelTileRange();
		displayGeoare = coordinate.getGeoDisplayArea();

		// DEBUG

		final int maxX = displayTileRange.getMaxX();
		final int maxY = displayTileRange.getMaxY();
		final int minX = displayTileRange.getMinX();
		final int minY = displayTileRange.getMinY();

		final int updateCoordinateLen = (maxY - minY + 1) * (maxX - minX + 1);
		final int updateCoordinateLen2 = updateCoordinateLen > 0 ? updateCoordinateLen
				: 0;
		final int firstTileOffsetX = -displayPixelRange.getMinX() % tileWidth;
		final int firstTileOffsetY = -displayPixelRange.getMinY() % tileHeight;
		mUpperLeftTileOffsetX = firstTileOffsetX > 0 ? firstTileOffsetX
				- tileWidth : firstTileOffsetX;
		mUpperLeftTileOffsetY = firstTileOffsetY > 0 ? firstTileOffsetY
				- tileHeight : firstTileOffsetY;

		int tileIndex = 0;
		tiles = new DrawTileData[updateCoordinateLen2];
		final int zoomLevel = coordinate.getZoomLevel();

		// row
		for (int i = minY, tileOffsetY = mUpperLeftTileOffsetY; i <= maxY; i++, tileOffsetY += tileHeight) {
			// column
			for (int j = minX, tileOffsetX = mUpperLeftTileOffsetX; j <= maxX; j++, tileOffsetX += tileWidth) {

				if (i >= levelTileRange.getMinY()
						&& i <= levelTileRange.getMaxY()
						&& j >= levelTileRange.getMinX()
						&& j <= levelTileRange.getMaxX()) {

					if (i >= 0 && i < coordinate.getRowCount() && j >= 0
							& j < coordinate.getColCount())
						tiles[tileIndex++] = new DrawTileData(zoomLevel + "_"
								+ i + "_" + j, tileOffsetX, tileOffsetY);
				}
			}
		}
		// release the touch offset
		mTouchMapOffsetX = 0;
		mTouchMapOffsetY = 0;

		onAfterCoordinateUpdated();
		updateDrawingOverlayList();

		// force to repaint the view
		invalidate();

	}

	protected void updateDrawingOverlayList() {
		synchronized (inDisplayOverlayList) {

			// Update the list to draw the overlay;
			inDisplayOverlayList.clear();
			if (!overlayList.isEmpty()) {
				for (Overlay overlay : overlayList) {
					if (overlay != null && overlay.isDraw(displayGeoare)) {
						inDisplayOverlayList.add(overlay);
					}
				}
			}
		}
	}

	public void refreshOverlayList() {
		updateDrawingOverlayList();
	}

	/**
	 * Function call just after the corrdiante updated and before to draw the
	 * view
	 */
	protected void onAfterCoordinateUpdated() {

	}

	protected void onAfterOverlayUpdated() {

	}

	@Override
	public boolean onUp(MotionEvent ev) {
		setStopMoveDetecting(true);

		if (moved)
			updateCoordinate(mTouchMapOffsetX, mTouchMapOffsetY);

		// release the touch offset
		mTouchMapOffsetX = 0;
		mTouchMapOffsetY = 0;

		mTouchScale = 1;
		invalidate();
		return true;
	}

	@Override
	public boolean onMultiDown(MotionEvent ev, float x1, float y1, float x2,
			float y2) {
		mTouchDiagonalSize = Math.hypot((double) (x1 - x2), (double) (y1 - y2));
		return true;

	}

	public void setScale(int scale) {
		coordinate.setScale(scale);
	}

	@Override
	public double getScale() {
		return coordinate.getScale();

	}

	@Override
	public boolean onMultiUp(MotionEvent ev) {

		if (moved) {

			final GeoPoint newCentrePt = viewToGeo((getWidth() >> 1),
					(getHeight() >> 1));

			setGeoCentre(newCentrePt);
			if (mTouchScale > 1) {
				// limit the scale up action to have a better feeling
				mTouchScale = mTouchScale < 3 ? mTouchScale : 3;
				coordinate.setZoomLevelIndex(coordinate.getZoomLevel()
						+ (int) Math.round(mTouchScale) - 1);
			} else
				coordinate.setZoomLevelIndex(coordinate.getZoomLevel()
						- (int) Math.round(1 / mTouchScale) + 1);
			updateCoordinate(0, 0);
		}
		mTouchScale = 1;
		setStopMoveDetecting(true);
		mTouchMapOffsetX = 0;
		mTouchMapOffsetY = 0;
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		coordinate.panTo((int) e.getX(), (int) e.getY());
		coordinate.zoomIn();
		reCalculateCoordinate();
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// check if overlay has event handle
		for (Overlay overlay : this.overlayList) {
			if (overlay.onSingleTapUp(e))
				return true;
		}

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// check if overlay has event handle
		for (Overlay overlay : this.inDisplayOverlayList) {
			if (overlay.onLongPress(e))
				return;
		}
	}

	public boolean isDrawOverlaysWhenTouching() {
		return drawOverlaysWhenTouching;
	}

	public void setDrawOverlaysWhenTouching(boolean drawOverlaysWhenTouching) {
		this.drawOverlaysWhenTouching = drawOverlaysWhenTouching;
	}

	public PixelPoint geoToViewOffset(GeoPoint geoPoint) {
		PixelPoint pt = coordinate.geoToViewOffset(geoPoint);
		pt.setX(drawFirstOffsetX + (int) ((pt.getX()) * mTouchScale));
		pt.setY(drawFirstOffsetY + (int) ((pt.getY()) * mTouchScale));
		return pt;
	}

	public PixelPoint[] geoToViewOffset(GeoPoint[] geoPoints) {
		PixelPoint[] pts = coordinate.geoToViewOffset(geoPoints);
		PixelPoint pt;
		for (int i = 0, ii = pts.length; i < ii; i++) {
			pt = pts[i];
			pt.setX(drawFirstOffsetX + (int) ((pt.getX()) * mTouchScale));
			pt.setY(drawFirstOffsetY + (int) ((pt.getY()) * mTouchScale));
		}
		return pts;
	}

	public GeoPoint viewToGeo(double x, double y) {
		return viewToGeo((int) x, (int) y);
	}

	public GeoPoint viewToGeo(int x, int y) {
		return coordinate.viewToGeo(
				(int) ((x - drawFirstOffsetX) / mTouchScale),
				(int) ((y - drawFirstOffsetY) / mTouchScale));
	}

	public Paint getPaint() {
		return paint;
	}

	public void setZoomLevelIndex(int level) {
		coordinate.setZoomLevelIndex(level);
	}

	public void setGeoCentre(GeoPoint point) {
		coordinate.setGeoCentre(point);
	}

	@Override
	public IntegerRectangular getDisplayPixelRange() {
		return displayPixelRange;
	}

	@Override
	public DoubleRactangular getDisplayGeoArea() {
		return displayGeoare;
	}

	@Override
	public int getZoomLevel() {
		return coordinate.getZoomLevel();
	}

	public double getPixelPerMeter() {
		return coordinate.getPixelPerMeter();
	}

	public void close() {
		tileSource.close();
	}

	private synchronized boolean isStopMoveDetecting() {
		return stopMoveDetecting;
	}

	private synchronized void setStopMoveDetecting(boolean stopMoveDetecting) {
		this.stopMoveDetecting = stopMoveDetecting;
	}

	private void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth > 0 ? tileWidth : 1;
	}

	private void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight > 0 ? tileHeight : 1;
	}

	@Override
	public IntegerRectangular getDisplayTileRange() {
		return coordinate.getDisplayTileRange();
	}

	@Override
	public void repaint() {
		invalidate();
	}

}
