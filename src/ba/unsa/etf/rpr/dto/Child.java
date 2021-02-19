package ba.unsa.etf.rpr.dto;

import java.time.LocalDate;

public class Child implements Person{
    private String username, password, name, surname, location, address, parentName, email, avatar;
    private LocalDate date;
    private long parentNumber;
    private Educator educator;

    public Child(String username, String password, String name, String surname, LocalDate date, String location, String address, String parentName, String email, long parentNumber, String avatar, Educator educator) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.location = location;
        this.address = address;
        this.parentName = parentName;
        this.email = email;
        this.avatar = avatar;
        this.date = date;
        this.parentNumber = parentNumber;
        this.educator = educator;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getParentNumber() {
        return parentNumber;
    }

    public void setParentNumber(long parentNumber) {
        this.parentNumber = parentNumber;
    }

    public Educator getEducator() {
        return educator;
    }

    public void setEducator(Educator educator) {
        this.educator = educator;
    }

    @Override
    public String toString() {
        return getName() + " " + getSurname() + " [" + getUsername() + "]";
    }
}
