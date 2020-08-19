package com.nndi_tech.spring.web.zerocell.examples.model;

import com.creditdatamw.zerocell.annotation.Column;

public class Customer {
    @Column(index=0, name="Account Number")
    private String accountNumber;
    @Column(index=1, name="Customer Name")
    private String fullName;
    @Column(index=2, name="E-mail")
    private String email;
    @Column(index=3, name="Address")
    private String address;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
