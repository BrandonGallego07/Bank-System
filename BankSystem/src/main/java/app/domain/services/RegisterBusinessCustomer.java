package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Customer;
import app.domain.models.User;
import app.domain.enums.UserRole;
import app.domain.ports.out.ICustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisterBusinessCustomer {

    private final ICustomerRepository customerRepository;

    public RegisterBusinessCustomer(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer register(User requester, Customer customer) throws BusinessException {
        if (requester.getRole() != UserRole.COMMERCIAL_EMPLOYEE &&
            requester.getRole() != UserRole.INTERNAL_ANALYST)
            throw new BusinessException("Acceso denegado: no tiene permisos para registrar empresas");

        if (customer.getCustomerId() == null || customer.getCustomerId().isEmpty())
            throw new BusinessException("El NIT es obligatorio");

        if (customer.getEmail() == null || !customer.getEmail().contains("@"))
            throw new BusinessException("El correo corporativo no es valido");

        if (customer.getLegalRepresentativeId() == null || customer.getLegalRepresentativeId().isEmpty())
            throw new BusinessException("El representante legal es obligatorio");

        if (customerRepository.existsByCustomerId(customer.getCustomerId()))
            throw new BusinessException("Ya existe una empresa con ese NIT");

        customer.setCustomerType("BUSINESS");
        return customerRepository.save(customer);
    }
}
