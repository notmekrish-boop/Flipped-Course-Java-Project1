package edu.ccrm.domain;
public class DuplicateEnrollmentException extends RuntimeException {
    public DuplicateEnrollmentException(String studentId, String courseId) {
        super(String.format("Student %s is already enrolled in course %s", studentId, courseId));
    }
}
