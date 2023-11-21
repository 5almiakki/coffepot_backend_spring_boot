package com.coffepot.coffepot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
	
	private String accessToken;
	private String refreshToken;

}
