package competition.onedata.backendjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import competition.onedata.job.StandaloneJob;

public class BackendJob implements Runnable {

	private static final int AN_HOUR = 1000 * 60 * 60;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(StandaloneJob.class);

	boolean loop;
	boolean stopOnException = false;
	int sleepTime = 1000 * 60 * 5;
	private StandaloneJob job;

	public BackendJob(StandaloneJob job, boolean loop) {
		LOGGER.info("New back end job: {}", job.getTaskName());
		this.job = job;
		this.loop = loop;
	}

	public void run() {
		int trueSleepTime = sleepTime;
		do {
			LOGGER.info("Running job: {}", job.getTaskName());
			try {

				job.run();

				// no exception found , restore to the default time
				trueSleepTime = sleepTime;
			} catch (Exception e) {
				LOGGER.error(job.getTaskName() + " - " + e.getMessage(), e);
				// Whenever there is an error, double the sleep time till an
				// hour
				trueSleepTime *= 2;
				trueSleepTime = trueSleepTime < AN_HOUR ? trueSleepTime
						: AN_HOUR;

				if (stopOnException)
					break;
			}

			LOGGER.info("Task \"{}\" end and Sleep: {}", job.getTaskName(),
					trueSleepTime / 1000);

			try {
				Thread.sleep(trueSleepTime);
			} catch (InterruptedException e) {
				LOGGER.error(e.getMessage(), e);

			}
		} while (loop);

		LOGGER.info("Task \"{}\" terminated", job.getTaskName());
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

}
