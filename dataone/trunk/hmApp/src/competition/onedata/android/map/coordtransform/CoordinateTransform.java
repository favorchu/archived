package competition.onedata.android.map.coordtransform;

public class CoordinateTransform
{
	private static final double a = 6378388.0;
	private static final double e2 = 6.722670022E-3;
	private static final double A0 = 1 - e2/4 - (3.0*e2*e2)/64;
	private static final double A2 = (3.0/8.0)*(e2+(e2*e2)/4);
	private static final double A4 = (15.0*e2*e2)/256;
	
	private static final double N0 = 819069.80;
	private static final double E0 = 836694.05;
//	private static final double m0 = 1.0;
	private static final double M0 = 2468395.723;
	private static final double Vs = 6381480.500;
//	private static final double Ps = 6359840.760;
	private static final double Ys = 1.003402560;
//	private static final double lat0 = Math.toRadians(22.0+18.0/60.0+43.68/3600.0);
	private static final double lon0 = Math.toRadians(114.0+10.0/60.0+42.8/3600.0);
	

	public static double tranN(double lat, double lon) throws Exception
	{
		double N = 0;
		lat = lat + 5.5/3600;
		lon = lon -8.8/3600;
		double radLat = Math.toRadians(lat);
		double radLon = Math.toRadians(lon);
		try
		{	
			double M = calMeridianDis(radLat);
			N = N0 + (M - M0) + Vs*Math.sin(radLat) * (Math.pow((radLon - lon0),2)/2.0) * Math.cos(radLat) ;
		}
		catch(Exception e)
		{
			throw e;
		}
		return N;
	}
	
	public static double tranE(double lat, double lon) throws Exception
	{
		double E = 0;
		lat = lat + 5.5/3600;
		lon = lon -8.8/3600;
		
		double radLat = Math.toRadians(lat);
		double radLon = Math.toRadians(lon);
		try
		{
//			double M = calMeridianDis(radLat);
			E = E0 + Vs * (radLon - lon0) * Math.cos(radLat) + Vs * (Math.pow((radLon - lon0), 3)/6.0) * Math.pow(Math.cos(radLat), 3) * (Ys - Math.pow(Math.tan(radLat), 2));
		}
		catch(Exception e)
		{
			throw e;
		}
		return E;
	}
	
	
	private static double calMeridianDis(double lat) throws Exception
	{
		double M = 0;
		try
		{
			M = a*(A0*lat - A2*Math.sin(2.0*lat) + A4*Math.sin(4.0*lat));
		}
		catch(Exception e)
		{
			throw e;
		}
		return M;
	}
}
