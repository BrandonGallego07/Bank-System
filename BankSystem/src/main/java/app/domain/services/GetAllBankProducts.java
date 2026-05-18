package app.domain.services;

import app.domain.models.BankProduct;
import app.domain.models.User;
import app.domain.ports.out.IBankProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GetAllBankProducts {

    private final IBankProductRepository productRepository;

    public GetAllBankProducts(IBankProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<BankProduct> getAll(User requester) {
        return productRepository.findAll();
    }
}
