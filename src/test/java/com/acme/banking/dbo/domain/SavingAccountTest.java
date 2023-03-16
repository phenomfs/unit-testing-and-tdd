package com.acme.banking.dbo.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.acme.banking.dbo.TestData.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SavingAccountTest {

    public static Stream<Arguments> validAccountParametersSource() {
        return Stream.of(
                Arguments.of(0, 0.0),
                Arguments.of(VALID_POSITIVE_ACCOUNT_ID, VALID_POSITIVE_AMOUNT)
        );
    }

    @ParameterizedTest
    @MethodSource(value = "validAccountParametersSource")
    public void shouldCreateNewAccountWhenParametersAreValid(int validAccountId, double validAmount) {
        Client stubClient = stubClient();

        SavingAccount sut = new SavingAccount(validAccountId, stubClient, validAmount);

        assertAll(
                () -> assertEquals(validAccountId, sut.getId()),
                () -> assertEquals(stubClient, sut.getClient()),
                () -> assertEquals(validAmount, sut.getAmount())
        );
    }

    @Test
    public void shouldThrowExceptionWhenAmountIsNegative() {
        assertThatThrownBy(() -> new SavingAccount(VALID_POSITIVE_ACCOUNT_ID, stubClient(), -1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount cannot be negative");
    }

    @Test
    public void shouldThrowExceptionWhenAccountIdIsNegative() {
        assertThatThrownBy(() -> new SavingAccount(-1, stubClient(), VALID_POSITIVE_AMOUNT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id cannot be negative");
    }

    @Test
    public void shouldThrowExceptionWhenClientIsNull() {
        assertThatThrownBy(() -> new SavingAccount(VALID_POSITIVE_ACCOUNT_ID, null, VALID_POSITIVE_AMOUNT))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Client cannot be null");
    }
}