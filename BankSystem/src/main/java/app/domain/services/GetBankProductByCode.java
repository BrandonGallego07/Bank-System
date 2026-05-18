package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.BankProduct;
import app.domain.models.User;
import app.domain.ports.out.IBankProductRepository;
import org.springframework.stereotype.Service;

@Service
public class GetBankProductByCode {

    private final IBankProductRepository productRepository;

    public GetBankProductByCode(IBankProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public BankProduct getProduct(User requester, String code) throws BusinessException {
        return productRepository.findByCode(code)
                .orElseThrow(() -> new BusinessException("Producto no encontrado con codigo: " + code));
    }
}
