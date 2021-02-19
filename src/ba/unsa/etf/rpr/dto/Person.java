package ba.unsa.etf.rpr.dto;

import java.time.LocalDate;

public interface Person {
    String getUsername();
    void setUsername(String username);

    String getPassword();
    void setPassword(String password);

    String getName();
    void setName(String name);

    String getSurname();
    void setSurname(String surname);

    String getLocation();
    void setLocation(String location);

    String getAddress();
    void setAddress(String address);

    String getAvatar();
    void setAvatar(String avatar);

    LocalDate getDate();
    void setDate(LocalDate date);

    String getEmail();
    void setEmail(String email);

}
