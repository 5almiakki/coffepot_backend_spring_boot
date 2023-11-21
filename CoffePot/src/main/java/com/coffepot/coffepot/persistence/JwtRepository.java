package com.coffepot.coffepot.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coffepot.coffepot.model.RefreshToken;

@Repository
public interface JwtRepository extends JpaRepository<RefreshToken, Long> {
	
	public Optional<RefreshToken> findByRefreshToken(String token);
	public Optional<RefreshToken> findByUserId(String userId);

}
