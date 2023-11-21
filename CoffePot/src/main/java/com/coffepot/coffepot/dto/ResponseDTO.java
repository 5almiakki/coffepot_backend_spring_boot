package com.coffepot.coffepot.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// HTTP 응답으로 사용할 DTO
// 다른 모델의 DTO도 사용할 수 있도록 제네릭 사용
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDTO<T> {
	
	private String error;
	private List<T> data;

}
