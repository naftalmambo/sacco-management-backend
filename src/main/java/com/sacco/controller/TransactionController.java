package com.sacco.controller;

import com.sacco.domain.LedgerTransaction;
import com.sacco.service.SaccoEngineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")

public class TransactionController {

    private final SaccoEngineService saccoEngineService;

    public TransactionController(SaccoEngineService saccoEngineService) {
        this.saccoEngineService = saccoEngineService;
    }

    @GetMapping("/account/{accountId}")
    public List<LedgerTransaction> getAccountTransactions(@PathVariable long accountId) {
        return saccoEngineService.getRecentTransactions(accountId);
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam long accountId, @RequestParam long amountInCents) {
        saccoEngineService.processCashDeposit(accountId, amountInCents);
        return "Deposit of " + (amountInCents / 100.0) + " KSh processed for Account ID " + accountId;
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam long accountId, @RequestParam long amountInCents) {
        saccoEngineService.processCashWithdrawal(accountId, amountInCents);
        return "Withdrawal of " + (amountInCents / 100.0) + " KSh processed for Account ID " + accountId;
    }

    @PostMapping("/loans/{loanId}/disburse")
    public String disburseLoan(@PathVariable long loanId) {
        saccoEngineService.processLoanDisbursement(loanId);
        return "Loan ID " + loanId + " disbursed successfully.";
    }

    @PostMapping("/loans/{loanId}/repay")
    public String repayLoan(@PathVariable long loanId,
            @RequestParam long amountInCents,
            @RequestParam(defaultValue = "true") boolean isCashPayment) {
        saccoEngineService.processLoanRepayment(loanId, amountInCents, isCashPayment);
        return "Repayment of " + (amountInCents / 100.0) + " KSh processed for Loan ID " + loanId;
    }

}
