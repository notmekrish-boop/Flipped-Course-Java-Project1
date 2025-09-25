package edu.ccrm.domain;

import java.util.*;
import java.time.LocalDateTime;

public class Student extends Person {
    private final String regNo;
    private final Set<String> enrolledCourses;
    private final Map<String, Double> courseGrades;
    private final Map<String, Grade> courseLetterGrades;
    
    public static class Builder {
        private String id;
        private String regNo;
        private String fullName;
        private String email;
        
        public Builder id(String id) { this.id = id; return this; }
        public Builder regNo(String regNo) { this.regNo = regNo; return this; }
        public Builder fullName(String fullName) { this.fullName = fullName; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Student build() { return new Student(this); }
    }
    
    private Student(Builder builder) {
        super(builder.id, builder.fullName, builder.email);
        this.regNo = Objects.requireNonNull(builder.regNo, "Registration number cannot be null");
        this.enrolledCourses = new HashSet<>();
        this.courseGrades = new HashMap<>();
        this.courseLetterGrades = new HashMap<>();
    }
    
    @Override
    public String getRole() { return "Student"; }
    public String getRegNo() { return regNo; }
    
    public boolean enrollInCourse(String courseId) { return enrolledCourses.add(courseId); }
    public boolean unenrollFromCourse(String courseId) {
        courseGrades.remove(courseId);
        courseLetterGrades.remove(courseId);
        return enrolledCourses.remove(courseId);
    }
    public Set<String> getEnrolledCourses() { return Collections.unmodifiableSet(enrolledCourses); }
    
    public void recordGrade(String courseId, double score) {
        if (!enrolledCourses.contains(courseId)) {
            throw new IllegalArgumentException("Student not enrolled in course: " + courseId);
        }
        courseGrades.put(courseId, score);
        courseLetterGrades.put(courseId, Grade.fromScore(score));
    }
    
    public Double getGrade(String courseId) { return courseGrades.get(courseId); }
    public Grade getLetterGrade(String courseId) { return courseLetterGrades.get(courseId); }
    
    public double calculateGPA() {
        if (courseGrades.isEmpty()) return 0.0;
        return courseLetterGrades.values().stream()
            .mapToInt(Grade::getPoints)
            .average()
            .orElse(0.0);
    }
    
    public Transcript generateTranscript() { return new Transcript(this); }
    
    public class Transcript {
        private final Student student;
        private final LocalDateTime generatedAt;
        
        public Transcript(Student student) {
            this.student = student;
            this.generatedAt = LocalDateTime.now();
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("OFFICIAL TRANSCRIPT\n");
            sb.append("===================\n");
            sb.append("Student: ").append(student.getFullName()).append("\n");
            sb.append("Reg No: ").append(student.getRegNo()).append("\n");
            sb.append("Generated: ").append(generatedAt).append("\n\n");
            sb.append("COURSES AND GRADES\n");
            sb.append("==================\n");
            
            student.courseGrades.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String courseId = entry.getKey();
                    double score = entry.getValue();
                    Grade grade = student.courseLetterGrades.get(courseId);
                    sb.append(String.format("Course: %s | Score: %.2f | Grade: %s (%s)%n", 
                        courseId, score, grade, grade.getDescription()));
                });
            
            sb.append(String.format("%nOverall GPA: %.2f", student.calculateGPA()));
            return sb.toString();
        }
    }
}