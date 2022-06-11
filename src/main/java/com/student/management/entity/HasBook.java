package com.student.management.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "HAS_BOOK")
@Data
public class HasBook extends ObjectUtil{
	
	@JoinColumn(name = "student", referencedColumnName = "ID")
	@ManyToOne
	private Student student;
	@JoinColumn(name = "books", referencedColumnName = "ID")
	@ManyToOne
	private Books books;
  
}
