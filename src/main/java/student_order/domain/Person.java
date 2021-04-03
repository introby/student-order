package student_order.domain;

import java.time.LocalDate;

public abstract class Person {
    private String surName;
    private String givenName;
    private String patronymic;
    private LocalDate dateOfBirthday;
    private Address address;

    public Person(String surName, String givenName, String patronymic, LocalDate dateOfBirthday) {
        this.surName = surName;
        this.givenName = givenName;
        this.patronymic = patronymic;
        this.dateOfBirthday = dateOfBirthday;
    }

    public Person() {

    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public LocalDate getDateOfBirthday() {
        return dateOfBirthday;
    }

    public void setDateOfBirthday(LocalDate dateOfBirthday) {
        this.dateOfBirthday = dateOfBirthday;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
