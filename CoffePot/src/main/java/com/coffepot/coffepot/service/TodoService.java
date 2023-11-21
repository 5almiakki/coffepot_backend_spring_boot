package com.coffepot.coffepot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.coffepot.coffepot.model.TodoEntity;
import com.coffepot.coffepot.persistence.TodoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class TodoService {
	
	private final TodoRepository todoRepository;
	
	public String testService() {
		// TodoEntity 생성
		TodoEntity todoEntity = TodoEntity.builder().title("My first todo item").build();
		// TodoEntity 저장
		todoRepository.save(todoEntity);
		// TodoEntity 검색
		TodoEntity savedEntity = todoRepository.findById(todoEntity.getId()).get();
		return savedEntity.getTitle();
	}
	
	private void validate(final TodoEntity entity) {
		// Validations
		if (entity == null) {
			log.warn("Entity cannot be null.");
			throw new RuntimeException("Entity cannot be null");
		}
		
		if (entity.getUserId() == null) {
			log.warn("Unknown user");
			throw new RuntimeException("Unknown user");
		}
	}
	
	public List<TodoEntity> create(final TodoEntity entity) {
		validate(entity);
		
		todoRepository.save(entity);
		log.info("Entity Id: {} is saved.", entity.getId());
		
		return todoRepository.findByUserId(entity.getUserId());
	}
	
	public List<TodoEntity> retrieve(final String userId) {
		log.info("Entity(ies) of user Id: {} is(are) retrieved", userId);
		return todoRepository.findByUserId(userId);
	}
	
	public List<TodoEntity> update(final TodoEntity entity) {
		validate(entity);
		
		final Optional<TodoEntity> original = todoRepository.findById(entity.getId());
		original.ifPresent(todo -> {
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			todoRepository.save(todo);
		});
		
		return retrieve(entity.getUserId());
	}
	
	public List<TodoEntity> delete(final TodoEntity entity) {
		validate(entity);
		
		try {
			todoRepository.delete(entity);
		} catch (Exception e) {
			log.error("error deleting entity ", entity.getId(), e);
			// 컨트롤러로 예외를 날린다. db 내부 로직을 캡슐화하기 위해 e 대신 exception 오브젝트를 날린다.
			throw new RuntimeException("error deleting entity" + entity.getId());
		}
		
		return retrieve(entity.getUserId());
	}

}
