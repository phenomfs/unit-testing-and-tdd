package com.acme.banking.dbo;

import com.acme.banking.dbo.domain.Client;
import org.junit.jupiter.api.Test;

import static com.acme.banking.dbo.TestData.TEST_VALID_CLIENT_ID;
import static com.acme.banking.dbo.TestData.TEST_VALID_CLIENT_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClientTest {

    @Test
    public void shouldCreateNewClientWhenParametersAreValid() {
        Client sut = new Client(TEST_VALID_CLIENT_ID, TEST_VALID_CLIENT_NAME);

        assertEquals(TEST_VALID_CLIENT_ID, sut.getId());
        assertEquals(TEST_VALID_CLIENT_NAME, sut.getName());
    }

    @Test
    public void shouldThrowExceptionWhenIdIsNegative() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new Client(-1, TEST_VALID_CLIENT_NAME));

        assertEquals("Id cannot be negative", thrown.getMessage());
    }

    @Test
    public void shouldCreateNewClientWhenIdIsZero() {
        Client sut = new Client(0, TEST_VALID_CLIENT_NAME);

        assertEquals(0, sut.getId());
        assertEquals(TEST_VALID_CLIENT_NAME, sut.getName());
    }

    @Test
    public void shouldThrowExceptionWhenNameIsEmpty() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new Client(TEST_VALID_CLIENT_ID, ""));

        assertEquals("Name cannot be null or empty", thrown.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenNameIsNull() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new Client(TEST_VALID_CLIENT_ID, null));

        assertEquals("Name cannot be null or empty", thrown.getMessage());
    }
}
