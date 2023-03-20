package com.acme.banking.dbo.repository;

import com.acme.banking.dbo.domain.Client;
import com.acme.banking.dbo.dto.CreateClientDto;

public interface ClientRepository {
    Client findById(int clientId);
    Client create(CreateClientDto client);
}
