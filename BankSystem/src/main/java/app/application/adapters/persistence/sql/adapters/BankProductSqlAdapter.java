package app.application.adapters.persistence.sql.adapters;

import app.application.adapters.persistence.sql.entities.BankProductEntity;
import app.application.adapters.persistence.sql.repositories.BankProductJpaRepository;
import app.domain.models.BankProduct;
import app.domain.ports.out.IBankProductRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BankProductSqlAdapter implements IBankProductRepository {

    private final BankProductJpaRepository jpaRepository;

    public BankProductSqlAdapter(BankProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    private BankProduct toModel(BankProductEntity e) {
        BankProduct p = new BankProduct();
        p.setId(e.getId());
        p.setProductCode(e.getProductCode());
        p.setProductName(e.getProductName());
        p.setCategory(e.getCategory());
        p.setRequiresApproval(e.isRequiresApproval());
        return p;
    }

    private BankProductEntity toEntity(BankProduct p) {
        BankProductEntity e = new BankProductEntity();
        e.setId(p.getId());
        e.setProductCode(p.getProductCode());
        e.setProductName(p.getProductName());
        e.setCategory(p.getCategory());
        e.setRequiresApproval(p.isRequiresApproval());
        return e;
    }

    @Override
    public BankProduct save(BankProduct product) {
        return toModel(jpaRepository.save(toEntity(product)));
    }

    @Override
    public Optional<BankProduct> findByCode(String code) {
        return jpaRepository.findByProductCode(code).map(this::toModel);
    }

    @Override
    public List<BankProduct> findAll() {
        return jpaRepository.findAll().stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByProductCode(code);
    }
}
