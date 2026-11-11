package com.sacco.controller;

import com.sacco.service.SaccoEngineService;
import com.sacco.domain.SaccoMember;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final SaccoEngineService saccoEngineService;

    public MemberController(SaccoEngineService saccoEngineService) {

        this.saccoEngineService = saccoEngineService;
    }

    @GetMapping("/audit/{memberId}")
    public String getMemberAuditReport(@PathVariable long memberId) {

        if (saccoEngineService != null) {
            saccoEngineService.runMemberAuditReport(memberId);

        }

        return "Audit report for Member ID " + memberId + " successfully printed to the console.";
    }

    @GetMapping("/search/{nationalId}")
    public SaccoMember searchMemberByNationalId(@PathVariable String nationalId) {
        return saccoEngineService.getMemberByNationalId(nationalId);
    }

    @PostMapping
    public SaccoMember registerMember(@RequestBody SaccoMember request) {
        return saccoEngineService.registerMember(
                request.getFirstName(),
                request.getLastName(),
                request.getDateOfBirth(),
                request.getSex(),
                request.getEmailAddress(),
                request.getPhoneNumber(),
                request.getNationalId(),
                request.getCounty());
    }

}