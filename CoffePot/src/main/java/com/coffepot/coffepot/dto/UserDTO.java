package com.coffepot.coffepot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	
	private String id;
	private String username;
	private String email;
	private String password;
	private String accessToken;
	private String refreshToken;

}
