package com.student.management.dto;

import java.util.List;

import lombok.Data;

@Data
public class StudentDto {
	private String studentId;
	private String name;
	private String firstName;
	private String lastName;
	private Long phoneNo;
	private String emailAddress;
	private String createdBy;
	private String modifiedBy;
	private List<BookDto> hasBooks;
	private String status;
}
