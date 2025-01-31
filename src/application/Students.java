package application;

import java.time.LocalDate;

public class Students {

    // Fields representing student information
    private int id;
    private String name;
    private String fatherName;
    private String surname;
    private LocalDate birthday;
    private String phone;
    private String email;
    private String studentClass;

    // Constructor to initialize a new Student object
    public Students(int id, String name, String fatherName, String surname, LocalDate birthday, String phone, String email, String studentClass) {
        this.id = id;
        this.name = name;
        this.fatherName = fatherName;
        this.surname = surname;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.studentClass = studentClass;
    }

    // Getter and setter methods for all fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }
}
