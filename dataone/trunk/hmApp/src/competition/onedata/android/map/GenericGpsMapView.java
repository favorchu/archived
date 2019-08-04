package competition.onedata.android.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import competition.onedata.android.map.coordtransform.CoordinateTransform;
import competition.onedata.android.map.coordtransform.Hk80Location;
import competition.onedata.android.map.overlap.LocationOverlay;
import competition.onedata.android.map.util.GeoPoint;

public abstract class GenericGpsMapView extends BasicMapView {

	private static String TAG = GenericGpsMapView.class.getSimpleName();
//
//	private LocationListener locationListener;
////	private LocationManager mlocManager;
//	private boolean gpsEnabled = false;
	private Context context;
//	private Hk80Location hk80location;
//	private Location location;
//	private LocationOverlay localOverlay;
//	private boolean localOverlayInView = false;
//	private boolean drawLocationOverlay = true;

	public GenericGpsMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

//		mlocManager = (LocationManager) context
//				.getSystemService(Context.LOCATION_SERVICE);
//
//		locationListener = new LocationListener() {
//
//			private Context context = GenericGpsMapView.this.context;
//
//			@Override
//			public void onStatusChanged(String provider, int status,
//					Bundle extras) {
//				Log.i(TAG, "GPS status changed.");
//				onGpsStatusChanged(provider, status, extras);
//			}
//
//			@Override
//			public void onProviderEnabled(String provider) {
//				Log.i(TAG, "GPS enabled");
//				Toast.makeText(context, context.getString(R.string.gpsOn),
//						Toast.LENGTH_SHORT).show();
//				onGpsEnabled(provider);
//
//			}
//
//			@Override
//			public void onProviderDisabled(String provider) {
//				Log.i(TAG, "GPS disabled");
//				Toast.makeText(context, context.getString(R.string.gpsOff),
//						Toast.LENGTH_SHORT).show();
//				onGpsDisabled(provider);
//			}
//
//			@Override
//			public void onLocationChanged(Location location) {
//				Hk80Location basicLoc = null;
//				try {
//					basicLoc = new Hk80Location();
//					basicLoc.setN(CoordinateTransform.tranN(
//							location.getLatitude(), location.getLongitude()));
//					basicLoc.setE(CoordinateTransform.tranE(
//							location.getLatitude(), location.getLongitude()));
//
//				} catch (Exception e) {
//					Log.e(TAG, Log.getStackTraceString(e));
//					basicLoc = null;
//					location = null;
//
//				}
//
//				Log.i(TAG, "Location changd: " + basicLoc.getE() + " "
//						+ basicLoc.getN());
//
//				onGpsPosChanged(basicLoc, location);
//
//			}
//		};
//
//		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
//				locationListener);

	}

//	@Override
//	protected void afterDrawBasemap(Canvas canvas) {
//
//		super.afterDrawBasemap(canvas);
//		if (localOverlay != null && drawLocationOverlay && localOverlayInView) {
//			localOverlay.onDraw(canvas);
//		}
//
//	}

//	public void onGpsStatusChanged(String provider, int status, Bundle extras) {
//
//		switch (status) {
////		case LocationProvider.OUT_OF_SERVICE:
////			Log.v(TAG, "Status Changed: Out of Service");
////			Toast.makeText(context, context.getString(R.string.gpsOutOfSer),
////					Toast.LENGTH_SHORT).show();
////			break;
////		case LocationProvider.TEMPORARILY_UNAVAILABLE:
////			Log.v(TAG, "Status Changed: Temporarily Unavailable");
////			Toast.makeText(context, context.getString(R.string.gpsOff),
////					Toast.LENGTH_SHORT).show();
////			break;
////		case LocationProvider.AVAILABLE:
////			Log.v(TAG, "Status Changed: Available");
////			Toast.makeText(context, context.getString(R.string.gpsOn),
////					Toast.LENGTH_SHORT).show();
////			break;
//		}
//	}

//	@Override
//	protected void onAfterCoordinateUpdated() {
//		if (drawLocationOverlay && localOverlay != null) {
//			localOverlayInView = localOverlay.isDraw(getDisplayGeoArea());
//		} else {
//			localOverlay = null;
//		}
//	}

//	public void onGpsEnabled(String provider) {
//		gpsEnabled = true;
//	}
//
//	public void onGpsDisabled(String provider) {
//		gpsEnabled = false;
//		localOverlay = null;
//	}
//
//	public void onGpsPosChanged(Hk80Location hkLoc, Location location) {
//
//		if (hkLoc == null || location == null) {
//			localOverlay = null;
//			return;
//		}
//
//		final double radius = location.getAccuracy();
//		final RectF rect = new RectF((float) (hkLoc.getE() - radius),
//				(float) (hkLoc.getN() + radius),
//				(float) (hkLoc.getE() + radius),
//				(float) (hkLoc.getN() - radius));
//		this.localOverlay = new LocationOverlay(rect, location.getBearing(),
//				this);
//		this.hk80location = hkLoc;
//		this.location = location;
//		reCalculateCoordinate();
//
//	}

//	public LocationListener getLocationListener() {
//		return locationListener;
//	}
//
//	public boolean isGpsEnabled() {
//		return gpsEnabled;
//	}
//
//	public Hk80Location getHK80Location() {
//		return hk80location;
//	}
//
//	public boolean panToGPSPoint() {
//
//		if (localOverlay == null) {
//			Log.i(TAG, "No GPS value");
//			Toast.makeText(context, context.getString(R.string.noGPS),
//					Toast.LENGTH_SHORT).show();
//			return false;
//		}
//		setGeoCentre(new GeoPoint(hk80location.getE(), hk80location.getN()));
//		reCalculateCoordinate();
//
//		if (location != null && hk80location != null) {
//			Toast.makeText(
//					context,
//					location.getLatitude() + "," + location.getLongitude()
//							+ "\nE:" + hk80location.getE() + ",N"
//							+ hk80location.getN(), Toast.LENGTH_SHORT).show();
//			return true;
//		}
//		return false;
//	}
//
//	public Location getLocation() {
//		return location;
//	}
//
//	public boolean isDrawLocationOverlay() {
//		return drawLocationOverlay;
//	}
//
//	public void setDrawLocationOverlay(boolean drawLocationOverlay) {
//		this.drawLocationOverlay = drawLocationOverlay;
//	}
//
//	public boolean isLocalOverlayInView() {
//		return localOverlayInView;
//	}
//
//	@Override
//	public void close() {
//		super.close();
//	//	mlocManager.removeUpdates(locationListener);
//	}
}
