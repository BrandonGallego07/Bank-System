package app.application.adapters.persistence.sql.adapters;

import app.application.adapters.persistence.sql.entities.CustomerEntity;
import app.application.adapters.persistence.sql.repositories.CustomerJpaRepository;
import app.domain.models.Customer;
import app.domain.ports.out.ICustomerRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class CustomerSqlAdapter implements ICustomerRepository {

    private final CustomerJpaRepository jpaRepository;

    public CustomerSqlAdapter(CustomerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    private Customer toModel(CustomerEntity e) {
        Customer c = new Customer();
        c.setId(e.getId());
        c.setCustomerId(e.getCustomerId());
        c.setFullName(e.getFullName());
        c.setEmail(e.getEmail());
        c.setPhone(e.getPhone());
        c.setAddress(e.getAddress());
        c.setBirthDate(e.getBirthDate());
        c.setCustomerType(e.getCustomerType());
        c.setLegalRepresentativeId(e.getLegalRepresentativeId());
        return c;
    }

    private CustomerEntity toEntity(Customer c) {
        CustomerEntity e = new CustomerEntity();
        e.setId(c.getId());
        e.setCustomerId(c.getCustomerId());
        e.setFullName(c.getFullName());
        e.setEmail(c.getEmail());
        e.setPhone(c.getPhone());
        e.setAddress(c.getAddress());
        e.setBirthDate(c.getBirthDate());
        e.setCustomerType(c.getCustomerType());
        e.setLegalRepresentativeId(c.getLegalRepresentativeId());
        return e;
    }

    @Override
    public Customer save(Customer customer) {
        return toModel(jpaRepository.save(toEntity(customer)));
    }

    @Override
    public Optional<Customer> findByCustomerId(String customerId) {
        return jpaRepository.findByCustomerId(customerId).map(this::toModel);
    }

    @Override
    public boolean existsByCustomerId(String customerId) {
        return jpaRepository.existsByCustomerId(customerId);
    }
}
