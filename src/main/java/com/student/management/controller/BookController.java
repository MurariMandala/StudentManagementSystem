package com.student.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.management.dto.BookDto;
import com.student.management.dto.MessageDto;
import com.student.management.service.BookService;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	@PostMapping
	public ResponseEntity<MessageDto> createBook(@RequestBody BookDto bookDto){
		return new ResponseEntity<>(bookService.createBook(bookDto), HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<List<BookDto>> getAllBooks(){
		return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
		
	}

}
