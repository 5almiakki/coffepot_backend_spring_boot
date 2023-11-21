package com.coffepot.coffepot.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coffepot.coffepot.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

	UserEntity findByUsername(String username);
	
	Boolean existsByEmail(String email);

	Boolean existsByUsername(String username);
	
	Optional<UserEntity> findByEmail(String email);

	UserEntity findByUsernameAndPassword(String username, String password);

}
