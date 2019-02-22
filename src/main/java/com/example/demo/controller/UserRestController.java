package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/rest")
public class UserRestController {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserService userService;
	
	//get all user + token
	@RequestMapping(value ="/users", method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUser(){
		return new ResponseEntity<>(userService.findAll(),HttpStatus.OK);
	}
	
	//id+token
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	  public ResponseEntity<Object> getUserById(@PathVariable int id) {
	    User user = userService.findById(id);
	    if (user != null) {
	      return new ResponseEntity<Object>(user, HttpStatus.OK);
	    }
	    return new ResponseEntity<Object>("Not Found User", HttpStatus.NO_CONTENT);
	  }
	
	// create new user + token
	@RequestMapping(value ="/users", method = RequestMethod.POST)
	public ResponseEntity<String> createUser(@RequestBody User user){
		if(userService.add(user)) {
			return new ResponseEntity<String>("Create!", HttpStatus.CREATED);	
		}else {
			return new ResponseEntity<String>("User Existed!", HttpStatus.BAD_REQUEST);
		}
	}
	
	//delete + token
	@RequestMapping(value ="/users/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteUserById(@PathVariable int id){
		userService.delete(id);
		return new ResponseEntity<String>("deleted!", HttpStatus.OK);
	}
	
	
	//token + login
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<String> login (HttpServletRequest request, @RequestBody User user){
		String result ="";
		HttpStatus httpStatus = null;
		try {
			if(userService.checkLogin(user)) {
				result = jwtService.generateTokenLogin(user.getUsername());
				httpStatus = HttpStatus.OK;
			}else {
				result = "Wrong userId and password";
				httpStatus = HttpStatus.BAD_REQUEST;
			}
		}catch (Exception e) {
			result = "Server Error!";
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<String>(result, httpStatus);
	}
	
	// register
		@RequestMapping(value ="/register", method = RequestMethod.POST)
		public ResponseEntity<String> registerUser(HttpServletRequest request,@RequestBody User user){
			String result ="";
			HttpStatus httpStatus = null;
			try {
			if(userService.registeradd(user)) {
				return new ResponseEntity<String>("Register!", HttpStatus.CREATED);	
			}else {
				return new ResponseEntity<String>("User Existed!", HttpStatus.BAD_REQUEST);
			}
			}catch (Exception e) {
				result = "Server Error!";
				httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			}
			return new ResponseEntity<String>(result, httpStatus);
		}
}
