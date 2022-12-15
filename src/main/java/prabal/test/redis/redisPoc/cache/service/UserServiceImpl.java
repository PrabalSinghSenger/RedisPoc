package prabal.test.redis.redisPoc.cache.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import prabal.test.redis.redisPoc.cache.model.User;
import prabal.test.redis.redisPoc.cache.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	 @Autowired
	  private UserRepository userRepository;

	  @Autowired
	  private RedissonClient redissonClient;

	  @Autowired
	  private RMap<Long, User> userRMap;



	
	@Override
	public List<User> getUsers() {
		List<User> employeeList =  userRepository.findAll();
	    return employeeList;
	}

	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		System.out.println("Save");
		userRMap.put(user.getUserId(), user);
		userRMap.loadAll(false, 1);
		return user;
	}
	

	@Override
	public User getUsersById(Long id) {
		User user = null;
		user = userRMap.get(id);
		 return user;
	}

	@Override
	public List<User> readAllValueFromCache(boolean isCached) {
		// TODO Auto-generated method stub
		List<User> users = null;
				if(isCached) {
					userRMap.readAllKeySet();
					users = (List<User>) userRMap.readAllValues();
				}else {
					users = userRepository.findAll();
				}
		return users;
	}

	@Override
	public String getConfig() {
		// TODO Auto-generated method stub
		try {
			return redissonClient.getConfig().toJSON();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean deleteUser(Long id) {
		
		try {
		if(null == userRMap.get(id)) {
			userRepository.deleteById(id);
		}
		 userRMap.remove(id);
		 return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			userRMap.loadAll(false, 1);
		}
		 
	}

	@Override
	public Set<Long> getAllUsersIds() {
		userRMap.loadAll(false, 1);
		return userRMap.keySet();	
	}
	


}
