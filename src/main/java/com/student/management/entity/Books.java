package com.student.management.entity;

import javax.persistence.Entity;

import lombok.Data;

@Entity
@Data
public class Books extends ObjectUtil{
	private String author;
	private double price;
}
