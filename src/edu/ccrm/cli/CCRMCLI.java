package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.EnrollmentService;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.io.BackupService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CCRMCLI {
    private final Scanner scanner;
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final ImportExportService importExportService;
    private final BackupService backupService;
    
    public CCRMCLI() {
        this.scanner = new Scanner(System.in);
        this.courseService = new CourseService();
        this.studentService = new StudentService(courseService);
        this.enrollmentService = new EnrollmentService(studentService, courseService);
        this.importExportService = new ImportExportService(studentService, courseService);
        this.backupService = new BackupService(importExportService);
        initializeDataDirectory();
        loadSampleData();
    }
    
    private void initializeDataDirectory() {
        try {
            Files.createDirectories(AppConfig.getInstance().getDataDirectory());
            Files.createDirectories(AppConfig.getInstance().getBackupDirectory());
        } catch (IOException e) {
            System.err.println("Error creating directories: " + e.getMessage());
        }
    }
    
    private void loadSampleData() {
        try {
            // Create sample students
            Student student1 = new Student.Builder()
                .id("S001").regNo("2023001").fullName("John Doe").email("john.doe@university.edu").build();
            Student student2 = new Student.Builder()
                .id("S002").regNo("2023002").fullName("Jane Smith").email("jane.smith@university.edu").build();
            
            studentService.addStudent(student1);
            studentService.addStudent(student2);
            
            // Create sample courses
            Course course1 = new Course.Builder()
                .code("CS101").title("Introduction to Programming").credits(3)
                .instructorId("I001").semester(Semester.SPRING).department("Computer Science").build();
            Course course2 = new Course.Builder()
                .code("MATH201").title("Calculus I").credits(4)
                .instructorId("I002").semester(Semester.FALL).department("Mathematics").build();
            
            courseService.addCourse(course1);
            courseService.addCourse(course2);
            
            // Sample enrollments and grades
            studentService.enrollStudentInCourse("S001", "CS101");
            studentService.recordGrade("S001", "CS101", 85.5);
            
        } catch (Exception e) {
            System.out.println("Note: Sample data not loaded: " + e.getMessage());
        }
    }
    
    public void start() {
        System.out.println("🚀 Starting Campus Course & Records Manager...");
        
        mainLoop: while (true) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1" -> manageStudents();
                case "2" -> manageCourses();
                case "3" -> manageEnrollments();
                case "4" -> manageGrades();
                case "5" -> importExportData();
                case "6" -> backupOperations();
                case "7" -> generateReports();
                case "8" -> { printJavaPlatformInfo(); break mainLoop; }
                case "0" -> { System.out.println("Exiting CCRM. Goodbye!"); return; }
                default -> System.out.println("❌ Invalid choice. Please try again.");
            }
        }
    }
    
    private void printMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🏫 CAMPUS COURSE & RECORDS MANAGER");
        System.out.println("=".repeat(50));
        System.out.println("1. 👨‍🎓 Manage Students");
        System.out.println("2. 📚 Manage Courses");
        System.out.println("3. 📋 Manage Enrollments");
        System.out.println("4. 📊 Manage Grades");
        System.out.println("5. 💾 Import/Export Data");
        System.out.println("6. 🗂️  Backup Operations");
        System.out.println("7. 📈 Generate Reports");
        System.out.println("8. ☕ Java Platform Info & Exit");
        System.out.println("0. ❌ Exit");
        System.out.print("Enter your choice: ");
    }
    
    private void manageStudents() {
        studentMenu: while (true) {
            System.out.println("\n" + "=".repeat(30));
            System.out.println("👨‍🎓 STUDENT MANAGEMENT");
            System.out.println("=".repeat(30));
            System.out.println("1. ➕ Add Student");
            System.out.println("2. 📋 List All Students");
            System.out.println("3. 🔍 Find Student by ID");
            System.out.println("4. ✏️  Update Student");
            System.out.println("5. 📜 Generate Transcript");
            System.out.println("6. ↩️  Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            switch (scanner.nextLine().trim()) {
                case "1" -> addStudent();
                case "2" -> listStudents();
                case "3" -> findStudent();
                case "4" -> updateStudent();
                case "5" -> generateTranscript();
                case "6" -> { break studentMenu; }
                default -> System.out.println("❌ Invalid choice.");
            }
        }
    }
    
    private void addStudent() {
        try {
            System.out.print("Enter Student ID: ");
            String id = scanner.nextLine().trim();
            System.out.print("Enter Registration Number: ");
            String regNo = scanner.nextLine().trim();
            System.out.print("Enter Full Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter Email: ");
            String email = scanner.nextLine().trim();
            
            Student student = new Student.Builder()
                .id(id).regNo(regNo).fullName(name).email(email).build();
            
            studentService.addStudent(student);
            System.out.println("✅ Student added successfully!");
            
        } catch (Exception e) {
            System.out.println("❌ Error adding student: " + e.getMessage());
        }
    }
    
    private void listStudents() {
        List<Student> students = studentService.getStudentsSortedByName();
        if (students.isEmpty()) {
            System.out.println("📭 No students found.");
            return;
        }
        
        System.out.println("\n" + "-".repeat(80));
        System.out.printf("%-8s %-12s %-20s %-25s %-6s%n", 
            "ID", "Reg No", "Name", "Email", "GPA");
        System.out.println("-".repeat(80));
        
        students.forEach(student -> {
            System.out.printf("%-8s %-12s %-20s %-25s %.2f%n",
                student.getId(), student.getRegNo(), 
                student.getFullName(), student.getEmail(),
                student.calculateGPA());
        });
    }
    
    private void findStudent() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine().trim();
        
        Optional<Student> student = studentService.getStudent(id);
        if (student.isPresent()) {
            Student s = student.get();
            System.out.println("\n📋 Student Details:");
            System.out.println("ID: " + s.getId());
            System.out.println("Registration No: " + s.getRegNo());
            System.out.println("Name: " + s.getFullName());
            System.out.println("Email: " + s.getEmail());
            System.out.println("GPA: " + s.calculateGPA());
            System.out.println("Enrolled Courses: " + s.getEnrolledCourses().size());
        } else {
            System.out.println("❌ Student not found!");
        }
    }
    
    private void updateStudent() {
        System.out.print("Enter Student ID to update: ");
        String id = scanner.nextLine().trim();
        
        Optional<Student> studentOpt = studentService.getStudent(id);
        if (studentOpt.isEmpty()) {
            System.out.println("❌ Student not found!");
            return;
        }
        
        Student student = studentOpt.get();
        System.out.print("Enter new name (current: " + student.getFullName() + "): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) student.setFullName(name);
        
        System.out.print("Enter new email (current: " + student.getEmail() + "): ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) student.setEmail(email);
        
        System.out.println("✅ Student updated successfully!");
    }
    
    private void generateTranscript() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();
        
        Optional<Student> student = studentService.getStudent(studentId);
        if (student.isPresent()) {
            Student.Transcript transcript = student.get().generateTranscript();
            System.out.println("\n" + transcript.toString());
        } else {
            System.out.println("❌ Student not found!");
        }
    }
    
    private void manageCourses() {
        courseMenu: while (true) {
            System.out.println("\n" + "=".repeat(30));
            System.out.println("📚 COURSE MANAGEMENT");
            System.out.println("=".repeat(30));
            System.out.println("1. ➕ Add Course");
            System.out.println("2. 📋 List All Courses");
            System.out.println("3. 🔍 Find Course by Code");
            System.out.println("4. 🏫 Courses by Department");
            System.out.println("5. 📅 Courses by Semester");
            System.out.println("6. ↩️  Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            switch (scanner.nextLine().trim()) {
                case "1" -> addCourse();
                case "2" -> listCourses();
                case "3" -> findCourse();
                case "4" -> coursesByDepartment();
                case "5" -> coursesBySemester();
                case "6" -> { break courseMenu; }
                default -> System.out.println("❌ Invalid choice.");
            }
        }
    }
    
    private void addCourse() {
        try {
            System.out.print("Enter Course Code: ");
            String code = scanner.nextLine().trim();
            System.out.print("Enter Course Title: ");
            String title = scanner.nextLine().trim();
            System.out.print("Enter Credits: ");
            int credits = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Instructor ID: ");
            String instructorId = scanner.nextLine().trim();
            System.out.print("Enter Semester (SPRING/SUMMER/FALL): ");
            Semester semester = Semester.valueOf(scanner.nextLine().trim().toUpperCase());
            System.out.print("Enter Department: ");
            String department = scanner.nextLine().trim();
            
            Course course = new Course.Builder()
                .code(code).title(title).credits(credits)
                .instructorId(instructorId).semester(semester).department(department).build();
            
            courseService.addCourse(course);
            System.out.println("✅ Course added successfully!");
            
        } catch (Exception e) {
            System.out.println("❌ Error adding course: " + e.getMessage());
        }
    }
    
    private void listCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("📭 No courses found.");
            return;
        }
        
        System.out.println("\n" + "-".repeat(100));
        System.out.printf("%-10s %-30s %-8s %-12s %-10s %-15s%n", 
            "Code", "Title", "Credits", "Instructor", "Semester", "Department");
        System.out.println("-".repeat(100));
        
        courses.forEach(course -> {
            System.out.printf("%-10s %-30s %-8d %-12s %-10s %-15s%n",
                course.getCode(), course.getTitle(), course.getCredits(),
                course.getInstructorId() != null ? course.getInstructorId() : "N/A",
                course.getSemester() != null ? course.getSemester().getDisplayName() : "N/A",
                course.getDepartment() != null ? course.getDepartment() : "N/A");
        });
    }
    
    private void findCourse() {
        System.out.print("Enter Course Code: ");
        String code = scanner.nextLine().trim();
        
        Optional<Course> course = courseService.getCourse(code);
        if (course.isPresent()) {
            Course c = course.get();
            System.out.println("\n📋 Course Details:");
            System.out.println("Code: " + c.getCode());
            System.out.println("Title: " + c.getTitle());
            System.out.println("Credits: " + c.getCredits());
            System.out.println("Instructor: " + (c.getInstructorId() != null ? c.getInstructorId() : "N/A"));
            System.out.println("Semester: " + (c.getSemester() != null ? c.getSemester().getDisplayName() : "N/A"));
            System.out.println("Department: " + (c.getDepartment() != null ? c.getDepartment() : "N/A"));
        } else {
            System.out.println("❌ Course not found!");
        }
    }
    
    private void coursesByDepartment() {
        System.out.print("Enter Department: ");
        String department = scanner.nextLine().trim();
        
        List<Course> courses = courseService.getCoursesByDepartment(department);
        if (courses.isEmpty()) {
            System.out.println("📭 No courses found in department: " + department);
            return;
        }
        
        System.out.println("\n📚 Courses in " + department + " Department:");
        courses.forEach(course -> System.out.println("• " + course.getCode() + " - " + course.getTitle()));
    }
    
    private void coursesBySemester() {
        System.out.print("Enter Semester (SPRING/SUMMER/FALL): ");
        try {
            Semester semester = Semester.valueOf(scanner.nextLine().trim().toUpperCase());
            List<Course> courses = courseService.getCoursesBySemester(semester);
            
            if (courses.isEmpty()) {
                System.out.println("📭 No courses found for semester: " + semester.getDisplayName());
                return;
            }
            
            System.out.println("\n📅 Courses for " + semester.getDisplayName() + " Semester:");
            courses.forEach(course -> System.out.println("• " + course.getCode() + " - " + course.getTitle()));
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Invalid semester. Please enter SPRING, SUMMER, or FALL.");
        }
    }
    
    private void manageEnrollments() {
        enrollmentMenu: while (true) {
            System.out.println("\n" + "=".repeat(30));
            System.out.println("📋 ENROLLMENT MANAGEMENT");
            System.out.println("=".repeat(30));
            System.out.println("1. ➕ Enroll Student in Course");
            System.out.println("2. 🗑️  Unenroll Student from Course");
            System.out.println("3. 📊 View Enrollments by Course");
            System.out.println("4. ↩️  Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            switch (scanner.nextLine().trim()) {
                case "1" -> enrollStudent();
                case "2" -> unenrollStudent();
                case "3" -> viewEnrollments();
                case "4" -> { break enrollmentMenu; }
                default -> System.out.println("❌ Invalid choice.");
            }
        }
    }
    
    private void enrollStudent() {
        try {
            System.out.print("Enter Student ID: ");
            String studentId = scanner.nextLine().trim();
            System.out.print("Enter Course Code: ");
            String courseCode = scanner.nextLine().trim();
            
            studentService.enrollStudentInCourse(studentId, courseCode);
            System.out.println("✅ Student enrolled successfully!");
            
        } catch (Exception e) {
            System.out.println("❌ Error enrolling student: " + e.getMessage());
        }
    }
    
    private void unenrollStudent() {
        try {
            System.out.print("Enter Student ID: ");
            String studentId = scanner.nextLine().trim();
            System.out.print("Enter Course Code: ");
            String courseCode = scanner.nextLine().trim();
            
            Optional<Student> student = studentService.getStudent(studentId);
            if (student.isPresent()) {
                student.get().unenrollFromCourse(courseCode);
                System.out.println("✅ Student unenrolled successfully!");
            } else {
                System.out.println("❌ Student not found!");
            }
        } catch (Exception e) {
            System.out.println("❌ Error unenrolling student: " + e.getMessage());
        }
    }
    
    private void viewEnrollments() {
    var enrollments = enrollmentService.getEnrollmentsByCourse();
    if (enrollments.isEmpty()) {
        System.out.println("📭 No enrollments found.");
        return;
    }
    
    System.out.println("\n📊 Enrollments by Course:");
    
    // Use the new method that utilizes courseService
    var courseDetails = enrollmentService.getCourseDetailsForEnrollments();
    
    enrollments.forEach((courseId, studentIds) -> {
        String courseInfo = courseDetails.getOrDefault(courseId, "Unknown Course");
        System.out.println("\n📚 " + courseId + " - " + courseInfo + " (" + studentIds.size() + " students)");
        
        studentIds.forEach(studentId -> {
            Optional<Student> student = studentService.getStudent(studentId);
            student.ifPresent(s -> System.out.println("   👨‍🎓 " + s.getFullName() + " (" + s.getId() + ")"));
        });
    });
    
    // Optional: Add a comprehensive report option
    System.out.print("\nShow detailed enrollment report? (y/n): ");
    if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
        enrollmentService.printEnrollmentReport();
    }
}
    
    private void manageGrades() {
        gradeMenu: while (true) {
            System.out.println("\n" + "=".repeat(30));
            System.out.println("📊 GRADE MANAGEMENT");
            System.out.println("=".repeat(30));
            System.out.println("1. 📝 Record Grade");
            System.out.println("2. 📈 View Student Grades");
            System.out.println("3. 🏆 Top Students by GPA");
            System.out.println("4. ↩️  Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            switch (scanner.nextLine().trim()) {
                case "1" -> recordGrade();
                case "2" -> viewStudentGrades();
                case "3" -> topStudentsByGPA();
                case "4" -> { break gradeMenu; }
                default -> System.out.println("❌ Invalid choice.");
            }
        }
    }
    
    private void recordGrade() {
        try {
            System.out.print("Enter Student ID: ");
            String studentId = scanner.nextLine().trim();
            System.out.print("Enter Course Code: ");
            String courseCode = scanner.nextLine().trim();
            System.out.print("Enter Score (0-100): ");
            double score = Double.parseDouble(scanner.nextLine().trim());
            
            studentService.recordGrade(studentId, courseCode, score);
            System.out.println("✅ Grade recorded successfully!");
            
        } catch (Exception e) {
            System.out.println("❌ Error recording grade: " + e.getMessage());
        }
    }
    
    private void viewStudentGrades() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine().trim();
        
        Optional<Student> student = studentService.getStudent(studentId);
        if (student.isPresent()) {
            Student s = student.get();
            System.out.println("\n📊 Grades for " + s.getFullName() + ":");
            System.out.println("-".repeat(50));
            
            s.getEnrolledCourses().forEach(courseId -> {
                Optional<Course> course = courseService.getCourse(courseId);
                String courseName = course.map(Course::getTitle).orElse("Unknown Course");
                Double score = s.getGrade(courseId);
                Grade grade = s.getLetterGrade(courseId);
                
                if (score != null && grade != null) {
                    System.out.printf("📚 %s - %s: %.2f (%s)%n", 
                        courseId, courseName, score, grade);
                } else {
                    System.out.printf("📚 %s - %s: No grade recorded%n", courseId, courseName);
                }
            });
            
            System.out.printf("%nOverall GPA: %.2f%n", s.calculateGPA());
        } else {
            System.out.println("❌ Student not found!");
        }
    }
    
    private void topStudentsByGPA() {
        List<Student> topStudents = studentService.getStudentsSortedByGPA().stream()
            .limit(5)
            .collect(Collectors.toList());
        
        if (topStudents.isEmpty()) {
            System.out.println("📭 No students with grades found.");
            return;
        }
        
        System.out.println("\n🏆 TOP 5 STUDENTS BY GPA");
        System.out.println("-".repeat(60));
        System.out.printf("%-20s %-12s %-8s%n", "Name", "Reg No", "GPA");
        System.out.println("-".repeat(60));
        
        topStudents.forEach(student -> {
            System.out.printf("%-20s %-12s %.2f%n",
                student.getFullName(), student.getRegNo(), student.calculateGPA());
        });
    }
    
    private void importExportData() {
        ioMenu: while (true) {
            System.out.println("\n" + "=".repeat(30));
            System.out.println("💾 IMPORT/EXPORT DATA");
            System.out.println("=".repeat(30));
            System.out.println("1. 📥 Import Students from CSV");
            System.out.println("2. 📥 Import Courses from CSV");
            System.out.println("3. 📤 Export Students to CSV");
            System.out.println("4. 📤 Export Courses to CSV");
            System.out.println("5. ↩️  Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            switch (scanner.nextLine().trim()) {
                case "1" -> importStudents();
                case "2" -> importCourses();
                case "3" -> exportStudents();
                case "4" -> exportCourses();
                case "5" -> { break ioMenu; }
                default -> System.out.println("❌ Invalid choice.");
            }
        }
    }
    
    private void importStudents() {
        System.out.print("Enter CSV file path (or press Enter for default): ");
        String path = scanner.nextLine().trim();
        Path filePath = Paths.get(path.isEmpty() ? "test-data/students.csv" : path);
        
        try {
            importExportService.importStudentsFromCSV(filePath);
            System.out.println("✅ Students imported successfully!");
        } catch (IOException e) {
            System.out.println("❌ Error importing students: " + e.getMessage());
        }
    }
    
    private void importCourses() {
        System.out.print("Enter CSV file path (or press Enter for default): ");
        String path = scanner.nextLine().trim();
        Path filePath = Paths.get(path.isEmpty() ? "test-data/courses.csv" : path);
        
        try {
            importExportService.importCoursesFromCSV(filePath);
            System.out.println("✅ Courses imported successfully!");
        } catch (IOException e) {
            System.out.println("❌ Error importing courses: " + e.getMessage());
        }
    }
    
    private void exportStudents() {
        Path filePath = AppConfig.getInstance().getDataDirectory().resolve("students_export.csv");
        
        try {
            importExportService.exportStudentsToCSV(filePath);
            System.out.println("✅ Students exported to: " + filePath);
        } catch (IOException e) {
            System.out.println("❌ Error exporting students: " + e.getMessage());
        }
    }
    
    private void exportCourses() {
        Path filePath = AppConfig.getInstance().getDataDirectory().resolve("courses_export.csv");
        
        try {
            importExportService.exportCoursesToCSV(filePath);
            System.out.println("✅ Courses exported to: " + filePath);
        } catch (IOException e) {
            System.out.println("❌ Error exporting courses: " + e.getMessage());
        }
    }
    
    private void backupOperations() {
        backupMenu: while (true) {
            System.out.println("\n" + "=".repeat(30));
            System.out.println("🗂️  BACKUP OPERATIONS");
            System.out.println("=".repeat(30));
            System.out.println("1. 💾 Create Backup");
            System.out.println("2. 📊 Show Backup Size (Recursive)");
            System.out.println("3. 📁 List Backup Files (Recursive)");
            System.out.println("4. ↩️  Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            switch (scanner.nextLine().trim()) {
                case "1" -> createBackup();
                case "2" -> showBackupSize();
                case "3" -> listBackupFiles();
                case "4" -> { break backupMenu; }
                default -> System.out.println("❌ Invalid choice.");
            }
        }
    }
    
    private void createBackup() {
        try {
            Path backupDir = backupService.createBackup();
            long size = backupService.calculateBackupSize(backupDir);
            System.out.println("✅ Backup created: " + backupDir);
            System.out.println("📊 Backup size: " + size + " bytes");
        } catch (IOException e) {
            System.out.println("❌ Error creating backup: " + e.getMessage());
        }
    }
    
   private void showBackupSize() {
    try {
        Path backupDir = AppConfig.getInstance().getBackupDirectory();
        
        // Fixed if statement syntax
        if (!Files.exists(backupDir)) {
            System.out.println("📭 No backup directory found.");
            return;
        }
        
        long totalSize = backupService.calculateBackupSize(backupDir);
        System.out.println("📊 Total backup size: " + totalSize + " bytes (" + 
            (totalSize / 1024) + " KB)");
    } catch (IOException e) {
        System.out.println("❌ Error calculating backup size: " + e.getMessage());
    }
}
    
    private void listBackupFiles() {
        try {
            Path backupDir = AppConfig.getInstance().getBackupDirectory();
            if (!Files.exists(backupDir)) {
                System.out.println("📭 No backup directory found.");
                return;
            }
            
            System.out.println("📁 Backup files structure:");
            backupService.listBackupFiles(backupDir, 3);
        } catch (IOException e) {
            System.out.println("❌ Error listing backup files: " + e.getMessage());
        }
    }
    
    private void generateReports() {
        reportMenu: while (true) {
            System.out.println("\n" + "=".repeat(30));
            System.out.println("📈 REPORTS");
            System.out.println("=".repeat(30));
            System.out.println("1. 📊 GPA Distribution");
            System.out.println("2. 🏫 Courses by Department");
            System.out.println("3. 👨‍🏫 Courses by Instructor");
            System.out.println("4. ↩️  Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            switch (scanner.nextLine().trim()) {
                case "1" -> gpaDistribution();
                case "2" -> coursesByDepartmentReport();
                case "3" -> coursesByInstructor();
                case "4" -> { break reportMenu; }
                default -> System.out.println("❌ Invalid choice.");
            }
        }
    }
    
    private void gpaDistribution() {
        var gpaGroups = studentService.getAllStudents().stream()
            .collect(Collectors.groupingBy(
                student -> {
                    double gpa = student.calculateGPA();
                    if (gpa >= 9) return "A (9.0+)";
                    else if (gpa >= 8) return "B (8.0-8.9)";
                    else if (gpa >= 7) return "C (7.0-7.9)";
                    else if (gpa >= 6) return "D (6.0-6.9)";
                    else return "F (<6.0)";
                },
                Collectors.counting()
            ));
        
        System.out.println("\n📊 GPA DISTRIBUTION");
        System.out.println("-".repeat(30));
        gpaGroups.forEach((range, count) -> {
            System.out.printf("%-12s: %d students%n", range, count);
        });
    }
    
    private void coursesByDepartmentReport() {
        var deptGroups = courseService.getCoursesGroupedByDepartment();
        
        System.out.println("\n🏫 COURSES BY DEPARTMENT");
        System.out.println("-".repeat(40));
        deptGroups.forEach((dept, courses) -> {
            System.out.printf("%-20s: %d courses%n", dept, courses.size());
            courses.forEach(course -> System.out.println("  • " + course.getCode() + " - " + course.getTitle()));
        });
    }
    
    private void coursesByInstructor() {
        System.out.print("Enter Instructor ID: ");
        String instructorId = scanner.nextLine().trim();
        
        List<Course> courses = courseService.getCoursesByInstructor(instructorId);
        if (courses.isEmpty()) {
            System.out.println("📭 No courses found for instructor: " + instructorId);
            return;
        }
        
        System.out.println("\n👨‍🏫 COURSES TAUGHT BY " + instructorId);
        System.out.println("-".repeat(50));
        courses.forEach(course -> {
            System.out.println("• " + course.getCode() + " - " + course.getTitle() + 
                " (" + course.getCredits() + " credits)");
        });
    }
    
    private void printJavaPlatformInfo() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("☕ JAVA PLATFORM INFORMATION");
        System.out.println("=".repeat(50));
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Vendor: " + System.getProperty("java.vendor"));
        System.out.println("Java Home: " + System.getProperty("java.home"));
        
        System.out.println("\n📚 Java Platform Comparison:");
        System.out.println("• Java SE (Standard Edition): Desktop, server applications");
        System.out.println("• Java EE (Enterprise Edition): Enterprise applications");
        System.out.println("• Java ME (Micro Edition): Embedded/mobile devices");
        
        System.out.println("\n🛠️  JDK/JRE/JVM:");
        System.out.println("• JDK (Java Development Kit): Development tools + JRE");
        System.out.println("• JRE (Java Runtime Environment): Runtime environment + JVM");
        System.out.println("• JVM (Java Virtual Machine): Executes Java bytecode");
        
        System.out.println("\n🎯 This application demonstrates Java SE features including:");
        System.out.println("• OOP Principles (Encapsulation, Inheritance, Polymorphism, Abstraction)");
        System.out.println("• Design Patterns (Singleton, Builder)");
        System.out.println("• File I/O with NIO.2");
        System.out.println("• Streams API and Lambdas");
        System.out.println("• Exception Handling");
        System.out.println("• Date/Time API");
        System.out.println("• Recursion and Collections");
        
        System.out.println("\nThank you for using CCRM! 👋");
    }
}