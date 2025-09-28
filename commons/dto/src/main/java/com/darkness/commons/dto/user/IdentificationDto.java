package com.darkness.commons.dto.user;

import jakarta.validation.constraints.NotEmpty;

public class IdentificationDto {
    private Long pk;
    @NotEmpty(message = "id number must not empty")
    private String idNumber;
    @NotEmpty(message = "First name must not empty")
    private String firstName;
    @NotEmpty(message = "Last name must not empty")
    private String lastName;
    @NotEmpty(message = "Middle name must not empty")
    private String middleName;
    @NotEmpty(message = "Gender must not empty")
    private String gender;

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
