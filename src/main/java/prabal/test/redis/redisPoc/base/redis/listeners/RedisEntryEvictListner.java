package prabal.test.redis.redisPoc.base.redis.listeners;

import org.redisson.api.map.event.EntryExpiredListener;
import org.redisson.api.map.event.EntryRemovedListener;

public interface RedisEntryEvictListner<K, V> extends EntryExpiredListener<K, V>, EntryRemovedListener<K, V> {

}
