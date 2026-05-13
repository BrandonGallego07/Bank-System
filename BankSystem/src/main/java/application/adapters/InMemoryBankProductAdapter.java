package application.adapters;

import domain.model.BankProduct;
import domain.ports.BankProductPort;

import java.util.ArrayList;
import java.util.List;

public class InMemoryBankProductAdapter implements BankProductPort {

    private final List<BankProduct> products = new ArrayList<>();

    @Override
    public BankProduct findByCode(String code) {
        return products.stream()
                .filter(p -> p.getProductCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<BankProduct> findAll() {
        return new ArrayList<>(products);
    }

    @Override
    public void saveProduct(BankProduct product) {
        products.removeIf(p -> p.getProductCode().equals(product.getProductCode()));
        products.add(product);
    }
}
