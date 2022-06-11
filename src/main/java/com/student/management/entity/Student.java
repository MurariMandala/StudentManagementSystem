package com.student.management.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "STUDENT_INFO")
@Data
public class Student extends ObjectUtil{
	private String studentId;
	private Long phoneNo;
	private String emailAddress;
	
}
