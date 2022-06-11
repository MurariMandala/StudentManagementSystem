package com.student.management.service;

import java.util.List;

import com.student.management.dto.BookDto;
import com.student.management.dto.MessageDto;

public interface BookService {

	MessageDto createBook(BookDto bookDto);
	List<BookDto> getAllBooks();
}
