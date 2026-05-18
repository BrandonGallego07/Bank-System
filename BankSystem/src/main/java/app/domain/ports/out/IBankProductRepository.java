package app.domain.ports.out;

import app.domain.models.BankProduct;
import java.util.List;
import java.util.Optional;

public interface IBankProductRepository {
    BankProduct save(BankProduct product);
    Optional<BankProduct> findByCode(String code);
    List<BankProduct> findAll();
    boolean existsByCode(String code);
}
