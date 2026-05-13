package domain.ports;

import domain.model.BankProduct;
import java.util.List;

public interface BankProductPort {
    BankProduct findByCode(String code);
    List<BankProduct> findAll();
    void saveProduct(BankProduct product);
}
