package domain.services;

import domain.model.Customer;
import domain.model.NaturalPersonCustomer;
import domain.model.BusinessCustomer;
import domain.model.User;
import domain.ports.CustomerPort;
import domain.enums.UserRole;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class CustomerService {

    private final CustomerPort customerPort;

    public CustomerService(CustomerPort customerPort) {
        this.customerPort = customerPort;
    }

    // ─── 1. REGISTRAR PERSONA NATURAL ─────────────────────────────────────────
    public NaturalPersonCustomer registerNaturalPerson(User requester, NaturalPersonCustomer customer) {
        validateCommonFields(customer);

        // Validar mayoria de edad
        if (customer.getBirthDate() == null || customer.getBirthDate().isEmpty()) {
            throw new RuntimeException("La fecha de nacimiento es obligatoria");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birth = LocalDate.parse(customer.getBirthDate(), formatter);
        if (Period.between(birth, LocalDate.now()).getYears() < 18) {
            throw new RuntimeException("El cliente debe ser mayor de edad");
        }

        // Unicidad de identificacion
        if (customerPort.findByIdentificationId(customer.getCustomerId()) != null) {
            throw new RuntimeException("Ya existe un cliente con ese numero de identificacion");
        }

        customerPort.saveCustomer(customer);
        return customer;
    }

    // ─── 2. REGISTRAR EMPRESA ─────────────────────────────────────────────────
    public BusinessCustomer registerBusinessCustomer(User requester, BusinessCustomer customer) {
        validateCommonFields(customer);

        if (customer.getLegalRepresentative() == null) {
            throw new RuntimeException("El representante legal es obligatorio");
        }

        if (customerPort.findByIdentificationId(customer.getCustomerId()) != null) {
            throw new RuntimeException("Ya existe una empresa con ese NIT");
        }

        customerPort.saveCustomer(customer);
        return customer;
    }

    // ─── 3. CONSULTAR CLIENTE POR ID ──────────────────────────────────────────
    public Customer getCustomerById(User requester, String customerId) {
        // Clientes solo pueden ver su propia info
        if (requester.getRole() == UserRole.NATURAL_PERSON_CUSTOMER ||
            requester.getRole() == UserRole.BUSINESS_CUSTOMER) {
            if (!requester.getRelatedId().equals(customerId)) {
                throw new RuntimeException("Acceso denegado: no puede ver informacion de otro cliente");
            }
        }
        return customerPort.findByIdentificationId(customerId);
    }

    // ─── 4. ACTUALIZAR DATOS DE CONTACTO ──────────────────────────────────────
    public void updateCustomerInfo(User requester, String customerId, String newEmail,
                                   String newPhone, String newAddress) {
        if (requester.getRole() == UserRole.NATURAL_PERSON_CUSTOMER ||
            requester.getRole() == UserRole.BUSINESS_CUSTOMER) {
            if (!requester.getRelatedId().equals(customerId)) {
                throw new RuntimeException("Acceso denegado: no puede modificar datos de otro cliente");
            }
        }

        if (newEmail != null && (!newEmail.contains("@") || !newEmail.contains("."))) {
            throw new RuntimeException("El correo electronico no es valido");
        }
        if (newPhone != null && (newPhone.length() < 7 || newPhone.length() > 15)) {
            throw new RuntimeException("El telefono debe tener entre 7 y 15 digitos");
        }

        Customer customer = customerPort.findByIdentificationId(customerId);
        if (newEmail != null) customer.setEmail(newEmail);
        if (newPhone != null) customer.setPhone(newPhone);
        if (newAddress != null) customer.setAddress(newAddress);

        customerPort.saveCustomer(customer);
    }

    // ─── VALIDACIONES COMUNES ──────────────────────────────────────────────────
    private void validateCommonFields(Customer customer) {
        if (customer.getCustomerId() == null || customer.getCustomerId().isEmpty()) {
            throw new RuntimeException("El numero de identificacion es obligatorio");
        }
        if (customer.getEmail() == null || !customer.getEmail().contains("@") || !customer.getEmail().contains(".")) {
            throw new RuntimeException("El correo electronico no es valido");
        }
        if (customer.getPhone() == null || customer.getPhone().length() < 7 || customer.getPhone().length() > 15) {
            throw new RuntimeException("El telefono debe tener entre 7 y 15 digitos");
        }
        if (customer.getAddress() == null || customer.getAddress().isEmpty()) {
            throw new RuntimeException("La direccion es obligatoria");
        }
    }
}
