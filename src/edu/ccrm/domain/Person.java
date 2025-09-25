package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Person {
    protected final String id;
    protected String fullName;
    protected String email;
    protected boolean active;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    
    public Person(String id, String fullName, String email) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.fullName = Objects.requireNonNull(fullName, "Full name cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public abstract String getRole();
    
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { 
        this.fullName = Objects.requireNonNull(fullName);
        this.updatedAt = LocalDateTime.now();
    }
    public String getEmail() { return email; }
    public void setEmail(String email) { 
        this.email = Objects.requireNonNull(email);
        this.updatedAt = LocalDateTime.now();
    }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { 
        this.active = active;
        this.updatedAt = LocalDateTime.now();
    }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    
    @Override
    public String toString() {
        return String.format("Person{id='%s', name='%s', email='%s', active=%s}", id, fullName, email, active);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }
    
    @Override
    public int hashCode() { return Objects.hash(id); }
}