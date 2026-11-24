let authHeader = sessionStorage.getItem("authHeader") || "";
let activeCustomer = null;
let activeAccounts = [];
let activeLoans = [];
let activeLedgerAccountId = null;

const alertBox = document.getElementById("alert-box");
const lockedPanel = document.getElementById("locked-panel");
const workspace = document.getElementById("workspace");
const loginForm = document.getElementById("login-form");
const logoutButton = document.getElementById("logout-btn");
const clearWorkspaceButton = document.getElementById("clear-workspace-btn");

function showAlert(message, type = "success") {
  alertBox.textContent = message;
  alertBox.className = `alert ${type}`;
  alertBox.scrollIntoView({ behavior: "smooth", block: "nearest" });
}

function clearAlert() {
  alertBox.className = "alert hidden";
}

async function apiFetch(url, options = {}) {
  const headers = { ...(options.headers || {}), Authorization: authHeader };
  let response;
  try {
    response = await fetch(url, { ...options, headers });
  } catch (networkError) {
    if (sessionStorage.getItem("authHeader")) {
      signOut("Connection lost or timed out. Please sign in again.");
      throw new Error("Unauthorized");
    }
    throw new Error("Network error: Unable to reach the server.");
  }

  if (response.status === 401) {
    signOut("Invalid username or password, or session expired.");
    throw new Error("Unauthorized");
  }

  const text = await response.text();
  let data = text;
  try {
    data = JSON.parse(text);
  } catch (_) {}

  if (!response.ok) {
    const message = data && data.message ? data.message : "Request failed.";
    throw new Error(message);
  }

  return data;
}

const usernameInput = document.getElementById("username");
const passwordInput = document.getElementById("password");
const signInButton = loginForm.querySelector("button[type='submit']");

function unlockWorkspace() {
  lockedPanel.classList.add("hidden");
  workspace.classList.remove("hidden");
  usernameInput.classList.add("hidden");
  passwordInput.classList.add("hidden");
  signInButton.classList.add("hidden");
  logoutButton.classList.remove("hidden");
  clearWorkspaceButton.classList.remove("hidden");
}
function resetCustomerPanels(
  accountsMessage = "Search for a customer above to view their accounts here.",
  loansMessage = "No customer loaded yet.",
) {
  activeAccounts = [];
  activeLoans = [];
  activeLedgerAccountId = null;

  document.getElementById("accounts-list").innerHTML =
    `<p class="muted">${accountsMessage}</p>`;
  document.getElementById("loans-list").innerHTML =
    `<p class="muted">${loansMessage}</p>`;
  document.getElementById("transactions-list").innerHTML =
    '<p class="muted">Click "View Ledger" on any account card to inspect recent transactions.</p>';
}
function resetTransactionInputs() {
  openAccountForm.reset();
  cashForm.reset();
  loanApplyForm.reset();
  loanRepayForm.reset();
}

function signOut(message = "Signed out.") {
  authHeader = "";
  activeCustomer = null;
  sessionStorage.removeItem("authHeader");

  workspace.classList.add("hidden");
  lockedPanel.classList.remove("hidden");
  usernameInput.classList.remove("hidden");
  passwordInput.classList.remove("hidden");
  signInButton.classList.remove("hidden");
  logoutButton.classList.add("hidden");
  clearWorkspaceButton.classList.add("hidden");

  loginForm.reset();
  searchForm.reset();
  registerForm.reset();
  cashForm.reset();
  loanApplyForm.reset();
  loanRepayForm.reset();

  document.getElementById("customer-profile").classList.add("hidden");
  resetCustomerPanels();
  showAlert(message, "error");
}

loginForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  clearAlert();

  const username = usernameInput.value.trim();
  const password = passwordInput.value;
  authHeader = "Basic " + btoa(`${username}:${password}`);

  try {
    await apiFetch("/api/members/search/00000000");
  } catch (error) {
    if (error.message === "Unauthorized") {
      return;
    }
  }

  sessionStorage.setItem("authHeader", authHeader);
  unlockWorkspace();
  showAlert(`Welcome! Teller session active.`, "success");
});

logoutButton.addEventListener("click", () => {
  signOut("Teller signed out safely.");
});

