package com.coffepot.coffepot.dto;

import com.coffepot.coffepot.model.TodoEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDTO {

	private String id;
	// userId는 없다. 스프링 시큐리티를 통해 인증을 구현할 수 있기 때문이다.
	// 보안 상 숨기는 게 맞다.
	private String title;
	private boolean done;

	public TodoDTO(final TodoEntity entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.done = entity.isDone();
	}

	public static TodoEntity toEntity(final TodoDTO dto) {
		return TodoEntity.builder().id(dto.getId()).title(dto.getTitle()).done(dto.isDone()).build();
	}

}
