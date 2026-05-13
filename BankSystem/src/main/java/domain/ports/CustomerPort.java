package domain.ports;

import domain.model.Customer;

public interface CustomerPort {
    Customer findByIdentificationId(String id);
    void saveCustomer(Customer customer);
}
