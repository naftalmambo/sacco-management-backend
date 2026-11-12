package com.sacco.controller;

import com.sacco.domain.Loan;
import com.sacco.service.SaccoEngineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loans")

public class LoanController {
    private final SaccoEngineService saccoEngineService;

    public LoanController(SaccoEngineService saccoEngineService) {
        this.saccoEngineService = saccoEngineService;

    }

    @GetMapping("/member/{memberId}")
    public List<Loan> getMemberLoans(@PathVariable long memberId) {
        return saccoEngineService.getLoansByMemberId(memberId);
    }

    @PostMapping("/apply")
    public Loan applyForLoan(@RequestParam long memberId,
            @RequestParam long principalAmountInCents,
            @RequestParam double interestRate) {
        return saccoEngineService.applyForLoan(memberId, principalAmountInCents, interestRate);
    }

}
