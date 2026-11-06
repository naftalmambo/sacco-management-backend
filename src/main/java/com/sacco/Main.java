package com.sacco;

import com.sacco.service.SaccoEngineService;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== 🏦 CORE BANKING LEDGER INTEGRATION TEST RUN ===");

        SaccoEngineService saccoService = new SaccoEngineService();

        System.out.println("\n[EXECUTE RUN A: Testing Safe Savings Withdrawal]");
        saccoService.processCashWithdrawal(2L, 540050L);

        System.out.println("\n[EXECUTE RUN B: Testing Account Type Violation Firewall]");
        saccoService.processCashWithdrawal(1L, 1000000L);

        System.out.println("\n[EXECUTE RUN C: Testing Minimum Liquidity Shield Firewall]");
        saccoService.processCashWithdrawal(2L, 1950000L);

        System.out.println("\n[EXECUTE RUN D: Testing Write Access - Safe Savings Deposit]");
        saccoService.processCashDeposit(2L, 1500000L);

        System.out.println("\n[EXECUTE RUN E: Testing Deposit Account Type Firewall]");
        saccoService.processCashDeposit(1L, 500000L);

        System.out.println("\n[EXECUTE RUN F: Testing Enterprise Credit Amortization & Disbursement]");
        saccoService.processLoanDisbursement(1L);

        System.out.println("\n[EXECUTE RUN G: Testing Enterprise Credit Interest-First Repayment Split]");
        saccoService.processLoanRepayment(1L, 500000L, false);

        System.out.println("\n=================================================");
        System.out.println("=== 📊 FINAL LEDGER STATE VERIFICATION AUDIT ===");
        System.out.println("=================================================");
        saccoService.runMemberAuditReport(1L);
    }
}
