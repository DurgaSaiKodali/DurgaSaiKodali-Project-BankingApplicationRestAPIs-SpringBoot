package com.ds.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Entity
@Table(name = "JPA_LOB_MARRIAGE_SEEKER")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class MarriageSeeker implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer sid;
	@NonNull
	@Column(length = 30)
	private String name;
	@NonNull
	@Column(length = 50)
	private String addrs;
	@NonNull
	@Column(length = 10)
	private Boolean indian;
	@Lob
	@NonNull
	@Column(length = 1500)
	private byte[] photo;
	@Lob
	@NonNull
	@Column(length = 1500)
	private char[] resume;

}
