
package prabal.test.redis.redisPoc.base.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import org.redisson.Redisson;
import org.redisson.api.AsyncIterator;
import org.redisson.api.MapOptions;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.MapLoader;
import org.redisson.api.map.MapLoaderAsync;
import org.redisson.api.map.MapWriter;
import org.redisson.api.map.MapWriterAsync;
import org.redisson.api.map.event.EntryCreatedListener;
import org.redisson.api.map.event.EntryEvent;
import org.redisson.api.map.event.EntryExpiredListener;
import org.redisson.api.map.event.EntryRemovedListener;
import org.redisson.api.map.event.EntryUpdatedListener;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import prabal.test.redis.redisPoc.cache.model.User;
import prabal.test.redis.redisPoc.cache.repository.UserRepository;

@Configuration
public class RedisConfig implements InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

	String[] masterNodes = "redis://127.0.0.1:30001,redis://127.0.0.1:30002,redis://127.0.0.1:30003".split(",");
	String[] slaveNodes = "redis://127.0.0.1:30004,redis://127.0.0.1:30005,redis://127.0.0.1:30006".split(",");

	@Value("${spring.redis.cluster.nodes}")
	private String cluster;

	@Value("${spring.profiles.active}")
	private String envProfile;

	private final String CACHE_NAME = "test-redission-cache";

	
	protected RedissonClient redissonClient;

	@Autowired
	private UserRepository userRepository;
	

	/**
	 * to use  WRITE_THROUGH mode for cache
	 * */

	/*
	 * @Bean public RMap<Long, User> userRMap() { RMap<Long, User> userRMap =
	 * (RMap<Long, User>) redissonClient.getMap(CACHE_NAME, MapOptions.<Long,
	 * User>defaults().writer(getMapWriter()).writeMode(MapOptions.WriteMode.
	 * WRITE_THROUGH)); return userRMap; }
	 */
	
	/**
	 * This is used to RLocalCachedMap apart from caching in Key value pair it also facilitate 
	 * with Event Listener for all kind of events 
	 * */
