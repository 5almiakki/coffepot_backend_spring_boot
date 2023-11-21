package com.coffepot.coffepot.model;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "Cafe")
public class CafeEntity {
	
	@Id
	@UuidGenerator
	private String id;
	
	private String name;
	private String scale;
	private Boolean isIndependent;
	private String seatCapacity;
	private String takeOutOnly;
	private String timeLimit;
	private String zoneFor;
	private String studyZone;
	private String mood;
	private String customerAgeGroup;
	private String parkingInfo;
	private Boolean internetAvailable;
	private String openingHours;
	private String menu;
	private String reviewKeywords;
	private String address;

}
