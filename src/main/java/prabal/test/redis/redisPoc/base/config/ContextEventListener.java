package prabal.test.redis.redisPoc.base.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ContextEventListener {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@EventListener(classes = { ContextClosedEvent.class, ContextStoppedEvent.class })
	public void onApplicationEvent(ContextClosedEvent event) {
		logger.info("Stopping all services. We can add some before shutdown must done activities here");
	}

}