function clearWorkspace() {
  activeCustomer = null;
  searchForm.reset();
  registerForm.reset();
  cashForm.reset();
  loanApplyForm.reset();
  loanRepayForm.reset();
  openAccountForm.reset();

  document.getElementById("customer-profile").classList.add("hidden");
  resetCustomerPanels();
  showAlert("Workspace cleared. Ready for next customer.", "success");
}
clearWorkspaceButton.addEventListener("click", clearWorkspace);

if (authHeader) {
  unlockWorkspace();
}

function displayCustomerProfile(member) {
  activeCustomer = member;

  document.getElementById("customer-name").textContent =
    `${member.firstName} ${member.lastName}`;
  document.getElementById("customer-id").textContent = member.memberId;
  document.getElementById("customer-national-id").textContent =
    member.nationalId;
  document.getElementById("customer-date-of-birth").textContent =
    member.dateOfBirth;
  document.getElementById("customer-sex").textContent = member.sex;
  document.getElementById("customer-email").textContent = member.emailAddress;
  document.getElementById("customer-phone").textContent = member.phoneNumber;
  document.getElementById("customer-county").textContent = member.county;
  document.getElementById("customer-country").textContent = member.country;
  document.getElementById("customer-joined-date").textContent =
    member.joinedDate;

  document.getElementById("customer-profile").classList.remove("hidden");
}

const searchForm = document.getElementById("search-form");

searchForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  clearAlert();

  const nationalId = document.getElementById("search-national-id").value.trim();
  resetCustomerPanels();
  resetTransactionInputs();
  try {
    const member = await apiFetch(`/api/members/search/${nationalId}`);
    displayCustomerProfile(member);
    await loadCustomerAccounts(member.memberId);
    await loadCustomerLoans(member.memberId);
    showAlert(
      `Loaded customer: ${member.firstName} ${member.lastName}.`,
      "success",
    );
  } catch (error) {
    if (error.message !== "Unauthorized") {
      document.getElementById("customer-profile").classList.add("hidden");
      activeCustomer = null;
      resetCustomerPanels();
      showAlert(error.message, "error");
    }
  }
});

const registerForm = document.getElementById("register-form");

registerForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  clearAlert();

  const payload = {
    nationalId: document.getElementById("national-id").value.trim(),
    firstName: document.getElementById("first-name").value.trim(),
    lastName: document.getElementById("last-name").value.trim(),
    dateOfBirth: document.getElementById("date-of-birth").value,
    sex: document.getElementById("sex").value,
    emailAddress: document.getElementById("email").value.trim(),
    phoneNumber: document.getElementById("phone").value.trim(),
    county: document.getElementById("county").value,
  };

  const confirmed = window.confirm(
    `Confirm registration for ${payload.firstName} ${payload.lastName} (National ID: ${payload.nationalId})?
     `,
  );
  if (!confirmed) {
    return;
  }

  try {
    const createdMember = await apiFetch("/api/members", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });

    displayCustomerProfile(createdMember);
    resetCustomerPanels(
      "No accounts found for this customer yet.",
      "No loan facilities found for this customer.",
    );
    registerForm.reset();
    searchForm.reset();
    resetTransactionInputs();

    showAlert(
      `Registered customer: ${createdMember.firstName} ${createdMember.lastName}.`,
      "success",
    );
  } catch (error) {
    if (error.message !== "Unauthorized") {
      showAlert(error.message, "error");
    }
  }
});

function formatKsh(cents) {
  const shillings = (cents || 0) / 100;
  const formatted = shillings.toLocaleString("en-KE", {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });

  return `KSh ${formatted}`;
}

async function loadCustomerAccounts(memberId) {
  const accountsList = document.getElementById("accounts-list");
  const accounts = await apiFetch(`/api/accounts/member/${memberId}`);
  activeAccounts = accounts;

  if (accounts.length === 0) {
    accountsList.innerHTML =
      '<p class="muted">No accounts found for this customer yet.</p>';
    return;
  }

  const cards = accounts.map((account) => {
    const openedAt = account.createdAt
      ? account.createdAt.replace("T", " ").slice(0, 19)
      : "";

    return `
      <div class="account-card">
        <strong>${account.accountType}</strong> (${account.accountStatus})
        <p class="muted">ID: ${account.accountId} | No: ${account.accountNumber}</p>
        <div class="balance">${formatKsh(account.balanceInCents)}</div>
        <p class="muted">Opened: ${openedAt}</p>
        <button type="button" class="btn secondary view-ledger-btn" data-account-id="${account.accountId}">View Ledger</button>

      </div>
    `;
  });
  accountsList.innerHTML = cards.join("");
}

