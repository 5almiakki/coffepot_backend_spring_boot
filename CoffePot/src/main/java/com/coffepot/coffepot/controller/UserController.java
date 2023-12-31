package com.coffepot.coffepot.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffepot.coffepot.dto.ResponseDTO;
import com.coffepot.coffepot.dto.UserDTO;
import com.coffepot.coffepot.model.UserEntity;
import com.coffepot.coffepot.security.TokenProvider;
import com.coffepot.coffepot.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	private final TokenProvider tokenProvider;
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
		try {
			if (userDTO == null || userDTO.getPassword() == null) {
				throw new RuntimeException("Invalid Password value.");
			}
			// 요청을 이용해 유저 만들기
			UserEntity userEntity = UserEntity.builder()
					.username(userDTO.getUsername())
					.password(passwordEncoder.encode(userDTO.getPassword()))
					.build();
			// 서비스를 이용해 유저 리포지토리에 저장
			UserEntity registeredUser = userService.create(userEntity);
			UserDTO responseUserDTO = UserDTO.builder()
					.id(registeredUser.getId())
					.username(registeredUser.getUsername())
					.build();
			
			return ResponseEntity.ok().body(responseUserDTO);
		} catch (Exception e) {
			// 유저 정보는 항상 하나이므로 리스트로 만들어야 하는 ResponseDTO 안 씀
			ResponseDTO responseDTO = ResponseDTO.builder()
					.error(e.getMessage())
					.build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
		UserEntity userEntity = userService.getByCredentials(
				userDTO.getUsername(),
				userDTO.getPassword(),
				passwordEncoder);
		
		// signin 시에는 access, refresh 둘 다 발급
		if (userEntity != null) {
			final String accessToken = tokenProvider.createAccessToken(userEntity);
			final String refreshToken = tokenProvider.createRefreshToken(userEntity);
			final UserDTO responseUserDTO = UserDTO.builder()
					.username(userEntity.getUsername())
					.id(userEntity.getId())
					.accessToken(accessToken)
					.refreshToken(refreshToken)
					.build();
			return ResponseEntity.ok().body(responseUserDTO);
		} else {
			ResponseDTO responseDTO = ResponseDTO.builder()
					.error("Login Failed")
					.build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@PatchMapping("/reissue")
	public ResponseEntity<?> reissueAccessToken(@RequestBody UserDTO userDTO, HttpServletRequest request) {
		String refreshToken = request.getHeader("Authorization");
		
		if (StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")) {
			refreshToken = refreshToken.substring(7);
		}
		
		UserEntity userEntity = userService.getByCredentials(
				userDTO.getUsername(),
				userDTO.getPassword(),
				passwordEncoder);
		
		String accessToken = tokenProvider.validateAndReissueAccessToken(refreshToken, userEntity);
		// null을 반환받았다면 if 문 실행
		if (accessToken == null) {
			ResponseDTO responseDTO = ResponseDTO.builder()
					.error("Reissue Failed")
					.build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
		
		UserDTO responseUserDTO = UserDTO.builder()
				.username(userEntity.getUsername())
				.id(userEntity.getId())
				.accessToken(accessToken)
				.build();
		return ResponseEntity.ok().body(responseUserDTO);
	}
	
	@PostMapping("/find-username")
	public ResponseEntity<?> findUsername(@RequestBody UserDTO userDTO) {
		List<String> usernameInfo = new ArrayList<String>();
		usernameInfo.add(userDTO.getEmail());
		// TODO
		// 이외 다른 가입 정보 정해지면 추가로 add 하기
		
		try {
			UserEntity userEntity = userService.getByUsernameInfo(usernameInfo);
			final UserDTO responseUserDTO = UserDTO.builder()
					.username(userEntity.getUsername())
					.build();
			return ResponseEntity.ok().body(responseUserDTO);
		} catch (Exception e) {
			ResponseDTO responseDTO = ResponseDTO.builder()
					.error("No Such Username")
					.build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@PostMapping("/find-password")
	public ResponseEntity<?> findPassword(@RequestBody UserDTO userDTO) {
		List<String> passwordInfo = new ArrayList<String>();
		passwordInfo.add(userDTO.getEmail());
		// TODO
		// 이외 다른 가입 정보 정해지면 추가로 add 하기
		
		// 비밀번호를 알려줄 수 없으므로 재설정하도록 해야 함
		try {
			UserEntity userEntity = userService.getByPasswordInfo(passwordInfo);
			final UserDTO responseUserDTO = UserDTO.builder()
					.username(userEntity.getUsername())
					.build();
			return ResponseEntity.ok().body(responseUserDTO);
		} catch (Exception e) {
			ResponseDTO responseDTO = ResponseDTO.builder()
					.error("No Password Matching")
					.build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody UserDTO userDTO) {
		try {
			UserEntity userEntity = UserEntity.builder()
					.username(userDTO.getUsername())
					.password(passwordEncoder.encode(userDTO.getPassword()))
					.build();
			UserEntity passwordChangedUser = userService.updatePassword(userEntity);

			UserDTO responseUserDTO = UserDTO.builder()
					.id(passwordChangedUser.getId())
					.username(passwordChangedUser.getUsername())
					.build();
			
			return ResponseEntity.ok().body(responseUserDTO);
		} catch (Exception e) {
			ResponseDTO responseDTO = ResponseDTO.builder()
					.error(e.getMessage())
					.build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}

}
