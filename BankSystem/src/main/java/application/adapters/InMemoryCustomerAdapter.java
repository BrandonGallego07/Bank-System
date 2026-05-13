package application.adapters;

import domain.model.Customer;
import domain.ports.CustomerPort;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCustomerAdapter implements CustomerPort {

    private final List<Customer> customers = new ArrayList<>();

    @Override
    public Customer findByIdentificationId(String id) {
        return customers.stream()
                .filter(c -> c.getCustomerId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void saveCustomer(Customer customer) {
        customers.removeIf(c -> c.getCustomerId().equals(customer.getCustomerId()));
        customers.add(customer);
    }
}
