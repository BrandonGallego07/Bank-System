package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Customer;
import app.domain.models.User;
import app.domain.enums.UserRole;
import app.domain.ports.out.ICustomerRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Service
public class RegisterNaturalPerson {

    private final ICustomerRepository customerRepository;

    public RegisterNaturalPerson(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer register(User requester, Customer customer) throws BusinessException {
        if (requester.getRole() != UserRole.TELLER_EMPLOYEE &&
            requester.getRole() != UserRole.COMMERCIAL_EMPLOYEE &&
            requester.getRole() != UserRole.INTERNAL_ANALYST)
            throw new BusinessException("Acceso denegado: no tiene permisos para registrar clientes");

        if (customer.getCustomerId() == null || customer.getCustomerId().isEmpty())
            throw new BusinessException("El numero de identificacion es obligatorio");

        if (customer.getEmail() == null || !customer.getEmail().contains("@"))
            throw new BusinessException("El correo electronico no es valido");

        if (customer.getPhone() == null || customer.getPhone().length() < 7 || customer.getPhone().length() > 15)
            throw new BusinessException("El telefono debe tener entre 7 y 15 digitos");

        if (customer.getBirthDate() != null) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate birth = LocalDate.parse(customer.getBirthDate(), fmt);
            if (Period.between(birth, LocalDate.now()).getYears() < 18)
                throw new BusinessException("El cliente debe ser mayor de edad");
        }

        if (customerRepository.existsByCustomerId(customer.getCustomerId()))
            throw new BusinessException("Ya existe un cliente con ese numero de identificacion");

        customer.setCustomerType("NATURAL");
        return customerRepository.save(customer);
    }
}
