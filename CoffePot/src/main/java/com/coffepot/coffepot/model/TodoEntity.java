package com.coffepot.coffepot.model;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity // 엔티티에 이름을 부여하려면 Entity("name")처럼 쓰면 됨
@Table(name = "Todo") // 테이블 이름 지정
public class TodoEntity {
	
	@Id
	// ID를 자동으로 생성. generator: ID 생성 방법 지정
	// 아래 name이 system-uuid인 generator를 사용하겠다는 의미
	/*
	 * GeneratedValue, GenericGenerator를 가지고 uuid를 사용하려니까 에러 뜸.
	 * deprecated 된 strategy를 썼으면 에러 안 떴을 거 같은데 deprecated 안 된 type을 쓰니 에러 뜨는 듯
	 * stackoverflow에서는 이 2개 쓰지 말고 @UuidGenerator를 쓰라고 한다...
	 */
//	@GeneratedValue(generator = "system-uuid")
	// Hibernate가 제공하는 기본 generator가 아닌 나만의 Generator를 사용하려 할 때 GenericGenerator 사용
	// name은 system-uuid로 지정하고 타입은 type UuidGenerator로 지정함
//	@GenericGenerator(name = "system-uuid", type = org.hibernate.id.uuid.UuidGenerator.class)
	@UuidGenerator
	private String id; // 이 오브젝트의 아이디
	
	private String userId; // 이 오브젝트를 생성한 유저의 아이디
	private String title; // Todo 제목
	private boolean done; // todo 완료 여부

}
