package com.Travel.Hot.Service;

import java.util.List;

import com.Travel.Hot.domain.Role;
import com.Travel.Hot.domain.User;

public interface UserService {
	
	User saveUser(User user);
	Role saveRole(Role role);
	User getUser(Long idUser);
	User FindUserByEmail(String Email);
	List<User> getUsers();
	
}
