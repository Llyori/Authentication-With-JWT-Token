package com.Travel.Hot.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Travel.Hot.Repository.RoleRepo;
import com.Travel.Hot.Repository.UserRepo;
import com.Travel.Hot.domain.Role;
import com.Travel.Hot.domain.User;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private PasswordEncoder passwordEncoder ; 

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = FindUserByEmail(email);
		if(user == null) {
			System.out.println("Cet utilisateur est pas dans la base de donnees");
			throw new UsernameNotFoundException("Cet utilisateur est pas dans la base de donnees");
		} else {
			System.out.println("Je l ai recupere "+user);
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(user.getRoles().getNomRole()));
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}  
	
	@Override
	public User saveUser(User user) {
		System.out.println("Saving new User {} to the database"+user.getNom());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepo.save(user);
	}

	@Override
	public Role saveRole(Role role) {
		System.out.println("Saving new Role {} to the database"+role.getNomRole());
		return roleRepo.save(role);
	}

	/*@Override
	public void addRoleToUser(Long idUser, Long idRole) {
		System.out.println("Role {} adding to User {} to the database"+idUser+" "+idRole);
		User user = userRepo.findById(idUser).get();
		Role role = roleRepo.findById(idRole).get();
		user.setRoles(role);
		userRepo.save(user);
	}*/

	@Override
	public User getUser(Long idUser) {
		return userRepo.findById(idUser).get();
	}

	@Override
	public List<User> getUsers() {
		System.out.println("Find Users");
		return userRepo.findAll();
	}

	@Override
	public User FindUserByEmail(String Email) {
		User u = null;
		List<User> users = getUsers();
		for(User user: users)
			if(user.getEmail().equals(Email)) {
				u = user;
				break;
			}
		return u;
	}


}
