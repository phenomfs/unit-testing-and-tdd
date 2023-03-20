package com.acme.banking.dbo.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

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

    @Test
    public void shouldWithdrawWhenEnoughMoney() {
        SavingAccount sut = new SavingAccount(VALID_POSITIVE_ACCOUNT_ID, stubClient(), VALID_POSITIVE_AMOUNT);

        sut.withdraw(VALID_POSITIVE_AMOUNT);

        assertEquals(0, sut.getAmount());
    }

    @Test
    public void shouldNotWithdrawWhenNotEnoughMoney() {
        SavingAccount sut = new SavingAccount(VALID_POSITIVE_ACCOUNT_ID, stubClient(), 0);

        assertThatThrownBy(() -> sut.withdraw(1.0))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Not enough money");
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0})
    public void shouldNotWithdrawWhenAmountIsNegative(double amount) {
        SavingAccount sut = new SavingAccount(VALID_POSITIVE_ACCOUNT_ID, stubClient(), VALID_POSITIVE_AMOUNT);

        assertThatThrownBy(() -> sut.withdraw(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount should be positive");
    }

    @Test
    public void shouldDepositWhenAmountIsValid() {
        SavingAccount sut = new SavingAccount(VALID_POSITIVE_ACCOUNT_ID, stubClient(), VALID_POSITIVE_AMOUNT);

        sut.deposit(1.0);

        assertEquals(VALID_POSITIVE_AMOUNT + 1.0, sut.getAmount());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0})
    public void shouldNotDepositWhenAmountIsNegative(double amount) {
        SavingAccount sut = new SavingAccount(VALID_POSITIVE_ACCOUNT_ID, stubClient(), VALID_POSITIVE_AMOUNT);

        assertThatThrownBy(() -> sut.deposit(amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount should be positive");
    }
}