package com.acme.banking.dbo;

import com.acme.banking.dbo.domain.Account;
import com.acme.banking.dbo.domain.Client;
import com.acme.banking.dbo.domain.SavingAccount;

public class TestData {
    public static final String VALID_CLIENT_NAME = "dummy name";
    public static final int VALID_POSITIVE_CLIENT_ID = 1;

    public static final int VALID_POSITIVE_ACCOUNT_ID = 1;

    public static final double VALID_POSITIVE_AMOUNT = 1.0;

    public static Client stubClient() {
        return new Client(VALID_POSITIVE_CLIENT_ID, VALID_CLIENT_NAME);
    }

    public static Account stubAccount(Client client) {
        return new SavingAccount(VALID_POSITIVE_ACCOUNT_ID, client, VALID_POSITIVE_AMOUNT);
    }
}
