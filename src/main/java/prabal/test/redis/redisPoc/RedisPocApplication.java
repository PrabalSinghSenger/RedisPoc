package prabal.test.redis.redisPoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "prabal.test.redis.*")
@EnableJpaRepositories
@EnableCaching
@EnableRedisRepositories
public class RedisPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisPocApplication.class, args);
	}

}
