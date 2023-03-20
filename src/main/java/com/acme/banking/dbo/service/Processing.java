package com.acme.banking.dbo.service;

import com.acme.banking.dbo.domain.Account;
import com.acme.banking.dbo.domain.Cash;
import com.acme.banking.dbo.domain.Client;
import com.acme.banking.dbo.dto.CreateClientDto;
import com.acme.banking.dbo.exception.EntityNotFoundException;
import com.acme.banking.dbo.repository.AccountRepository;
import com.acme.banking.dbo.repository.ClientRepository;

import java.util.Collection;

public class Processing {
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;

    public Processing(ClientRepository clientRepository, AccountRepository accountRepository) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
    }

    public Client createClient(CreateClientDto createClientDto) {
        validateClientDto(createClientDto);
        return clientRepository.create(createClientDto);
    }

    public Collection<Account> getAccountsByClientId(int clientId) {
        Client client = clientRepository.findById(clientId);

        if (client == null) {
            throw new EntityNotFoundException("Unknown clientId: " + clientId);
        }

        return client.getAccounts();
    }

    public void transfer(int fromAccountId, int toAccountId, double amount) {
        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("Cannot transfer to same account");
        }

        Account from = accountRepository.findById(fromAccountId);
        Account to = accountRepository.findById(toAccountId);

        if (from == null) {
            throw new EntityNotFoundException("Unknown account " + fromAccountId);
        }

        if (to == null) {
            throw new EntityNotFoundException("Unknown account " + toAccountId);
        }

        from.withdraw(amount);
        to.deposit(amount);

        accountRepository.save(from);
        accountRepository.save(to);
    }

    public void cash(double amount, int fromAccountId) {
        Cash.log(amount, fromAccountId);
    }

    private void validateClientDto(CreateClientDto dto) {
        if (dto == null || dto.getName() == null)
            throw new IllegalArgumentException("Bad request");
    }
}
