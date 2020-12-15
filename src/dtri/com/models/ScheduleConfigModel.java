package dtri.com.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class ScheduleConfigModel extends WebMvcConfigurationSupport {
	// log 訊息
	private static Logger logger = LogManager.getLogger();

	@Bean
	public TaskScheduler taskScheduler() {
		logger.info("Setting up thread pool task scheduler with 20 threads.");
		System.out.println("Setting up thread pool task scheduler with 20 threads.");
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(20);
		scheduler.setThreadNamePrefix("scheduled-thread-");
		scheduler.setAwaitTerminationSeconds(60);
		scheduler.setWaitForTasksToCompleteOnShutdown(true);
		scheduler.setErrorHandler(t -> logger.error("Unknown error occurred while executing task.", t));
		scheduler.setRejectedExecutionHandler(
				(r, e) -> logger.error("Execution of task {} was rejected for unknown reasons.", r));
		
		return scheduler;
		 //return new ConcurrentTaskScheduler();
	}
}
