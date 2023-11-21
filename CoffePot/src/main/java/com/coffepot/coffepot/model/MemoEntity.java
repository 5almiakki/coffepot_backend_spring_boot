package com.coffepot.coffepot.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Memo")
public class MemoEntity {
	
	@Id
	@UuidGenerator
	private String id;
	
	private Boolean visibility;
	private String title;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	@ManyToOne
	private UserEntity author;
	
	@ManyToOne
	private CafeEntity about;

}
