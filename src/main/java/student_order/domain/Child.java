package student_order.domain;

import java.time.LocalDate;

public class Child extends Person {

    private String certificateNumber;
    private LocalDate certificateIssueDate;
    private RegisterOffice issueDepartment;

    public Child(String surName, String givenName, String patronymic, LocalDate dateOfBirthday) {
        super(surName, givenName, patronymic, dateOfBirthday);
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public LocalDate getCertificateIssueDate() {
        return certificateIssueDate;
    }

    public void setCertificateIssueDate(LocalDate certificateIssueDate) {
        this.certificateIssueDate = certificateIssueDate;
    }

    public RegisterOffice getIssueDepartment() {
        return issueDepartment;
    }

    public void setIssueDepartment(RegisterOffice issueDepartment) {
        this.issueDepartment = issueDepartment;
    }

    @Override
    public String toString() {
        return "Child{" +
                "certificateNumber='" + certificateNumber + '\'' +
                ", certificateIssueDate=" + certificateIssueDate +
                ", issueDepartment=" + issueDepartment +
                "} " + super.toString();
    }
}
