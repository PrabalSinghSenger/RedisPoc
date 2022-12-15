package prabal.test.redis.redisPoc.cache.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import prabal.test.redis.redisPoc.cache.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}