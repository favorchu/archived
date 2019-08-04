package competition.onedata.context;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import competition.onedata.backendjob.BackendJob;
import competition.onedata.config.ConfigurationManager;
import competition.onedata.job.realtimeairquality.UpdateRealtimeAirQuality;
import competition.onedata.job.realtimetraffic.UpdateRealtimeTrafficConditionJob;
import competition.onedata.job.realtimeuvindex.UpdateUVIndex;
import competition.onedata.job.realtimeweather.UpdateRealtimeWeatherJob;

public class ServletContextListener implements
		javax.servlet.ServletContextListener {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ServletContextListener.class);

	public void contextInitialized(ServletContextEvent sce) {

		LOGGER.info("contextInitialized()");
		if (ConfigurationManager.RUN_JOB) {

			initBackendJobs();
		}
		LOGGER.info("Initing back end job end...");
	}

	private void initBackendJobs() {
		// Reatime Traffic condition
		if (ConfigurationManager.RUN_JOB_REALTIME_TRAFFIC) {
			BackendJob job = new BackendJob(
					new UpdateRealtimeTrafficConditionJob(), true);
			job.setSleepTime(1000 * 60 * 5);
			new Thread(job).start();
		}

		// Realtime weather
		if (ConfigurationManager.RUN_JOB_REALTIME_WEATHER) {
			BackendJob job = new BackendJob(new UpdateRealtimeWeatherJob(),
					true);
			job.setSleepTime(1000 * 60 * 5);
			new Thread(job).start();
		}

		// Realtime air quality
		if (ConfigurationManager.RUN_JOB_REALTIME_WEATHER) {
			BackendJob job = new BackendJob(new UpdateRealtimeAirQuality(),
					true);
			job.setSleepTime(1000 * 60 * 5);
			new Thread(job).start();
		}

		// Realtime UV Index
		if (ConfigurationManager.RUN_JOB_REALTIME_UV_INDEX) {
			BackendJob job = new BackendJob(new UpdateUVIndex(), true);
			job.setSleepTime(1000 * 60 * 5);
			new Thread(job).start();
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
		LOGGER.info("contextDestroyed()");
	}

}