package com.student.management.dto;

import lombok.Data;

@Data
public class BookDto {
	
	private String id;
	private String name;
	private String author;	
	private double price;	
	private String createdBy;
	
}
