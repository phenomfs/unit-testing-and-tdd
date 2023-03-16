package com.acme.banking.dbo.domain;

import java.util.Collection;
import java.util.HashSet;

import static java.util.Collections.unmodifiableCollection;

public class Client {
    private int id;
    private String name;
    private Collection<Account> accounts = new HashSet<>();

    public Client(int id, String name) {
        if (id < 0) throw new IllegalArgumentException("Id cannot be negative");
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Name cannot be null or empty");

        this.id = id;
        this.name = name;
    }

    public Collection<Account> getAccounts() {
        return unmodifiableCollection(accounts);
    }

    public void addAccount(Account account) {
        if (account == null) throw new IllegalArgumentException("Account cannot be null");

        if (account.getClient() != this)
            throw new IllegalStateException("Account doesn't belong to client with id " + id);

        accounts.add(account);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
