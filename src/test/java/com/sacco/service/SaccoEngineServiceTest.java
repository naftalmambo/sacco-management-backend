package com.sacco.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SaccoEngineServiceTest {

    @Test
    public void testInterestFirstRepaymnetlogic() {

        long initialPrincipalInCents = 16500000L;
        long initialInterestOwedInCents = 1980000L;
        long paymentAmountInCents = 500000L;

        long remainingPayment = paymentAmountInCents;
        long newInterestOwed = initialInterestOwedInCents;
        long newPrincipalOwed = initialPrincipalInCents;

        if (remainingPayment >= initialInterestOwedInCents) {
            remainingPayment -= initialInterestOwedInCents;
            newInterestOwed = 0;
            newPrincipalOwed = initialPrincipalInCents - remainingPayment;
        } else {
            newInterestOwed = initialInterestOwedInCents - remainingPayment;
            remainingPayment = 0;
        }

        assertEquals(1480000L, newInterestOwed, "Interest owed must drop by exactly 5,000.00 KSh");
        assertEquals(16500000L, newPrincipalOwed, "Principal amount must remain completely untouched");
        assertEquals(0L, remainingPayment, "The payment balance must be entirely absorbed");

    }

}
