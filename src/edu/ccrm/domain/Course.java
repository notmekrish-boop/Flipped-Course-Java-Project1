package edu.ccrm.domain;

import java.util.Objects;

public class Course {
    private final String code;
    private final String title;
    private final int credits;
    private String instructorId;
    private Semester semester;
    private String department;
    private boolean active;
    
    public static class Builder {
        private String code;
        private String title;
        private int credits = 3;
        private String instructorId;
        private Semester semester;
        private String department;
        
        public Builder code(String code) { this.code = code; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder credits(int credits) { 
            if (credits <= 0) throw new IllegalArgumentException("Credits must be positive");
            this.credits = credits; return this; 
        }
        public Builder instructorId(String instructorId) { this.instructorId = instructorId; return this; }
        public Builder semester(Semester semester) { this.semester = semester; return this; }
        public Builder department(String department) { this.department = department; return this; }
        public Course build() { return new Course(this); }
    }
    
    private Course(Builder builder) {
        this.code = Objects.requireNonNull(builder.code, "Course code cannot be null");
        this.title = Objects.requireNonNull(builder.title, "Course title cannot be null");
        this.credits = builder.credits;
        this.instructorId = builder.instructorId;
        this.semester = builder.semester;
        this.department = builder.department;
        this.active = true;
    }
    
    public String getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getInstructorId() { return instructorId; }
    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }
    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    @Override
    public String toString() {
        return String.format("Course{code='%s', title='%s', credits=%d, department='%s'}", code, title, credits, department);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return Objects.equals(code, course.code);
    }
    
    @Override
    public int hashCode() { return Objects.hash(code); }
}