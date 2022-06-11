package com.student.management.dto;

import lombok.Data;

@Data
public class MessageDto {
	
	private String status;
	private String statusMessage;
	private String messageDesc;
	private int successCount;
    private Object childObj;
}
