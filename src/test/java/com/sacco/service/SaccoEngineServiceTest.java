package com.sacco.service;

import com.sacco.repository.*;
import com.sacco.domain.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaccoEngineServiceTest {

    @Mock
    private MemberRepository memberRepo;

    @Mock
    private AccountRepository accountRepo;

    @Mock
    private TransactionRepository transactionRepo;

    @Mock
    private LoanRepository loanRepo;

    @InjectMocks
    private SaccoEngineService saccoEngineService;

    @Test
    void rejectsWithdrawalBelowMinimumBalance() {
        SaccoAccount savings = new SaccoAccount(1L, "128456123790", "SAVINGS");
        savings.setBalanceInCents(150_000L);

        when(accountRepo.findById(2L)).thenReturn(Optional.of(savings));

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> saccoEngineService.processCashWithdrawal(2L, 100_000L));

        assertTrue(error.getMessage().contains("1,000.00 KSh"));
        verifyNoInteractions(transactionRepo);
        verify(accountRepo, never()).save(any());
    }

    @Test
    void withdrawsCashWithinBalanceLimit() {
        SaccoAccount savings = new SaccoAccount(1L, "128456123790", "SAVINGS");
        savings.setBalanceInCents(500_000L);

        when(accountRepo.findById(2L)).thenReturn(Optional.of(savings));

        saccoEngineService.processCashWithdrawal(2L, 200_000L);

        assertEquals(300_000L, savings.getBalanceInCents());
        verify(accountRepo).save(savings);
        verify(transactionRepo).save(any());

    }

    @Test
    void repaymentCoversInterestBeforePrincipal() {
        Loan loan = new Loan(1L, 16_500_000L, 12.0);
        loan.applyForLoan();
        loan.disburseLoan();

        SaccoAccount loanAccount = new SaccoAccount(1L, "128456123791", "LOAN");
        loanAccount.setBalanceInCents(18_480_000L);

        when(loanRepo.findById(1L)).thenReturn(Optional.of(loan));
        when(accountRepo.findByMemberIdAndAccountType(1L, "LOAN")).thenReturn(Optional.of(loanAccount));

        saccoEngineService.processLoanRepayment(1L, 500_000L, true);

        assertEquals(1_480_000L, loan.getInterestOwedInCents());
        assertEquals(16_500_000L, loan.getPrincipleAmountInCents());
        verify(loanRepo).save(loan);
        verify(accountRepo).save(loanAccount);
    }

}
