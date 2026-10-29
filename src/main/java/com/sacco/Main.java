package com.sacco;

import com.sacco.service.SaccoEngineService;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== 🏦 CORE BANKING LEDGER INTEGRATION TEST RUN ===");

        // 1. Initialize our service layer coordinator
        SaccoEngineService saccoService = new SaccoEngineService();

        // =================================================================
        // TEST SCENARIO A: The Happy Path (Valid Withdrawal from Savings)
        // Target: Account ID 2 (SAVINGS) | Current Balance: 25,400.50 KSh
        // Action: Withdraw 5,400.50 KSh (Leaving exactly 20,000.00 KSh)
        // =================================================================
        System.out.println("\n[EXECUTE RUN A: Testing Safe Savings Withdrawal]");
        saccoService.processCashWithdrawal(2L, 540050L); // 5,400.50 KSh passed as 540050 cents

        // =================================================================
        // TEST SCENARIO B: The Account Type Violation Firewall
        // Target: Account ID 1 (SHARES) | Balance: 450,000.00 KSh
        // Action: Attempting to withdraw 10,000.00 KSh from restricted capital
        // Expected: Firewall blocks it, throws exception, executes ROLLBACK
        // =================================================================
        System.out.println("\n[EXECUTE RUN B: Testing Account Type Violation Firewall]");
        saccoService.processCashWithdrawal(1L, 1000000L); // 10,000.00 KSh passed as 1000000 cents

        // =================================================================
        // TEST SCENARIO C: The Minimum Liquidity Shield Firewall
        // Target: Account ID 2 (SAVINGS) | Remaining Balance: 20,000.00 KSh
        // Action: Attempting to withdraw 19,500.00 KSh (Leaving only 500 KSh)
        // Expected: Firewall blocks it to preserve the 1,000 KSh floor limit
        // =================================================================
        System.out.println("\n[EXECUTE RUN C: Testing Minimum Liquidity Shield Firewall]");
        saccoService.processCashWithdrawal(2L, 1950000L); // 19,500.00 KSh passed as 1950000 cents

        // =================================================================
        // VERIFICATION SUMMARY: Output the updated member audit statement
        // =================================================================
        System.out.println("\n=================================================");
        System.out.println("=== 📊 FINAL LEDGER STATE VERIFICATION AUDIT ===");
        System.out.println("=================================================");
        saccoService.runMemberAuditReport(1L);
    }
}
