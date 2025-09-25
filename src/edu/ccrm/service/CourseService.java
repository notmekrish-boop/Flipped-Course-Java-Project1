package edu.ccrm.service;

import edu.ccrm.domain.*;
import java.util.*;
import java.util.stream.Collectors;

public class CourseService implements Searchable<Course> {
    private final Map<String, Course> courses;
    
    public CourseService() { this.courses = new HashMap<>(); }
    
    public void addCourse(Course course) {
        assert course != null : "Course cannot be null";
        if (courses.containsKey(course.getCode())) {
            throw new IllegalArgumentException("Course with code " + course.getCode() + " already exists");
        }
        courses.put(course.getCode(), course);
    }
    
    public Optional<Course> getCourse(String code) {
        return Optional.ofNullable(courses.get(code));
    }
    
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }
    
    public List<Course> getActiveCourses() {
        return courses.values().stream()
            .filter(Course::isActive)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> search(java.util.function.Predicate<Course> predicate) {
        return courses.values().stream().filter(predicate).collect(Collectors.toList());
    }
    
    public List<Course> getCoursesByInstructor(String instructorId) {
        return courses.values().stream()
            .filter(course -> instructorId.equals(course.getInstructorId()))
            .collect(Collectors.toList());
    }
    
    public List<Course> getCoursesByDepartment(String department) {
        return courses.values().stream()
            .filter(course -> department.equalsIgnoreCase(course.getDepartment()))
            .collect(Collectors.toList());
    }
    
    public List<Course> getCoursesBySemester(Semester semester) {
        return courses.values().stream()
            .filter(course -> semester == course.getSemester())
            .collect(Collectors.toList());
    }
    
    public Map<String, List<Course>> getCoursesGroupedByDepartment() {
        return courses.values().stream()
            .collect(Collectors.groupingBy(Course::getDepartment));
    }
}