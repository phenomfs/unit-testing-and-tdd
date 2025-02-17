package com.acme.banking.dbo.domain;

public interface Account {
    int getId();
    double getAmount();
    Client getClient(); //TODO reference integrity
    void withdraw(double amount);
    void deposit(double amount);
}
