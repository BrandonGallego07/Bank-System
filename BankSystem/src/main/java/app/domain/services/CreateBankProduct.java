package app.domain.services;

import app.domain.exceptions.BusinessException;
import app.domain.models.BankProduct;
import app.domain.models.User;
import app.domain.enums.UserRole;
import app.domain.ports.out.IBankProductRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateBankProduct {

    private final IBankProductRepository productRepository;

    public CreateBankProduct(IBankProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public BankProduct create(User requester, BankProduct product) throws BusinessException {
        if (requester.getRole() != UserRole.INTERNAL_ANALYST)
            throw new BusinessException("Acceso denegado: solo el Analista Interno puede crear productos");

        if (product.getProductCode() == null || product.getProductCode().isEmpty())
            throw new BusinessException("El codigo del producto es obligatorio");

        if (productRepository.existsByCode(product.getProductCode()))
            throw new BusinessException("Ya existe un producto con ese codigo");

        return productRepository.save(product);
    }
}
