package prabal.test.redis.redisPoc.base.redis.listeners;

import org.redisson.api.map.event.EntryCreatedListener;
import org.redisson.api.map.event.EntryEvent;
import org.redisson.api.map.event.EntryUpdatedListener;

public class RedisEntryCreatedListenerImpl<K, V> implements EntryCreatedListener<K, V> , EntryUpdatedListener<K, V>{
	
	@Override
	public void onCreated(EntryEvent<K, V> event) {
		// TODO Auto-generated method stub
		System.out.println("This is cache entry Created listener");
	}

	@Override
	public void onUpdated(EntryEvent<K, V> event) {
		// TODO Auto-generated method stub
		System.out.println("This is cache entry Updated listener");
	}

}