const openAccountForm = document.getElementById("open-account-form");

openAccountForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  clearAlert();

  if (!activeCustomer) {
    showAlert(
      "Search or register a customer before opening an account!",
      "error",
    );
    return;
  }

  const accountType = document.getElementById("account-type").value;

  const confirmed = window.confirm(
    `Open a new ${accountType} account for ${activeCustomer.firstName} ${activeCustomer.lastName} (ID: ${activeCustomer.memberId})?`,
  );
  if (!confirmed) {
    return;
  }

  try {
    const newAccount = await apiFetch(
      `/api/accounts?memberId=${activeCustomer.memberId}&accountType=${accountType}`,
      { method: "POST" },
    );

    await loadCustomerAccounts(activeCustomer.memberId);
    showAlert(
      `Opened ${newAccount.accountType} account (${newAccount.accountNumber}).`,
      "success",
    );
  } catch (error) {
    if (error.message !== "Unauthorized") {
      showAlert(error.message, "error");
    }
  }
});

function getValidatedCashInput() {
  const accountId = Number(document.getElementById("cash-account-id").value);
  const amountShillings = Number(document.getElementById("cash-amount").value);

  if (!accountId || accountId <= 0) {
    showAlert("Please enter a valid Account ID.", "error");
    return null;
  }

  if (!amountShillings || amountShillings <= 0) {
    showAlert("Please enter an amount greater than 0 KSh.", "error");
    return null;
  }

  return {
    accountId,
    amountInCents: Math.round(amountShillings * 100),
  };
}
function isAllowedCashAccount(action, targetAccount) {
  if (
    action === "deposit" &&
    targetAccount.accountType !== "SAVINGS" &&
    targetAccount.accountType !== "SHARES"
  ) {
    showAlert(
      "Direct counter deposits are restricted to SAVINGS and SHARES accounts.",
      "error",
    );
    return false;
  }

  if (action === "withdraw" && targetAccount.accountType !== "SAVINGS") {
    showAlert(
      "Direct counter withdrawals are restricted strictly to SAVINGS accounts.",
      "error",
    );
    return false;
  }

  return true;
}

function hasSufficientSavingsBalance(targetAccount, amountInCents) {
  if (targetAccount.balanceInCents <= 0) {
    showAlert(
      "Cannot withdraw: Account balance is KSh 0.00. Please deposit funds first.",
      "error",
    );
    return false;
  }

  const minimumFloorCents = 100000;
  if (targetAccount.balanceInCents - amountInCents < minimumFloorCents) {
    showAlert(
      `Insufficient funds: Balance (${formatKsh(targetAccount.balanceInCents)}) cannot drop below KSh 1,000.00.`,
      "error",
    );
    return false;
  }

  return true;
}

async function processCashTransaction(action) {
  clearAlert();

  const input = getValidatedCashInput();
  if (!input) {
    return;
  }
  if (!activeCustomer) {
    showAlert(
      "Load a customer profile first to verify account ownership.",
      "error",
    );
    return;
  }

  const targetAccount = activeAccounts.find(
    (account) => account.accountId === input.accountId,
  );

  if (!targetAccount) {
    showAlert(
      `Account ID ${input.accountId} does not belong to ${activeCustomer.firstName} ${activeCustomer.lastName}.`,
      "error",
    );
    return;
  }

  if (!isAllowedCashAccount(action, targetAccount)) {
    return;
  }

  if (
    action === "withdraw" &&
    !hasSufficientSavingsBalance(targetAccount, input.amountInCents)
  ) {
    return;
  }

  const confirmed = window.confirm(
    `Confirm ${action.toUpperCase()} of ${formatKsh(input.amountInCents)}?\n` +
      `Customer: ${activeCustomer.firstName} ${activeCustomer.lastName}\n` +
      `Account: ${targetAccount.accountType} | No: ${targetAccount.accountNumber} (ID: ${targetAccount.accountId})`,
  );
  if (!confirmed) {
    return;
  }

  try {
    const message = await apiFetch(
      `/api/transactions/${action}?accountId=${input.accountId}&amountInCents=${input.amountInCents}`,
      { method: "POST" },
    );

    document.getElementById("cash-amount").value = "";

    if (activeCustomer) {
      await loadCustomerAccounts(activeCustomer.memberId);
      await refreshOpenLedger();
    }

    showAlert(message, "success");
  } catch (error) {
    if (error.message !== "Unauthorized") {
      showAlert(error.message, "error");
    }
  }
}

