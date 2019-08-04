package competition.onedata.android.hongming;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;

public abstract class AbstractActivity extends Activity {
	protected static final int ACTIVITY_FLAG_EDIT_ROADS = 8083;
	protected static final ExecutorService threadPool = Executors
			.newFixedThreadPool(3);

}
