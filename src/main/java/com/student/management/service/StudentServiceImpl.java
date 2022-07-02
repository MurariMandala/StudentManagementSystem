package com.student.management.service;


import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.opencsv.CSVWriter;
import com.student.management.dto.AssignBooksDto;
import com.student.management.dto.BookDto;
import com.student.management.dto.MessageDto;
import com.student.management.dto.StudentDto;
import com.student.management.entity.Books;
import com.student.management.entity.HasBook;
import com.student.management.entity.Student;
import com.student.management.repository.BookRepository;
import com.student.management.repository.HasBookRepository;
import com.student.management.repository.StudentRepository;
import com.student.management.util.ConstantUtil;
import com.student.management.util.EntityUtil;

@Service
public class StudentServiceImpl implements StudentService{
	private ModelMapper modelMapper = new ModelMapper();
	
	@Autowired
	StudentRepository studentRepo;
	
	@Autowired
	HasBookRepository hasBookRepo;
	
	@Autowired
	BookRepository bookRepo;
	
	@Value("${college.code}")
	String collegeCode;
	
	@Value("${app.admin}")
	String appAdmin;
	
	public MessageDto createStudentInfo(StudentDto studentDto) {
		MessageDto messageDto = new MessageDto();
		Student student =  studentRepo.findByNameAndStatus(studentDto.getName(), ConstantUtil.STATUS_ACTIVE);
		if(!ObjectUtils.isEmpty(student)) {
			//duplicate
			EntityUtil.setMessageDto(messageDto, ConstantUtil.STATUS_FAILURE, ConstantUtil.STUDENT_ALREADY_EXIST);
			return messageDto;
		}
		Student newStudent = new Student();
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		String year = String.valueOf(currentYear).substring(2, 4);
		Random random = new Random();
		
		String studentId = year+collegeCode+random.nextInt(10000);
		modelMapper.map(studentDto, newStudent);
		newStudent.setStudentId(studentId);
		EntityUtil.setEntityDetails(newStudent, studentDto.getName(), appAdmin);
		studentRepo.save(newStudent);
		return messageDto;
	}
	
	public MessageDto assignBooksToStudent(AssignBooksDto assignBookDto) {
		MessageDto messageDto = new MessageDto();
		
		List<BookDto> bookDtos =  assignBookDto.getBookDtos();
		List<String> bookIds =bookDtos.stream().map(BookDto :: getId).collect(Collectors.toList());
		
		List<HasBook> hasBooks = hasBookRepo.findByBooksIdInAndStudentIdAndStatus(bookIds, assignBookDto.getStudentId(), ConstantUtil.STATUS_ACTIVE);
		if(!ObjectUtils.isEmpty(hasBooks)) {
			List<Books> books =	hasBooks.stream().map(HasBook::getBooks).collect(Collectors.toList());
			List<BookDto>  bookDtosList = bookDtos.stream().filter(bookDto->books.stream().anyMatch(book->book.getId().equals(bookDto.getId()))).collect(Collectors.toList());
			List<BookDto>  bookDtosList1 =books.stream().filter(book->bookDtos.stream().anyMatch(dto->dto.getId().equals(book.getId()))).map(map->{
				BookDto bookDto = modelMapper.map(map, BookDto.class);
				return bookDto;
			}).collect(Collectors.toList());
			EntityUtil.setMessageDto(messageDto, ConstantUtil.PARTIAL_SUCCESS, ConstantUtil.BOOK_ALREADY_ASSIGNED);
			messageDto.setChildObj(bookDtosList1);
			bookDtos.removeAll(bookDtosList);
		}
		
		if(!ObjectUtils.isEmpty(bookDtos)) {
			bookDtos.forEach(dto->{
				HasBook bookTobeAssign = new HasBook();
				Optional<Books> books = bookRepo.findById(dto.getId());
				if(books.isPresent()) {
					bookTobeAssign.setBooks(books.get());
				}
				
				Optional<Student> students = studentRepo.findById(assignBookDto.getStudentId());
				if(students.isPresent()) {
					bookTobeAssign.setStudent(students.get());
				}
				EntityUtil.setEntityDetails(bookTobeAssign, bookTobeAssign.getStudent().getStudentId(), appAdmin);
				hasBookRepo.save(bookTobeAssign);
			});
			messageDto.setSuccessCount(bookDtos.size());
		}else {
			messageDto.setStatus(ConstantUtil.STATUS_FAILURE);
		}
		
		return messageDto;
	}

