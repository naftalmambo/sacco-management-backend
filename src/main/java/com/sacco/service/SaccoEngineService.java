package com.sacco.service;

import com.sacco.domain.*;
import com.sacco.repository.*;
import java.util.ArrayList;

public class SaccoEngineService {

    private final MemberRepository memberRepo = new MemberRepository();
    private final AccountRepository accountRepo = new AccountRepository();
    private final TransactionRepository transactionRepo = new TransactionRepository();
    private final LoanRepository loanRepo = new LoanRepository();

    public void runMemberAuditReport(long memberId) {
        SaccoMember member = memberRepo.findById(memberId);

        if (member == null) {
            System.out.println("CRITICAL ERROR: Member record with ID " + memberId + " missing from disk.");
            return;
        }

        System.out.println("\n[MEMBER IDENTITY DUMP]");
        System.out.println(member);

        System.out.println("\n[FINANCIAL ACCOUNT MANAGEMENT SYSTEM]");
        ArrayList<SaccoAccount> activeAccounts = accountRepo.findAllByMemberId(memberId);

        if (activeAccounts.isEmpty()) {
            System.out.println("No active financial accounts registered for this member.");
        } else {
            for (SaccoAccount account : activeAccounts) {
                System.out.println(account);

                ArrayList<LedgerTransaction> recentLedgerEntries = transactionRepo
                        .findRecentByAccountId(account.getAccountId());
                for (LedgerTransaction entry : recentLedgerEntries) {
                    System.out.println("   └─ " + entry);
                }
            }
        }

        System.out.println("\n[CREDIT REGISTRY ANALYSIS]");
        ArrayList<Loan> memberLoans = loanRepo.findAllByMemberId(memberId);

        if (memberLoans.isEmpty()) {
            System.out.println("No credit facility logs found for this member.");
        } else {
            for (Loan creditRecord : memberLoans) {
                System.out.println(creditRecord);
            }
        }
    }

}
