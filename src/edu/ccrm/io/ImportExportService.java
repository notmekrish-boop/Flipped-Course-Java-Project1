package edu.ccrm.io;

import edu.ccrm.domain.*;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.CourseService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImportExportService {
    private final StudentService studentService;
    private final CourseService courseService;
    
    public ImportExportService(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }
    
    public void importStudentsFromCSV(Path filePath) throws IOException {
        assert Files.exists(filePath) : "File must exist: " + filePath;
        
        try (Stream<String> lines = Files.lines(filePath)) {
            lines.skip(1)
                .map(this::parseStudentFromCSV)
                .forEach(studentService::addStudent);
        }
    }
    
    public void importCoursesFromCSV(Path filePath) throws IOException {
        assert Files.exists(filePath) : "File must exist: " + filePath;
        
        try (Stream<String> lines = Files.lines(filePath)) {
            lines.skip(1)
                .map(this::parseCourseFromCSV)
                .forEach(courseService::addCourse);
        }
    }
    
    public void exportStudentsToCSV(Path filePath) throws IOException {
        List<String> lines = studentService.getAllStudents().stream()
            .map(this::convertStudentToCSV)
            .collect(Collectors.toList());
        
        lines.add(0, "id,regNo,fullName,email,active");
        Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    public void exportCoursesToCSV(Path filePath) throws IOException {
        List<String> lines = courseService.getAllCourses().stream()
            .map(this::convertCourseToCSV)
            .collect(Collectors.toList());
        
        lines.add(0, "code,title,credits,instructorId,semester,department,active");
        Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    private Student parseStudentFromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 5) throw new IllegalArgumentException("Invalid CSV line: " + csvLine);
        
        return new Student.Builder()
            .id(parts[0].trim())
            .regNo(parts[1].trim())
            .fullName(parts[2].trim())
            .email(parts[3].trim())
            .build();
    }
    
    private Course parseCourseFromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 7) throw new IllegalArgumentException("Invalid CSV line: " + csvLine);
        
        return new Course.Builder()
            .code(parts[0].trim())
            .title(parts[1].trim())
            .credits(Integer.parseInt(parts[2].trim()))
            .instructorId(parts[3].trim())
            .semester(Semester.valueOf(parts[4].trim().toUpperCase()))
            .department(parts[5].trim())
            .build();
    }
    
    private String convertStudentToCSV(Student student) {
        return String.join(",",
            student.getId(), student.getRegNo(), student.getFullName(),
            student.getEmail(), String.valueOf(student.isActive())
        );
    }
    
    private String convertCourseToCSV(Course course) {
        return String.join(",",
            course.getCode(), course.getTitle(), String.valueOf(course.getCredits()),
            course.getInstructorId() != null ? course.getInstructorId() : "",
            course.getSemester() != null ? course.getSemester().name() : "",
            course.getDepartment() != null ? course.getDepartment() : "",
            String.valueOf(course.isActive())
        );
    }
}
