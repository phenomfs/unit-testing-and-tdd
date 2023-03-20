package com.acme.banking.dbo.dto;

public class CreateClientDto {
    private final String name;

    public CreateClientDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
