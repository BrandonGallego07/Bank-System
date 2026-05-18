package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Customer;
import app.domain.models.User;
import app.domain.enums.UserRole;
import app.domain.ports.out.ICustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class GetCustomerById {

    private final ICustomerRepository customerRepository;

    public GetCustomerById(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getCustomer(User requester, String customerId) throws BusinessException {
        if (requester.getRole() == UserRole.NATURAL_PERSON_CUSTOMER ||
            requester.getRole() == UserRole.BUSINESS_CUSTOMER) {
            if (!requester.getIdentificationId().equals(customerId))
                throw new BusinessException("Acceso denegado: solo puede ver su propia informacion");
        }

        return customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado"));
    }
}