const cashForm = document.getElementById("cash-form");
const depositButton = document.getElementById("deposit-button");
const withdrawButton = document.getElementById("withdraw-button");

depositButton.addEventListener("click", () => {
  processCashTransaction("deposit");
});

withdrawButton.addEventListener("click", () => {
  processCashTransaction("withdraw");
});

function renderLoanCard(loan) {
  const appliedAt = loan.appliedAt
    ? loan.appliedAt.replace("T", " ").slice(0, 19)
    : "";

  const disburseAction =
    loan.loanStatus === "PENDING APPROVAL"
      ? `<button type="button" class="btn primary disburse-loan-btn" data-loan-id="${loan.loanId}">Disburse Loan</button>`
      : "";

  return `
    <div class="account-card">
      <strong>Loan #${loan.loanId}</strong> (${loan.loanStatus})
      <p class="muted">Original loan: ${formatKsh(loan.originalPrincipalInCents)}</p>
      <p class="muted">Principal Owed: ${formatKsh(loan.principleAmountInCents)}</p>
      <p class="muted">Interest Owed: ${formatKsh(loan.interestOwedInCents)} (${loan.interestRate}%)</p>
      <p class="muted">Applied: ${appliedAt}</p>
      ${disburseAction}
    </div>
  `;
}

async function loadCustomerLoans(memberId) {
  const loansList = document.getElementById("loans-list");
  const loans = await apiFetch(`/api/loans/member/${memberId}`);
  activeLoans = loans;

  if (loans.length === 0) {
    loansList.innerHTML =
      '<p class="muted">No loan facilities found for this customer.</p>';
    return;
  }

  loansList.innerHTML = loans.map(renderLoanCard).join("");
}

function hasAccountType(accountType) {
  return activeAccounts.some((account) => account.accountType === accountType);
}
function confirmLoanDisbursement(targetLoan) {
  return window.confirm(
    `Confirm DISBURSEMENT of Loan #${targetLoan.loanId} (${formatKsh(targetLoan.principleAmountInCents)})?\n` +
      `Customer: ${activeCustomer.firstName} ${activeCustomer.lastName}\n` +
      `Funds will be credited directly to customer's SAVINGS account.`,
  );
}

async function disbursementLoan(loanId) {
  clearAlert();

  if (!activeCustomer) {
    return;
  }

  if (!hasAccountType("SAVINGS") || !hasAccountType("LOAN")) {
    showAlert(
      "Disbursement blocked: Customer must hold both an active SAVINGS account and an active LOAN account.",
      "error",
    );
    return;
  }

  const targetLoan = activeLoans.find((loan) => loan.loanId === loanId);
  if (!targetLoan) {
    return;
  }

  if (!confirmLoanDisbursement(targetLoan)) {
    return;
  }

  try {
    const message = await apiFetch(
      `/api/transactions/loans/${loanId}/disburse`,
      { method: "POST" },
    );

    await loadCustomerAccounts(activeCustomer.memberId);
    await loadCustomerLoans(activeCustomer.memberId);
    await refreshOpenLedger();

    showAlert(message, "success");
  } catch (error) {
    if (error.message !== "Unauthorized") {
      showAlert(error.message, "error");
    }
  }
}

document.getElementById("loans-list").addEventListener("click", (event) => {
  if (event.target.classList.contains("disburse-loan-btn")) {
    const loanId = Number(event.target.dataset.loanId);
    disbursementLoan(loanId);
  }
});

