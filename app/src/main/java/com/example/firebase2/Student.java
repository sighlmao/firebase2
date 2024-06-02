package com.example.firebase2;

public class Student {
    private String id;
    private String name;
    private String email;

    public Student() {
        // Default constructor required for calls to DataSnapshot.getValue(Student.class)
    }

    public Student(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    // Getters and setters...
}
