package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.Customer;
import app.domain.models.User;
import app.domain.enums.UserRole;
import app.domain.ports.out.ICustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class UpdateCustomerInfo {

    private final ICustomerRepository customerRepository;

    public UpdateCustomerInfo(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer update(User requester, String customerId, String email,
                           String phone, String address) throws BusinessException {
        if (requester.getRole() == UserRole.NATURAL_PERSON_CUSTOMER ||
            requester.getRole() == UserRole.BUSINESS_CUSTOMER) {
            if (!requester.getIdentificationId().equals(customerId))
                throw new BusinessException("Acceso denegado: solo puede modificar su propia informacion");
        }

        if (email != null && !email.contains("@"))
            throw new BusinessException("El correo no es valido");

        if (phone != null && (phone.length() < 7 || phone.length() > 15))
            throw new BusinessException("El telefono debe tener entre 7 y 15 digitos");

        Customer customer = customerRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        if (email != null) customer.setEmail(email);
        if (phone != null) customer.setPhone(phone);
        if (address != null) customer.setAddress(address);

        return customerRepository.save(customer);
    }
}
