package com.student.management.repository;

import java.awt.print.Book;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.management.entity.Books;

public interface BookRepository extends JpaRepository<Books, String>{

	Books findByNameAndAuthor(String name, String author);

}
