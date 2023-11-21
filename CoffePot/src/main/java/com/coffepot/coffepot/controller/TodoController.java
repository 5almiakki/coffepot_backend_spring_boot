package com.coffepot.coffepot.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coffepot.coffepot.dto.ResponseDTO;
import com.coffepot.coffepot.dto.TodoDTO;
import com.coffepot.coffepot.model.TodoEntity;
import com.coffepot.coffepot.service.TodoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("todo")
@RequiredArgsConstructor
public class TodoController {
	
	private final TodoService todoService;
	
	@PostMapping
	public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId,
			@RequestBody TodoDTO dto) {
		try {
//			String temporaryUserId = "temporary-user"; // temporary user id
			
			// 1. TodoEntity로 변환
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			// 2. id를 null로 초기화한다. 생성 당시에는 id가 없어야 하기 때문
			entity.setId(null);
			
//			// 3. 임시 유저 아이디를 설정한다. 당장은 인증, 인가 기능 없으므로
//			// temporary user가 로그인 없이 사용 가능한 어플리케이션이다.
//			entity.setUserId(temporaryUserId);
			
			// 3. Authentication Bearer Token을 통해 받은 userId를 넘긴다.
			entity.setUserId(userId);
			
			// 4. 서비스를 이용해 Todo 엔티티를 생성
			List<TodoEntity> entities = todoService.create(entity);
			
			// 5. 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			// 6. 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			// 7. ResponseDTO를 리턴
			return ResponseEntity.ok().body(response);
			
		} catch (Exception e) {
			// 예외 발생 시 dto 대신 error에 메시지를 넣어 리턴한다.
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
//	@GetMapping("/test")
//	public ResponseEntity<?> testTodo() {
//		String str = todoService.testService();
//		List<String> list = new ArrayList<>();
//		list.add(str);
//		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
//		return ResponseEntity.ok().body(response);
//	}
	
	@GetMapping
	public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId) {
//		String temporaryUserId = "temporary-user";
		
		// 1. 서비스 메서드의 retrieve 메서드로 Todo 리스트 가져옴
//		List<TodoEntity> entities = todoService.retrieve(temporaryUserId);
		List<TodoEntity> entities = todoService.retrieve(userId);
		
		// 2. 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		// 3. 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		// 4. ResponseDTO를 리턴한다.
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping
	public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
//		String temporaryUserId = "temporary-user";
		
		// 1. dto를 entity로 변환한다.
		TodoEntity entity = TodoDTO.toEntity(dto);
		
		// 2. user id를 temporaryUserId로 초기화한다.
		entity.setUserId(userId);
		
		// 3. 서비스를 이용해 entity를 업데이트한다.
		List<TodoEntity> entities = todoService.update(entity);
		
		// 4. 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		// 5. 변환된 TodoDTO 리스트를 이용해 ResponseDTO 초기화
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		return ResponseEntity.ok().body(response);
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
		try {
//			String temporaryUserId = "temporary-user";
			
			TodoEntity entity = TodoDTO.toEntity(dto);
//			entity.setUserId(temporaryUserId);
			entity.setUserId(userId);
			// 서비스를 이용해 엔티티 삭제
			List<TodoEntity> entities = todoService.delete(entity);
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			// 예외가 발생하면 ResponseDTO의 error에 메시지를 넣어 리턴한다.
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			
			return ResponseEntity.badRequest().body(response);
		}
	}

}