function getValidatedLoanApplyInput() {
  const principalShillings = Number(
    document.getElementById("loan-principal").value,
  );
  const interestRate = Number(
    document.getElementById("loan-interest-rate").value,
  );

  if (!principalShillings || principalShillings <= 0) {
    showAlert("Please enter a loan principal greater than 0 KSh.", "error");
    return null;
  }

  if (!interestRate || interestRate <= 0) {
    showAlert("Please enter an interest rate greater than 0%.", "error");
    return null;
  }

  return {
    principalAmountInCents: Math.round(principalShillings * 100),
    interestRate,
  };
}
function hasFundedSavingsAccount() {
  const minimumSavingsCents = 7500000;
  const savingsAccount = activeAccounts.find(
    (account) => account.accountType === "SAVINGS",
  );

  if (!savingsAccount || savingsAccount.balanceInCents < minimumSavingsCents) {
    showAlert(
      "Loan application blocked: Customer must have at least KSh 75,000.00 in SAVINGS.",
      "error",
    );
    return false;
  }

  return true;
}

function confirmLoanApplication(input) {
  return window.confirm(
    `Confirm LOAN APPLICATION for ${activeCustomer.firstName} ${activeCustomer.lastName} (ID: ${activeCustomer.nationalId})?\n` +
      `Principal: ${formatKsh(input.principalAmountInCents)} | Interest Rate: ${input.interestRate}%`,
  );
}
const loanApplyForm = document.getElementById("loan-apply-form");

loanApplyForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  clearAlert();

  if (!activeCustomer) {
    showAlert(
      "Search or register a customer before submitting a loan application.",
      "error",
    );
    return;
  }

  const input = getValidatedLoanApplyInput();
  if (!input) {
    return;
  }

  if (!hasFundedSavingsAccount()) {
    return;
  }

  if (!confirmLoanApplication(input)) {
    return;
  }

  try {
    const query = `memberId=${activeCustomer.memberId}&principalAmountInCents=${input.principalAmountInCents}&interestRate=${input.interestRate}`;
    const createdLoan = await apiFetch(`/api/loans/apply?${query}`, {
      method: "POST",
    });

    loanApplyForm.reset();
    await loadCustomerLoans(activeCustomer.memberId);
    showAlert(
      `Loan #${createdLoan.loanId} submitted (${createdLoan.loanStatus}).`,
      "success",
    );
  } catch (error) {
    if (error.message !== "Unauthorized") {
      showAlert(error.message, "error");
    }
  }
});

function getValidatedLoanRepayInput() {
  const loanId = Number(document.getElementById("repay-loan-id").value);
  const amountShillings = Number(document.getElementById("repay-amount").value);
  const isCashPayment =
    document.getElementById("repay-method").value === "true";

  if (!loanId || loanId <= 0) {
    showAlert("Please enter a valid Loan ID.", "error");
    return null;
  }

  if (!amountShillings || amountShillings <= 0) {
    showAlert("Please enter a repayment amount greater than 0 KSh.", "error");
    return null;
  }

  return {
    loanId,
    amountInCents: Math.round(amountShillings * 100),
    isCashPayment,
  };
}

function isEligibleForRepayment(targetLoan, amountInCents) {
  if (!targetLoan) {
    showAlert("Loan ID does not belong to the loaded customer.", "error");
    return false;
  }
  if (targetLoan.loanStatus !== "ACTIVE") {
    showAlert(
      `Cannot repay Loan #${targetLoan.loanId}: Status is ${targetLoan.loanStatus}.`,
      "error",
    );
    return false;
  }

  const totalOwedCents =
    targetLoan.interestOwedInCents + targetLoan.principleAmountInCents;

  if (amountInCents > totalOwedCents) {
    showAlert(
      `Repayment exceeds total balance owed (${formatKsh(totalOwedCents)}).`,
      "error",
    );
    return false;
  }
  return true;
}