	public StudentDto getStudentInfo(String studentID) {
		StudentDto studentResponseDto = new StudentDto();
		Student studentInfo = studentRepo.findByStudentIdAndStatus(studentID, ConstantUtil.STATUS_ACTIVE);
		List<HasBook> hasBooks = hasBookRepo.findByStudent(studentInfo);
		List<Books> books = hasBooks.stream().map(HasBook :: getBooks).collect(Collectors.toList());
		List<BookDto> bookDtos = new ArrayList<>();
		books.forEach(book->{
			BookDto bookDto = new BookDto();
			modelMapper.map(book, bookDto);
			bookDtos.add(bookDto);
		});
		modelMapper.map(studentInfo, studentResponseDto);
		studentResponseDto.setHasBooks(bookDtos);
		return studentResponseDto;
	}
//	@Override
//	public void generateCsvFile(HttpServletResponse response,
//            HttpServletRequest request) throws IOException {
//		
//		List<Student> students = studentRepo.findAll();
//		try {
//		response.setContentType("application/zip");
//         response.setHeader("Content-Disposition", "attachment; filename=Test Report " + new Date().toString()  + ".zip");
//         String[] stringValue = {"ID","Name","EMail"};
//         
//         PrintWriter writer1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream("stringValue1.csv"), "UTF-8"));
//         CSVWriter csvWriter1 = new CSVWriter(writer1);
//         
//         csvWriter1.writeNext(stringValue);
//         csvWriter1.writeNext(new String[0]);
//         PrintWriter writer2 = new PrintWriter(new OutputStreamWriter(new FileOutputStream("stringValue2.csv"), "UTF-8"));
//         CSVWriter csvWriter2 = new CSVWriter(writer2);
//         csvWriter2.writeNext(stringValue);
//         students.forEach(stu->{
//        	 String[] s = (stu.getId()+","+stu.getName()+","+stu.getEmailAddress()).split(",");
//        	 csvWriter1.writeNext(s);
//        	 csvWriter2.writeNext(s);
//
//         });
//        
//         writer1.close();
//         writer2.close();
//
//
//         File file1 = new File("stringValue1.csv");
//         File file2 = new File("stringValue2.csv");
//         filesToZip(response, file1, file2);     
//         file1.delete();
//         file2.delete();
//
//         response.flushBuffer();
//
//     } catch (Exception e) {
//         e.printStackTrace();
//     }
//	}
	
	public void generateCsvFile(HttpServletResponse response,HttpServletRequest request) throws IOException {
		try {
			response.setContentType("application/zip");
			response.setHeader("Content-Disposition", "attachment; filename=TestReport.zip");
			OutputStream servletOutputStream = response.getOutputStream(); // retrieve OutputStream from
																			// HttpServletResponse
			ZipOutputStream zos = new ZipOutputStream(servletOutputStream); // create a ZipOutputStream from
																			// servletOutputStream
			List<Student> students = studentRepo.findAll();
			System.out.println("executed...");
			int count = 0;
			for (int i = 0; i < 2; i++) {
				String filename = "file-" + ++count + ".csv";
				ZipEntry entry = new ZipEntry(filename);
				zos.putNextEntry(entry);
				CSVWriter writer = new CSVWriter(new OutputStreamWriter(zos));
				for (Student stu : students) {
					String[] s = (stu.getId() + "," + stu.getName() + "," + stu.getEmailAddress()).split(",");
					writer.writeNext(s);
					writer.flush();
				}
				zos.closeEntry();

			}

			zos.close();
		} catch (Exception e) {
		  
		}
	}    
//    public static void filesToZip(HttpServletResponse response, File... files) throws IOException {
//        // Create a buffer for reading the files
//        byte[] buf = new byte[1024];
//        // create the ZIP file
//        ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
//        // compress the files
//        for(int i=0; i<files.length; i++) {
//            FileInputStream in = new FileInputStream(files[i].getName());
//            // add ZIP entry to output stream
//            out.putNextEntry(new ZipEntry(files[i].getName()));
//            // transfer bytes from the file to the ZIP file
//            int len;
//            while((len = in.read(buf)) > 0) {
//                out.write(buf, 0, len);
//            }
//            // complete the entry
//            out.closeEntry();
//            in.close();
//        }
//        // complete the ZIP file
//        out.close();
//    } 3462834

}
