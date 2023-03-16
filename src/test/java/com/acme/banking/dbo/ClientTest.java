package com.acme.banking.dbo;

import com.acme.banking.dbo.domain.Account;
import com.acme.banking.dbo.domain.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.acme.banking.dbo.TestData.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    @ParameterizedTest
    @ValueSource(ints = {0, VALID_POSITIVE_CLIENT_ID})
    public void shouldCreateNewClientWhenParametersAreValid(int validId) {
        Client sut = new Client(validId, VALID_CLIENT_NAME);

        assertAll(
                () -> assertEquals(validId, sut.getId()),
                () -> assertEquals(VALID_CLIENT_NAME, sut.getName())
        );
    }

    @Test
    public void shouldThrowExceptionWhenIdIsNegative() {
        assertThatThrownBy(() -> new Client(-1, VALID_CLIENT_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id cannot be negative");
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void shouldThrowExceptionWhenNameIsNotValid(String notValidName) {
        assertThatThrownBy(() -> new Client(VALID_POSITIVE_CLIENT_ID, notValidName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Name cannot be null or empty");
    }

    @Test
    public void shouldSuccessfullyLinkAccountWhenItBelongsToGivenClient() {
        Client sut = new Client(VALID_POSITIVE_CLIENT_ID, VALID_CLIENT_NAME);
        Account account = stubAccount(sut);

        sut.addAccount(account);

        assertTrue(sut.getAccounts().contains(account));
    }

    @Test
    public void shouldThrowExceptionWhileLinkingAccountWhenItNotBelongsToGivenClient() {
        Client sut = new Client(VALID_POSITIVE_CLIENT_ID, VALID_CLIENT_NAME);

        Client stubClient = stubClient();
        Account account = stubAccount(stubClient);


        assertAll(
                () -> assertThatThrownBy(() -> sut.addAccount(account))
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessage("Account doesn't belong to client with id " + stubClient.getId()),
                () -> assertFalse(sut.getAccounts().contains(account))
        );
    }

    @Test
    public void shouldThrowExceptionWhileLinkingAccountWhenItIsNull() {
        Client sut = new Client(VALID_POSITIVE_CLIENT_ID, VALID_CLIENT_NAME);

        assertAll(
                () -> assertThatThrownBy(() -> sut.addAccount(null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("Account cannot be null"),
                () -> assertTrue(sut.getAccounts().isEmpty())
        );
    }
}
