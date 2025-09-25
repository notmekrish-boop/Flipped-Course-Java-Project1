package edu.ccrm.service;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import java.util.*;
import java.util.stream.Collectors;

public class StudentService implements Searchable<Student> {
    private final Map<String, Student> students;
    private final CourseService courseService;
    
    public StudentService(CourseService courseService) {
        this.students = new HashMap<>();
        this.courseService = courseService;
    }
    
    public void addStudent(Student student) {
        assert student != null : "Student cannot be null";
        if (students.containsKey(student.getId())) {
            throw new IllegalArgumentException("Student with ID " + student.getId() + " already exists");
        }
        students.put(student.getId(), student);
    }
    
    public Optional<Student> getStudent(String id) {
        return Optional.ofNullable(students.get(id));
    }
    
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }
    
    public List<Student> getActiveStudents() {
        return students.values().stream()
            .filter(Student::isActive)
            .collect(Collectors.toList());
    }
    
    public void enrollStudentInCourse(String studentId, String courseId) {
        Student student = students.get(studentId);
        if (student == null) throw new IllegalArgumentException("Student not found: " + studentId);
        
        Course course = courseService.getCourse(courseId)
            .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));
        
        if (student.getEnrolledCourses().contains(courseId)) {
            throw new DuplicateEnrollmentException(studentId, courseId);
        }
        
        int currentCredits = getCurrentSemesterCredits(studentId);
        if (currentCredits + course.getCredits() > AppConfig.getInstance().getMaxCreditsPerSemester()) {
            throw new MaxCreditLimitExceededException(
                currentCredits, course.getCredits(), AppConfig.getInstance().getMaxCreditsPerSemester());
        }
        
        student.enrollInCourse(courseId);
    }
    
    private int getCurrentSemesterCredits(String studentId) {
        Student student = students.get(studentId);
        return student.getEnrolledCourses().stream()
            .map(courseService::getCourse)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .mapToInt(Course::getCredits)
            .sum();
    }
    
    public void recordGrade(String studentId, String courseId, double score) {
        Student student = students.get(studentId);
        if (student == null) throw new IllegalArgumentException("Student not found: " + studentId);
        student.recordGrade(courseId, score);
    }
    
    @Override
    public List<Student> search(java.util.function.Predicate<Student> predicate) {
        return students.values().stream().filter(predicate).collect(Collectors.toList());
    }
    
    public List<Student> getStudentsSortedByName() {
        return students.values().stream()
            .sorted((s1, s2) -> s1.getFullName().compareToIgnoreCase(s2.getFullName()))
            .collect(Collectors.toList());
    }
    
    public List<Student> getStudentsSortedByGPA() {
        return students.values().stream()
            .sorted(Comparator.comparingDouble(Student::calculateGPA).reversed())
            .collect(Collectors.toList());
    }
}
