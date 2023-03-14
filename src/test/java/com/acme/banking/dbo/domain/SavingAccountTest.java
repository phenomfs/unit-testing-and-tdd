package com.acme.banking.dbo.domain;

import org.junit.jupiter.api.Test;

import static com.acme.banking.dbo.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

class SavingAccountTest {

    @Test
    public void shouldCreateNewAccountWhenParametersAreValid() {
        Client stubClient = stubClient();

        SavingAccount sut = new SavingAccount(TEST_VALID_ACCOUNT_ID, stubClient, TEST_VALID_AMOUNT);

        assertEquals(TEST_VALID_AMOUNT, sut.getAmount());
        assertEquals(stubClient, sut.getClient());
        assertEquals(TEST_VALID_ACCOUNT_ID, sut.getId());
    }

    @Test
    public void shouldCreateNewAccountWhenIdIsZero() {
        Client stubClient = stubClient();

        SavingAccount sut = new SavingAccount(0, stubClient, TEST_VALID_AMOUNT);

        assertEquals(0, sut.getId());
        assertEquals(stubClient, sut.getClient());
        assertEquals(TEST_VALID_ACCOUNT_ID, sut.getAmount());
    }

    @Test
    public void shouldCreateNewAccountWhenAmountIsZero() {
        Client stubClient = stubClient();

        SavingAccount sut = new SavingAccount(TEST_VALID_ACCOUNT_ID, stubClient, 0.0);

        assertEquals(0.0, sut.getAmount());
        assertEquals(stubClient, sut.getClient());
        assertEquals(TEST_VALID_ACCOUNT_ID, sut.getId());
    }

    @Test
    public void shouldThrowExceptionWhenAccountIdIsNegative() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new SavingAccount(-1, stubClient(), TEST_VALID_AMOUNT));

        assertEquals("Id cannot be negative", thrown.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenClientIsNull() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new SavingAccount(TEST_VALID_ACCOUNT_ID, null, TEST_VALID_AMOUNT));

        assertEquals("Client cannot be null", thrown.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenAmountIsNegative() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new SavingAccount(TEST_VALID_ACCOUNT_ID, stubClient(), -1.0));

        assertEquals("Amount cannot be negative", thrown.getMessage());
    }
}