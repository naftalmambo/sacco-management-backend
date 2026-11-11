package com.sacco.controller;

import com.sacco.service.SaccoEngineService;
import com.sacco.domain.SaccoAccount;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final SaccoEngineService saccoEngineService;

    public AccountController(SaccoEngineService saccoEngineService) {
        this.saccoEngineService = saccoEngineService;
    }

    @GetMapping("/member/{memberId}")
    public List<SaccoAccount> getMemberAccounts(@PathVariable long memberId) {
        return saccoEngineService.getAccountsByMemberId(memberId);
    }

    @GetMapping("/{accountNumber}")
    public SaccoAccount getAccountByNumber(@PathVariable String accountNumber) {
        return saccoEngineService.getAccountByNumber(accountNumber);
    }

    @PostMapping
    public SaccoAccount createAccount(@RequestParam long memberId, @RequestParam String accountType) {
        return saccoEngineService.createAccount(memberId, accountType);
    }

}
