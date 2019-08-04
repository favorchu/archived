package competition.onedata.job;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class StandaloneJob {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(StandaloneJob.class);

	protected static final boolean IS_DEBUG = true;

	public void run() throws Exception {
		String taskname = getTaskName();
		Date startTime = new Date();
		LOGGER.info("run() start task:{} @ {}", taskname, startTime);

		// Invoke main task
		executeTask();

		Date endTime = new Date();
		LOGGER.info("run() end task:{} @ {}", taskname, endTime);
		long runtime = endTime.getTime() - startTime.getTime();
		LOGGER.info("Runtime: {}ms", runtime);
	}

	protected abstract void executeTask() throws Exception;

	public abstract String getTaskName();

}
