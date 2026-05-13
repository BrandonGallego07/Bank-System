package domain.services;

import domain.model.BankProduct;
import domain.model.User;
import domain.ports.BankProductPort;
import domain.enums.UserRole;

import java.util.List;

public class BankProductService {

    private final BankProductPort bankProductPort;

    public BankProductService(BankProductPort bankProductPort) {
        this.bankProductPort = bankProductPort;
    }

    // ─── 1. CREAR PRODUCTO EN CATALOGO ────────────────────────────────────────
    public BankProduct createBankProduct(User requester, BankProduct product) {
        if (requester.getRole() != UserRole.INTERNAL_ANALYST) {
            throw new RuntimeException("Acceso denegado: solo el Analista Interno puede crear productos");
        }
        if (product.getProductCode() == null || product.getProductCode().isEmpty()) {
            throw new RuntimeException("El codigo del producto es obligatorio");
        }
        if (bankProductPort.findByCode(product.getProductCode()) != null) {
            throw new RuntimeException("Ya existe un producto con ese codigo");
        }

        bankProductPort.saveProduct(product);
        return product;
    }

    // ─── 2. CONSULTAR PRODUCTO POR CODIGO ─────────────────────────────────────
    public BankProduct getBankProductByCode(User requester, String code) {
        return bankProductPort.findByCode(code);
    }

    // ─── 3. LISTAR TODOS LOS PRODUCTOS ────────────────────────────────────────
    public List<BankProduct> getAllBankProducts(User requester) {
        return bankProductPort.findAll();
    }
}
