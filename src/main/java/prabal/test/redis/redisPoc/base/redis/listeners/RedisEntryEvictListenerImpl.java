package prabal.test.redis.redisPoc.base.redis.listeners;

import org.redisson.api.map.event.EntryEvent;

public class RedisEntryEvictListenerImpl<K, V> implements RedisEntryEvictListner<K,V> {

	@Override
	public void onExpired(EntryEvent<K, V> event) {
		// TODO Auto-generated method stub
		System.out.println("This is cache entry onExpired listener");
	}

	@Override
	public void onRemoved(EntryEvent<K, V> event) {
		// TODO Auto-generated method stub
		System.out.println("This is cache entry onRemoved listener");
	}

}