//	@Bean
//	public RMap<Long, User> userRMap1() {
//		RLocalCachedMap<Long, User> rLocalCachedMap =  redissonClient.getLocalCachedMap(CACHE_NAME,
//				LocalCachedMapOptions.<Long, User>defaults().writerAsync(getMapWriterAsync()).writeMode(MapOptions.WriteMode.WRITE_BEHIND)
//				.writeBehindBatchSize(10).writeBehindDelay(5000));
//		
//		return rLocalCachedMap;
//	}
	

	/**
	 * to use  WRITE_BEHIND mode for cache with Loader feature
	 * */
	@Bean
	public RMap<Long, User> userRMap() {
		RMap<Long, User> userRMap = (RMap<Long, User>) redissonClient.getMap(CACHE_NAME,
				MapOptions.<Long, User>defaults().writerAsync(getMapWriterAsync()).writeMode(MapOptions.WriteMode.WRITE_BEHIND)
				.writeBehindBatchSize(10).writeBehindDelay(5000).loader( getMapLoader()));
		
		return userRMap;
	}
	
	

	private MapLoader<Long, User> getMapLoader() {
		return new MapLoader<Long, User>() {
		

			@Override
			public User load(Long key) {
				// TODO Auto-generated method stub
				return getUserById(key);
			}

			@Override
			public Iterable<Long> loadAllKeys() {
				return getUserKeyIterable();
			}
		};
	}
	
	private MapLoaderAsync<Long, User> getMapLoaderAsync() {
		return new MapLoaderAsync<Long, User>() {

			@Override
			public CompletionStage<User> load(Long key) {
				// TODO Auto-generated method stub
				CompletionStage<User> cf = CompletableFuture.supplyAsync(() -> getUserById(key));
				return cf;
			}

			@Override
			public AsyncIterator<Long> loadAllKeys() {
				return getUserKeyIterator();
			}
		};
	}

	//	@Bean
	public RMapCache<Long, User> userRMapCache() {

			RMapCache<Long, User> userRMapCache = redissonClient.getMapCache(CACHE_NAME + "_DEMO",
				MapOptions.<Long, User>defaults().writer(getMapWriter()).writeMode(MapOptions.WriteMode.WRITE_THROUGH));

		int updateListener = userRMapCache.addListener(new EntryUpdatedListener<Long, User>() {

			@Override
			public void onUpdated(EntryEvent<Long, User> event) {
				System.out.println("onUpdated");
				System.out.println(event.getSource().toString());
				event.getKey(); // key
				event.getValue();
			}

		});

		int createListener = userRMapCache.addListener(new EntryCreatedListener<Integer, Integer>() {

			@Override
			public void onCreated(EntryEvent<Integer, Integer> event) {
				System.out.println("onCreated");
				event.getKey();
				System.out.println(event.getSource().toString());
			}
		});

		int expireListener = userRMapCache.addListener(new EntryExpiredListener<Integer, Integer>() {

			@Override
			public void onExpired(EntryEvent<Integer, Integer> event) {
				System.out.println("onExpired");
				event.getKey();
				System.out.println(event.getSource().toString());
			}
		});

		int removeListener = userRMapCache.addListener(new EntryRemovedListener<Integer, Integer>() {

			@Override
			public void onRemoved(EntryEvent<Integer, Integer> event) {
				System.out.println("onRemoved");
				event.getKey();
				System.out.println(event.getSource().toString());
			}
		});

		return userRMapCache;
	}

	private MapWriterAsync<Long, User> getMapWriterAsync() {
		return new MapWriterAsync<Long, User>() {

			@Override
			public CompletionStage<Void> write( Map<Long, User> map) {
				CompletionStage<Void> cf = CompletableFuture.runAsync(() -> saveOrUpdate(map));
				return cf;
			}

			@Override
			public CompletionStage<Void> delete(Collection<Long> keys) {
				
				return CompletableFuture.runAsync(() -> removeOrDelete(keys));
			}
		};
	}

	private Iterable<Long> getUserKeyIterable(){
		System.out.println("This is for Loader getUserKeyIterable  ");
		List<User> users = userRepository.findAll();
		List<Long> usersKeyList = users.stream().map(User::getUserId).collect(Collectors.toList());
		return usersKeyList;
	}
	
	private AsyncIterator<Long> getUserKeyIterator(){
		System.out.println("This is for Loader getUserIterator  ");
		List<User> users = userRepository.findAll();
		List<Long> usersKeyList = users.stream().map(User::getUserId).collect(Collectors.toList());
		return AsyncIterator.class.cast(usersKeyList);
	}
	

	private User getUserById(Long id){
		System.out.println("This is for Loader getUserById : "+id);
		Optional<User> u = userRepository.findById(id);
		if (u.isPresent()) {

			return u.get();
		}
		return null;
	}
	
	private void saveOrUpdate( Map<Long, User> map) {
		
		map.forEach((K,V)->{
			System.out.println("This is for saving repo call");
			User dbUser = userRepository.save(V);
			redissonClient.getMap(CACHE_NAME).remove(K, V);
			redissonClient.getMap(CACHE_NAME).replace(dbUser.getUserId(), V, dbUser);
//			redissonClient.getMap(CACHE_NAME).loadAll(true, 1);
		});
	}
	
	private void removeOrDelete( Collection<Long> keys) {
		keys.stream().forEach(e -> {
			System.out.println("This is for rmoving key : "+e.intValue());
			Optional<User> u = userRepository.findById((long) e.intValue());
			if (u.isPresent()) {
				userRepository.delete(u.get());
//				redissonClient.getMap(CACHE_NAME).remove((long) e.intValue());
			}
			redissonClient.getMap(CACHE_NAME).loadAll(false, 1);
		});
	}
	
	private MapWriter<Long, User> getMapWriter() {
		return new MapWriter<Long, User>() {

			@Override
			public void write( Map<Long, User> map) {

				map.forEach((k, v) -> {
					User dbUser = userRepository.save(v);
//					redissonClient.getMap(CACHE_NAME).put(dbUser.getUserId(), dbUser);
				});
			}

			@Override
			public void delete(Collection<Long> keys) {
				keys.stream().forEach(e -> {
					Optional<User> u = userRepository.findById((long) e.intValue());
					if (u.isPresent()) {
						userRepository.delete(u.get());
						redissonClient.getMap(CACHE_NAME).remove((long) e.intValue());
					}
				});
			}

		};
	}

	@Bean
	public RedissonClient getRedisson() {
		Config config = new Config();
		if (envProfile.contentEquals("local")) {
			config.useSingleServer().setAddress("redis://127.0.0.1:6379");
		} else {
			for (int i = 0; i < masterNodes.length; i++) {
				config.useClusterServers().addNodeAddress(masterNodes[i]).setClientName("REDIS_POC_CLIENT").setRetryAttempts(3);
				config.useClusterServers().addNodeAddress(slaveNodes[i]).setClientName("REDIS_POC_CLIENT").setReadMode(ReadMode.SLAVE);
			}
			config.useClusterServers().setScanInterval(20).setPingConnectionInterval(20);
		}
		try {
			this.redissonClient = Redisson.create(config);
			logger.debug("Redis connection Successfully.");
		} catch (Exception e) {
			logger.error("Error while configuring Redis nodes .", e);
		}
		return redissonClient;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String[] nodes = cluster.split(",");
		Config config = new Config();
		if (envProfile.contentEquals("local")) {
			config.useSingleServer().setAddress("redis://127.0.0.1:6379");
		} else {
			for (int i = 0; i < masterNodes.length; i++) {
				config.useClusterServers().addNodeAddress(masterNodes[i]).setClientName("REDIS_POC_CLIENT").setRetryAttempts(3);
				config.useClusterServers().addNodeAddress(slaveNodes[i]).setClientName("REDIS_POC_CLIENT").setReadMode(ReadMode.SLAVE);
			}
			config.useClusterServers().setScanInterval(20).setPingConnectionInterval(20);
		}
		try {
			this.redissonClient = Redisson.create(config);
			logger.debug("Redis connection Successfully.");
		} catch (Exception e) {
			logger.error("Error while configuring Redis nodes .", e);
		}

	}

	
	
	
}
