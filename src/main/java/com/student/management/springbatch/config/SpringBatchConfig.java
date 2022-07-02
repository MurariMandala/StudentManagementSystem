package com.student.management.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.student.management.dto.StudentDto;
import com.student.management.entity.Student;
import com.student.management.repository.StudentRepository;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private StudentRepository studentRepo;
	
	@Bean
	public FlatFileItemReader<StudentDto> reader(){
		FlatFileItemReader<StudentDto> itemReader = new FlatFileItemReader<>();
		itemReader.setResource(new FileSystemResource("src/main/resources/studentdata.csv"));
		itemReader.setName("studentReader");
		itemReader.setLinesToSkip(1);
		itemReader.setLineMapper(lineMapper());
		
		return itemReader;
	}

	private LineMapper<StudentDto> lineMapper() {
		DefaultLineMapper<StudentDto> lineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("firstName","lastName","emailAddress","status");
		
		BeanWrapperFieldSetMapper<StudentDto> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(StudentDto.class);
		
		lineMapper.setFieldSetMapper(fieldSetMapper);
		lineMapper.setLineTokenizer(lineTokenizer);
		
		return lineMapper;
	}
	
	@Bean
	public StudentProcessor processor() {
		return new StudentProcessor();
	}
	
	@Bean
	public RepositoryItemWriter<Student> itemWriter(){
		RepositoryItemWriter<Student> itemWriter = new RepositoryItemWriter<>();
		itemWriter.setRepository(studentRepo);
		itemWriter.setMethodName("save");
		return itemWriter;
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("import student").<StudentDto,Student>chunk(100)
				.reader(reader())
				.processor(processor())
				.writer(itemWriter())
				.build();
		
	}
	
	@Bean
	public Job runjob() {
		return jobBuilderFactory.get("run student job")
				.flow(step1()).end().build();
	}
}
