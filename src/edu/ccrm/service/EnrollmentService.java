package edu.ccrm.service;

import edu.ccrm.domain.*;
import java.util.*;
import java.util.stream.Collectors;

public class EnrollmentService {
    private final StudentService studentService;
    private final CourseService courseService;
    
    public EnrollmentService(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }
    
    public Map<String, List<String>> getEnrollmentsByCourse() {
        Map<String, List<String>> enrollments = new HashMap<>();
        
        studentService.getAllStudents().forEach(student -> {
            student.getEnrolledCourses().forEach(courseId -> {
                // Use _ for unused parameter
                enrollments.computeIfAbsent(courseId, _ -> new ArrayList<>()).add(student.getId());
            });
        });
        
        return enrollments;
    }
    
    public List<String> getStudentsInCourse(String courseId) {
        return studentService.getAllStudents().stream()
            .filter(student -> student.getEnrolledCourses().contains(courseId))
            .map(Student::getId)
            .collect(Collectors.toList());
    }
    
    public Map<String, String> getCourseDetailsForEnrollments() {
        Map<String, String> courseDetails = new HashMap<>();
        Map<String, List<String>> enrollments = getEnrollmentsByCourse();
        
        enrollments.keySet().forEach(courseId -> {
            Optional<Course> course = courseService.getCourse(courseId);
            if (course.isPresent()) {
                Course c = course.get();
                String details = String.format("%s (%s credits) - %s", 
                    c.getTitle(), c.getCredits(), c.getDepartment());
                courseDetails.put(courseId, details);
            } else {
                courseDetails.put(courseId, "Unknown Course");
            }
        });
        
        return courseDetails;
    }
    
    public void printEnrollmentReport() {
        Map<String, List<String>> enrollments = getEnrollmentsByCourse();
        
        System.out.println("\n📊 ENROLLMENT REPORT");
        System.out.println("=".repeat(50));
        
        enrollments.entrySet().stream()
            .sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()))
            .forEach(entry -> {
                String courseId = entry.getKey();
                List<String> studentIds = entry.getValue();
                
                Optional<Course> course = courseService.getCourse(courseId);
                String courseInfo = course.map(c -> c.getTitle() + " - " + c.getDepartment())
                                         .orElse("Unknown Course");
                
                System.out.printf("📚 %s: %s (%d students)%n", 
                    courseId, courseInfo, studentIds.size());
            });
    }
}