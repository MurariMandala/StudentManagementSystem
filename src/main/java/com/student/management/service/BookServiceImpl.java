package com.student.management.service;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.student.management.dto.BookDto;
import com.student.management.dto.MessageDto;
import com.student.management.entity.Books;
import com.student.management.repository.BookRepository;
import com.student.management.util.ConstantUtil;
import com.student.management.util.EntityUtil;

@Service
public class BookServiceImpl implements BookService{

	private ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private BookRepository bookRepo;

	public MessageDto createBook(BookDto bookDto) {
		MessageDto messageDto = new MessageDto();

		Books isBookExist = bookRepo.findByNameAndAuthor(bookDto.getName(), bookDto.getAuthor());
		if (!ObjectUtils.isEmpty(isBookExist)) {
			EntityUtil.setMessageDto(messageDto, ConstantUtil.STATUS_FAILURE, ConstantUtil.STUDENT_ALREADY_EXIST);
		}
		Books book = modelMapper.map(bookDto, Books.class);
		EntityUtil.setEntityDetails(book, bookDto.getName(), bookDto.getCreatedBy());
		bookRepo.save(book);
		if (ObjectUtils.isEmpty(messageDto.getStatus())) {
			messageDto.setStatus(ConstantUtil.STATUS_SUCCESS);
			messageDto.setStatusMessage(ConstantUtil.SUCCESS_MSG);
		}

		return messageDto;

	}

	public List<BookDto> getAllBooks() {
		return bookRepo.findAll().stream().map(map -> {
			BookDto bookDto = modelMapper.map(map, BookDto.class);
			return bookDto;
		}).collect(Collectors.toList());
	}
}
