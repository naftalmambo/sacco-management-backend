package com.sacco;

import com.sacco.service.SaccoEngineService;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== 🏦 CORE BANKING LEDGER APPLICATION ENGINE ===");
        SaccoEngineService saccoService = new SaccoEngineService();

        saccoService.runMemberAuditReport(1L);

        System.out.println("\n=================================================");

    }
}
