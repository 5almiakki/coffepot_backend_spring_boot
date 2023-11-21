package com.coffepot.coffepot.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coffepot.coffepot.model.TodoEntity;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {

	// @Query를 이용해서 복잡한 쿼리를 작성할 수 있다.
	// 예) @Query("select * from TodoEntity t where t.userId = ?1")
	// ?1는 첫 번째 파라미터를 가리킴
	List<TodoEntity> findByUserId(String userId);

}
