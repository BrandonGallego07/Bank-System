
package bank_ports;

import bank_model.Loan;

public interface LoanPort {

    Loan findById(int id);
    void saveLoan(Loan loan);
}