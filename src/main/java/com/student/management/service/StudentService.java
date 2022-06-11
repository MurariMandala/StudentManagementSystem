package com.student.management.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.student.management.dto.AssignBooksDto;
import com.student.management.dto.MessageDto;
import com.student.management.dto.StudentDto;

public interface StudentService {

	MessageDto createStudentInfo(StudentDto studentDto);

	MessageDto assignBooksToStudent(AssignBooksDto assignBooksDto);

	StudentDto getStudentInfo(String studentID);

	void generateCsvFile(HttpServletResponse response, HttpServletRequest request) throws IOException;

}
