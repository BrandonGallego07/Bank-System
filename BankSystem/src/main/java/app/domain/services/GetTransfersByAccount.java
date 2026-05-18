package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Transfer;
import app.domain.models.User;
import app.domain.enums.UserRole;
import app.domain.ports.out.IAccountRepository;
import app.domain.ports.out.ITransferRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GetTransfersByAccount {

    private final ITransferRepository transferRepository;
    private final IAccountRepository accountRepository;

    public GetTransfersByAccount(ITransferRepository transferRepository, IAccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
    }

    public List<Transfer> getTransfers(User requester, String accountNumber) throws BusinessException {
        var account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BusinessException("Cuenta no encontrada"));

        if (requester.getRole() == UserRole.NATURAL_PERSON_CUSTOMER ||
            requester.getRole() == UserRole.BUSINESS_EMPLOYEE) {
            if (!account.getHolderId().equals(requester.getIdentificationId()))
                throw new BusinessException("Acceso denegado: no puede ver transferencias de otra cuenta");
        }

        return transferRepository.findByAccountNumber(accountNumber);
    }
}
