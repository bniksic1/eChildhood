package ba.unsa.etf.rpr.dto;

import java.time.LocalDate;

public class Educator implements Person {
    private String username, password, name, surname, location, address, email, avatar;
    private LocalDate date;
    private long number;
    private boolean admin;

    public Educator(String username, String password, String name, String surname, LocalDate date, String location, String address, long number, String email, String avatar, boolean admin) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.location = location;
        this.address = address;
        this.email = email;
        this.avatar = avatar;
        this.date = date;
        this.number = number;
        this.admin = admin;
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

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return getName() + " " + getSurname() + " [" + getUsername() + "]";
    }
}
