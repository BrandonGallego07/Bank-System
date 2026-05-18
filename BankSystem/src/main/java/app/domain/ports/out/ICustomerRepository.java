package app.domain.ports.out;

import app.domain.models.Customer;
import java.util.Optional;

public interface ICustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findByCustomerId(String customerId);
    boolean existsByCustomerId(String customerId);
}
