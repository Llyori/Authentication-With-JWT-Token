package com.Travel.Hot.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Travel.Hot.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {
	
	

}
