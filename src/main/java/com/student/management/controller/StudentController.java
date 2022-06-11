package com.student.management.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.management.dto.AssignBooksDto;
import com.student.management.dto.MessageDto;
import com.student.management.dto.StudentDto;
import com.student.management.service.StudentService;

@RestController
@RequestMapping(value = "/api/v1/student")
public class StudentController {
	
	@Autowired
	StudentService studentService;
	
	@PostMapping
	public ResponseEntity<MessageDto> createStudentInfo(@RequestBody StudentDto studentDto){
		return new ResponseEntity<>(studentService.createStudentInfo(studentDto), HttpStatus.OK);
	}
 
	@PostMapping(value = "/books")
	public ResponseEntity<MessageDto> assignBooksToStudent(@RequestBody AssignBooksDto assignBooksDto){
		return new ResponseEntity<>(studentService.assignBooksToStudent(assignBooksDto), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{studentID}")
	public ResponseEntity<StudentDto> getStudentInfo(@PathVariable String studentID){
		return new ResponseEntity<>(studentService.getStudentInfo(studentID), HttpStatus.OK);
		
	}
	
	@PostMapping("/csv")
	public void generateCsvFile(HttpServletResponse response, HttpServletRequest request) throws IOException {
		studentService.generateCsvFile(response, request);
	}
}
