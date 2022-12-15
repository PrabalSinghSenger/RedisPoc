package prabal.test.redis.redisPoc.cache.service;

import java.util.List;
import java.util.Set;

import prabal.test.redis.redisPoc.cache.model.User;



public interface UserService {

	Set<Long> getAllUsersIds();
	 List<User> getUsers();
	 List<User> readAllValueFromCache(boolean isCached);
	 User save(User user);
	 User getUsersById(Long id);
	 boolean deleteUser(Long id);
	 String getConfig();
}
