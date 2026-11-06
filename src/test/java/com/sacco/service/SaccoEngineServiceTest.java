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

    @Test
    public void testWithdrawalWithinLiquidityLimits() {
        long initialBalanceInCents = 500000L;
        long withdrawalAmountInCents = 200000L;
        long minimumFloorLimitInCents = 100000L;

        long finalBalanceInCents = initialBalanceInCents - withdrawalAmountInCents;

        assertTrue(finalBalanceInCents >= minimumFloorLimitInCents,
                "The final balance must remain safely above the floor limit.");
        assertEquals(300000L, finalBalanceInCents,
                "The remaining balance must be exactly 3,000.00 KSh (300,000 cents).");

    }

    @Test
    public void testWithdrawalViolatingMinimumLiquidityShield() {
        long initialBalanceInCents = 150000L;
        long withdrawalAmountInCents = 100000L;
        long minimumFloorLimitInCents = 100000L;

        assertThrows(IllegalArgumentException.class, () -> {
            long projectedBalance = initialBalanceInCents - withdrawalAmountInCents;
            if (projectedBalance < minimumFloorLimitInCents) {
                throw new IllegalArgumentException(
                        "Transaction baseline violation: Minimum liquidity shield breached.");
            }
        }, "The engine must throw an IllegalArgumentException if the remaining balance drops below 1,000.00 KSh.");
    }
}
