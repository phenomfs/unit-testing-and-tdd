package com.acme.banking.dbo;

import com.acme.banking.dbo.domain.Client;

public class TestData {
    public static final String TEST_VALID_CLIENT_NAME = "dummy name";
    public static final int TEST_VALID_CLIENT_ID = 1;

    public static final int TEST_VALID_ACCOUNT_ID = 1;

    public static final double TEST_VALID_AMOUNT = 1.0;

    public static Client stubClient() {
        return new Client(TEST_VALID_CLIENT_ID, TEST_VALID_CLIENT_NAME);
    }
}
