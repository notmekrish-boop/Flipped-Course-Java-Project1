# 🏫 Campus Course & Records Manager (CCRM)

# 📖 Table of Contents
- [Project Overview](#-project-overview)
- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Installation](#-installation)
- [How to Run](#-how-to-run)
- [Project Structure](#-project-structure)
- [Java Evolution](#-java-evolution)
- [Java Platforms Comparison](#-java-platforms-comparison)
- [JDK/JRE/JVM Architecture](#-jdkjrejvm-architecture)
- [Usage Guide](#-usage-guide)
- [Syllabus Mapping](#-syllabus-mapping)
- [Screenshots](#-screenshots)
- [Contributing](#-contributing)

# 🎯 Project Overview

CCRM is a console-based Java application that demonstrates advanced Java SE features including OOP principles, design patterns, file I/O with NIO.2, Streams API, and modern Java development practices.

Key Objectives:
- Manage student records and course information
- Handle enrollments with business rules (credit limits)
- Record grades and generate transcripts
- Import/export data using CSV files
- Perform backup operations with recursion

# ✨ Features

 # 🔧 Core Functionality
- Student Management: Add, update, list, and deactivate students
- Course Management: Create, search, filter courses by department/instructor
- Enrollment System: Enroll/unenroll students with credit limit validation
- Grading System: Record marks, compute GPA, generate transcripts
- File Operations: CSV import/export, backup/restore functionality
- Reporting: GPA distribution, enrollment reports, top students

# 💻 Technical Features
- OOP Principles: Encapsulation, Inheritance, Polymorphism, Abstraction
- Design Patterns: Singleton, Builder
- Exception Handling: Custom exceptions, try-catch-finally
- File I/O: NIO.2 with Path/Files APIs
- Streams API: Filtering, mapping, reduction operations
- Date/Time API: Modern date handling
- Lambda Expressions: Functional programming
- Recursion: Directory traversal and size calculation

# 🛠 Technology Stack

- Java Version: Java SE 8+
- Build Tool: Pure Java (no external dependencies)
- File Format: CSV for data exchange
- Platform: Cross-platform console application

# 📥 Installation

Prerequisites
- Java JDK 8 or higher
- Git (for version control)

Windows Installation Steps

1. Download and Install Java JDK
   - Visit [Oracle JDK Downloads](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
   - Download appropriate version for Windows
   - Run installer and follow setup wizard

2. Verify Installation
   ```cmd
   java -version
   javac -version

3. Set JAVA_HOME Environment Variable 
 -Add to System Environment Variables
JAVA_HOME=C:\Program Files\Java\jdk-11.0.xx
PATH=%JAVA_HOME%\bin;%PATH%

# 🚀 How to Run
1. Clone or download the project
git clone <repository-url>
cd CCRM

2. Create bin directory
mkdir bin

3. Compile all Java files
javac -d bin src/edu/ccrm/*.java src/edu/ccrm/**/*.java

4. Run the application (with assertions)
java -ea -cp bin edu.ccrm.CCRMApp

5. Or run without assertions
java -cp bin edu.ccrm.CCRMApp
 
# 📁 Project Structure

CCRM/
├── src/edu/ccrm/
│   ├── CCRMApp.java                 # Main application class
│   ├── config/
│   │   └── AppConfig.java           # Singleton configuration
│   ├── domain/                      # Domain models
│   │   ├── Person.java              # Abstract base class
│   │   ├── Student.java             # Student entity with Builder
│   │   ├── Course.java              # Course entity with Builder
│   │   ├── Semester.java            # Enum for semesters
│   │   ├── Grade.java               # Enum for grading system
│   │   ├── Searchable.java          # Functional interface
│   │   └── exceptions/              # Custom exceptions
│   ├── service/                     # Business logic
│   │   ├── StudentService.java      # Student operations
│   │   ├── CourseService.java       # Course operations
│   │   └── EnrollmentService.java   # Enrollment management
│   ├── io/                          # File operations
│   │   ├── ImportExportService.java # CSV import/export
│   │   └── BackupService.java       # Backup operations
│   ├── util/                        # Utilities
│   │   └── Validator.java           # Validation helpers
│   └── cli/
│       └── CCRMCLI.java             # Command-line interface
├── data/                            # Auto-created data directory
├── backups/                         # Auto-created backup directory
├── test-data/                       # Sample data files
│   ├── students.csv                 # Sample student data
│   └── courses.csv                  # Sample course data
└── README.md                        # This file

# 📜 Java Evolution

Java Version Timeline
1996: Java 1.0 - Initial release

1997: Java 1.1 - Inner classes, JDBC

2000: Java 1.3 - HotSpot JVM

2004: Java 5 - Generics, annotations, autoboxing

2006: Java 6 - Scripting support

2011: Java 7 - try-with-resources, NIO.2

2014: Java 8 - Lambdas, Streams API, Date/Time API

2017: Java 9 - Module system

2018: Java 11 - LTS version, HTTP client

2021: Java 17 - LTS, sealed classes

2023: Java 21 - Virtual threads, pattern matching

# ☕ Java Platforms Comparison

Platform	Full Name	Purpose	Use Cases
Java SE	Standard Edition	Desktop/server apps	Desktop applications, utilities
Java EE	Enterprise Edition	Enterprise apps	Web apps, distributed systems
Java ME	Micro Edition	Embedded/mobile	IoT devices, mobile phones

This Project: Java SE

CCRM demonstrates Java SE features for desktop application development with:

1. Console-based user interface

2. File system operations

3. Local data storage

4. No external dependencies

# 🏗 JDK/JRE/JVM Architecture

Components Overview

┌─────────────────┐
│     JDK         │  ← Development Kit (Compiler + Tools + JRE)
│  ┌─────────────┐│
│  │     JRE     ││  ← Runtime Environment (Libraries + JVM)
│  │  ┌─────────┐││
│  │  │   JVM   │││  ← Virtual Machine (Bytecode Execution)
│  │  └─────────┘││
│  └─────────────┘│
└─────────────────┘

Detailed Explanation

1. JDK (Java Development Kit): Complete development environment

Contains: javac (compiler), java (runtime), tools, documentation

Used for: Developing and compiling Java applications

2. JRE (Java Runtime Environment): Runtime environment only

Contains: Java class libraries, JVM, configuration tools

Used for: Running compiled Java applications

3. JVM (Java Virtual Machine): Execution engine

Provides: Platform independence, memory management, optimization

Process: Loads → Verifies → Executes → Manages bytecode

Interaction Flow
1. Development: Write code → Compile with JDK (javac) → Generate .class files

2. Execution: JRE loads classes → JVM interprets bytecode → Native execution

3. Platform Independence: Write once, run anywhere (WORA) principle

# 📖 Usage Guide

Sample Commands

# Student Management:
## Add a new student
Enter Student ID: S005
Enter Registration Number: 2023005
Enter Full Name: Michael Chen
Enter Email: michael.chen@university.edu

# Generate transcript
Enter Student ID: S001

# Course Management:
## Add a new course
Enter Course Code: CS201
Enter Course Title: Advanced Programming
Enter Credits: 4
Enter Instructor ID: I005
Enter Semester: FALL
Enter Department: Computer Science

# Enrollment:
## Enroll student in course
Enter Student ID: S001
Enter Course Code: CS101

## Error handling: Credit limit exceeded
Credit limit exceeded: Current=15, Attempted=4, Max=18

# Sample Data Files

## students.csv
id,regNo,fullName,email,active
S001,2023001,John Doe,john.doe@university.edu,true
S002,2023002,Jane Smith,jane.smith@university.edu,true

## courses.csv
code,title,credits,instructorId,semester,department,active
CS101,Introduction to Programming,3,I001,SPRING,Computer Science,true
MATH201,Calculus I,4,I002,FALL,Mathematics,true

# 📚 Syllabus Mapping


Java Topic - Implementation Location - Demonstration
OOP Principles	All domain classes	Encapsulation, Inheritance, Polymorphism
Design Patterns	AppConfig.java (Singleton), Student/Course Builders	Singleton, Builder patterns
Exception Handling	Custom exceptions, service classes	Try-catch, custom exceptions
File I/O (NIO.2)	ImportExportService.java	Path, Files, Stream operations
Streams API	Service classes, reports	Filter, map, reduce, collect
Lambda Expressions	Searchable interface, comparators	Functional programming
Date/Time API	Person class, backup service	LocalDateTime, formatting
Collections Framework	All service classes	Lists, Sets, Maps, generics
Enums	Semester.java, Grade.java	Enums with fields/methods
Recursion	BackupService.java	Directory traversal
Interfaces	Searchable.java	Functional interfaces

# 🖼 Screenshots
## 1. jdk-version
![Image](https://github.com/user-attachments/assets/0bd96365-67b3-4130-aff2-8325bc480d72)
## 2. Run-Command
![Image](https://github.com/user-attachments/assets/c7abbd54-81eb-4ba9-937e-b8068a783a1f)
## 3. File-Structure
![Image](https://github.com/user-attachments/assets/fc8acbda-5a7b-47f7-bc00-3b8adf9ad313)
## 4. Main-Menu
![Image](https://github.com/user-attachments/assets/58f40c9f-41cd-4f5d-a0fb-cab8f4ffec1c)
## 5. Student-Management 
![Image](https://github.com/user-attachments/assets/228bd896-3c31-4f18-b120-2911edd56a08)

# 🔧 Enabling Assertions
## Enable assertions
java -ea -cp bin edu.ccrm.CCRMApp

## Enable system assertions only
java -esa -cp bin edu.ccrm.CCRMApp

## Disable assertions (default)
java -da -cp bin edu.ccrm.CCRMApp

# 🤝 Contributing
Fork the repository

Create a feature branch: git checkout -b feature/new-feature

Commit changes: git commit -am 'Add new feature'

Push to branch: git push origin feature/new-feature

Submit a pull request

# 📄 License
This project is created for educational purposes as part of Java programming coursework.

# 👨‍💻 Author
KRISH
Student ID: [24BCE10646]
Course: Java Programming
Institution: [VIT Bhopal University]