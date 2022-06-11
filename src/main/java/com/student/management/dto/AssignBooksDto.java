package com.student.management.dto;

import java.util.List;

import lombok.Data;

@Data
public class AssignBooksDto {
	private String studentId;
	private List<BookDto> bookDtos;
}
