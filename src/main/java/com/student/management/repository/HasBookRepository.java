package com.student.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.management.entity.HasBook;
import com.student.management.entity.Student;

public interface HasBookRepository extends JpaRepository<HasBook, String>{

	List<HasBook> findByBooksIdInAndStudentIdAndStatus(List<String> bookIds, String studentId, String statusActive);

	List<HasBook> findByStudent(Student studentInfo);

}
