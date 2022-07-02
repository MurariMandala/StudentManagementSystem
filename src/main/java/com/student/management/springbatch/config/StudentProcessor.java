package com.student.management.springbatch.config;

import java.util.Calendar;
import java.util.Random;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import com.student.management.dto.StudentDto;
import com.student.management.entity.Student;
import com.student.management.util.ConstantUtil;
import com.student.management.util.EntityUtil;

public class StudentProcessor implements ItemProcessor<StudentDto, Student> {

	
	@Value("${college.code}")
	String collegeCode;

	@Value("${app.admin}")
	String appAdmin;

	@Override
	public Student process(StudentDto item) throws Exception {
		String studentName = item.getFirstName() + " " + item.getLastName();
		
			Student student = new Student();
			student.setName(studentName);
			student.setEmailAddress(item.getEmailAddress());

			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			String year = String.valueOf(currentYear).substring(2, 4);
			Random random = new Random();
			String studentId = year + collegeCode + random.nextInt(10000);
			student.setStudentId(studentId);
			EntityUtil.setEntityDetails(student, student.getName(), appAdmin);
			if (item.getStatus().equals("1")) {
				student.setStatus(ConstantUtil.STATUS_ACTIVE);
			} else {
				student.setStatus(ConstantUtil.STATUS_INACTIVE);
			}

			return student;
	
	}

}
