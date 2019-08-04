package competition.onedata.android.map.tile.online.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TileCacheSqliteOpenHelper extends SQLiteOpenHelper {
	private String TAG = TileCacheSqliteOpenHelper.class.getName();

	private static final String[] ON_CREATE_SQLS = new String[] { "CREATE TABLE tiles (z int,  y int, x int ,  image blob, get_date date, PRIMARY KEY  (z,y,x));" };
	private static final String[] ON_DROP_SQLS = new String[] { "DROP TABLE IF EXISTS tiles;" };

	private SQLiteDatabase db;

	public TileCacheSqliteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		db = this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (String sql : ON_CREATE_SQLS) {
			Log.i(TAG, "Executing SQL: " + sql);
			db.execSQL(sql);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		cleanDB(db);
	}

	private void cleanDB(SQLiteDatabase db) {
		for (String sql : ON_DROP_SQLS) {
			Log.i(TAG, "Executing SQL: " + sql);
			db.execSQL(sql);
		}
		onCreate(db);
	}

}
