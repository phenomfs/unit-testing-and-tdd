package com.acme.banking.dbo.service;

import com.acme.banking.dbo.domain.Account;
import com.acme.banking.dbo.domain.Client;
import com.acme.banking.dbo.dto.CreateClientDto;
import com.acme.banking.dbo.exception.EntityNotFoundException;
import com.acme.banking.dbo.repository.AccountRepository;
import com.acme.banking.dbo.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static com.acme.banking.dbo.TestData.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessingTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CreateClientDto stubClientDto;

    @Mock
    private Client stubClient;

    @Mock
    private Account stubFromAccount;

    @Mock
    private Account stubToAccount;

    @InjectMocks
    private Processing processing;

    public static Stream<Arguments> invalidCreateDtoParametersSource() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(new CreateClientDto(null))
        );
    }

    public static Stream<Arguments> nullableAccountParametersSource() {
        return Stream.of(
                Arguments.of(1, mock(Account.class), 2, null),
                Arguments.of(1, null, 2, mock(Account.class))
        );
    }

    @Test
    void shouldCreateClientWhenClientDtoIsValid() {
        when(stubClientDto.getName()).thenReturn(VALID_CLIENT_NAME);
        when(clientRepository.create(stubClientDto)).thenReturn(stubClient);

        Client createdClient = processing.createClient(stubClientDto);

        assertAll(
                () -> assertEquals(stubClient, createdClient),
                () -> verify(clientRepository).create(stubClientDto)
        );
    }

    @ParameterizedTest
    @MethodSource(value = "invalidCreateDtoParametersSource")
    void shouldNotCreateClientWhenClientDtoIsInvalid(CreateClientDto invalidDto) {
        assertThatThrownBy(() -> processing.createClient(invalidDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Bad request");
    }

    @Test
    void shouldGetAccountsByClientIdWhenClientExist() {
        when(clientRepository.findById(anyInt())).thenReturn(stubClient);

        Collection<Account> stubAccounts = List.of(stubFromAccount);
        when(stubClient.getAccounts()).thenReturn(stubAccounts);


        assertEquals(
                stubAccounts,
                processing.getAccountsByClientId(VALID_POSITIVE_CLIENT_ID)
        );
    }

    @Test
    void shouldThrowExceptionWhenClientNotExist() {
        when(clientRepository.findById(anyInt())).thenReturn(null);


        assertThatThrownBy(() -> processing.getAccountsByClientId(-1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Unknown clientId: " + -1);
    }

    @Test
    void shouldTransferWhenOperationIsValid() {
        int fromAccountId = 1;
        int toAccountId = 2;
        double dummyAmount = 1.0;

        when(accountRepository.findById(fromAccountId)).thenReturn(stubFromAccount);
        when(accountRepository.findById(toAccountId)).thenReturn(stubToAccount);

        processing.transfer(fromAccountId, toAccountId, dummyAmount);

        assertAll(
                () -> verify(stubFromAccount).withdraw(dummyAmount),
                () -> verify(stubToAccount).deposit(dummyAmount),
                () -> verify(accountRepository).save(stubFromAccount),
                () -> verify(accountRepository).save(stubToAccount)
        );
    }

    @Test
    void shouldNotTransferWhenSameAccounts() {
        assertThatThrownBy(() -> processing.transfer(1, 1, 1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cannot transfer to same account");
    }

    @ParameterizedTest
    @MethodSource(value = "nullableAccountParametersSource")
    void shouldNotTransferWhenAccountNotExist(
            int fromAccountId, Account fromAccount,
            int toAccountId, Account toAccount) {

        when(accountRepository.findById(fromAccountId)).thenReturn(fromAccount);
        when(accountRepository.findById(toAccountId)).thenReturn(toAccount);

        assertThatThrownBy(() -> processing.transfer(fromAccountId, toAccountId, 1.0))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageFindingMatch("Unknown account \\d");
    }
}