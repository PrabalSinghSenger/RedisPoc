package prabal.test.redis.redisPoc.cache.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import prabal.test.redis.redisPoc.cache.model.User;
import prabal.test.redis.redisPoc.cache.service.UserService;


@RequestMapping(value = "/users/api")
@RestController
public class UserController {

	@Autowired
	UserService service;

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")

	public ResponseEntity<List<User>> fetchUsers() {
		return new ResponseEntity(service.getUsers(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/config", method = RequestMethod.GET, produces = "application/json")

	public ResponseEntity<String> config() {
		return new ResponseEntity(service.getConfig(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/readAllValueFromCache", method = RequestMethod.GET, produces = "application/json")

	public ResponseEntity<List<User>> readAllValueFromCache(@RequestParam boolean isCached) {
		return new ResponseEntity(service.readAllValueFromCache(isCached), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/readAllkeysFromCache", method = RequestMethod.GET, produces = "application/json")

	public ResponseEntity<Set<User>> readAllkeysFromCache(@RequestParam boolean isCached) {
		return new ResponseEntity(service.getAllUsersIds(), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}")
	public User fetchUsers(@PathVariable final Long id) {
		return service.getUsersById(id);
	}

	@RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
	public  ResponseEntity<Boolean>  deleteUser(@PathVariable final Long id) {
		return new ResponseEntity(service.deleteUser(id), HttpStatus.OK);
	}

	@PostMapping
	public User save(@RequestBody User employee) {
		return this.service.save(employee);
	}

}