function canRepayFromSavings(amountInCents) {
  const savingsAccount = activeAccounts.find(
    (account) => account.accountType === "SAVINGS",
  );
  if (!savingsAccount) {
    showAlert("Customer does not hold an active SAVINGS account.", "error");
    return false;
  }

  const minimumFloorCents = 100000;
  if (savingsAccount.balanceInCents - amountInCents < minimumFloorCents) {
    showAlert(
      `Insufficient savings: Balance (${formatKsh(savingsAccount.balanceInCents)}) must retain KSh 1,000.00.`,
      "error",
    );
    return false;
  }

  return true;
}
function confirmLoanRepayment(targetLoan, input) {
  const methodLabel = input.isCashPayment
    ? "Cash Payment"
    : "Deduct from Savings";

  return window.confirm(
    `Confirm LOAN REPAYMENT of ${formatKsh(input.amountInCents)} for Loan #${targetLoan.loanId}?\n` +
      `Customer: ${activeCustomer.firstName} ${activeCustomer.lastName} | Method: ${methodLabel}`,
  );
}
async function executeLoanRepayment(input) {
  const query = `amountInCents=${input.amountInCents}&isCashPayment=${input.isCashPayment}`;
  const message = await apiFetch(
    `/api/transactions/loans/${input.loanId}/repay?${query}`,
    { method: "POST" },
  );

  loanRepayForm.reset();
  await loadCustomerAccounts(activeCustomer.memberId);
  await loadCustomerLoans(activeCustomer.memberId);
  await refreshOpenLedger();
  showAlert(message, "success");
}
const loanRepayForm = document.getElementById("loan-repay-form");

loanRepayForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  clearAlert();

  if (!activeCustomer) {
    showAlert("Search or register a customer before repaying a loan.", "error");
    return;
  }

  const input = getValidatedLoanRepayInput();
  if (!input) {
    return;
  }

  const targetLoan = activeLoans.find((loan) => loan.loanId === input.loanId);
  if (!isEligibleForRepayment(targetLoan, input.amountInCents)) {
    return;
  }

  if (!input.isCashPayment && !canRepayFromSavings(input.amountInCents)) {
    return;
  }

  if (!confirmLoanRepayment(targetLoan, input)) {
    return;
  }

  try {
    await executeLoanRepayment(input);
  } catch (error) {
    if (error.message !== "Unauthorized") {
      showAlert(error.message, "error");
    }
  }
});

function renderTransactionCard(tx, accountNumber) {
  const timestamp = tx.createdAt
    ? tx.createdAt.replace("T", " ").slice(0, 19)
    : "";

  return `
    <div class="account-card">
      <strong>${tx.transactionType}</strong> — ${formatKsh(tx.amountInCents)}
      <p class="muted">Account No: ${accountNumber} (ID: ${tx.accountId}) | Tx ID: ${tx.transactionId} | Date: ${timestamp}</p>
    </div>
  `;
}

function renderAccountLedger(transactions, targetAccount) {
  const transactionsList = document.getElementById("transactions-list");
  const accountNumber = targetAccount ? targetAccount.accountNumber : "N/A";
  const accountType = targetAccount ? targetAccount.accountType : "ACCOUNT";
  const accountId = targetAccount ? targetAccount.accountId : "";

  if (transactions.length === 0) {
    transactionsList.innerHTML = `<p class="muted">No recent transactions found for ${accountType} — No: ${accountNumber} (ID: ${accountId}).</p>`;
    return;
  }

  const header = `<p class="muted"><strong>Showing last ${transactions.length} transactions for ${accountType} — No: ${accountNumber} (ID: ${accountId})</strong></p>`;
  const cards = transactions.map((tx) =>
    renderTransactionCard(tx, accountNumber),
  );
  transactionsList.innerHTML = header + cards.join("");
}
async function refreshOpenLedger() {
  if (activeLedgerAccountId !== null) {
    await loadAccountLedger(activeLedgerAccountId);
  }
}
async function loadAccountLedger(accountId) {
  activeLedgerAccountId = accountId;
  const targetAccount = activeAccounts.find(
    (account) => account.accountId === accountId,
  );

  try {
    const transactions = await apiFetch(
      `/api/transactions/account/${accountId}`,
    );
    renderAccountLedger(transactions, targetAccount);
  } catch (error) {
    if (error.message !== "Unauthorized") {
      showAlert(error.message, "error");
    }
  }
}
document.getElementById("accounts-list").addEventListener("click", (event) => {
  if (event.target.classList.contains("view-ledger-btn")) {
    const accountId = Number(event.target.dataset.accountId);
    loadAccountLedger(accountId);
  }
});
