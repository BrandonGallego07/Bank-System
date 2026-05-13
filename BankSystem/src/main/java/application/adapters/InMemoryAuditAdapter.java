package application.adapters;

import domain.model.AuditLog;
import domain.ports.AuditPort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryAuditAdapter implements AuditPort {

    private final List<AuditLog> logs = new ArrayList<>();

    @Override
    public void saveLog(AuditLog log) {
        logs.add(log);
    }

    @Override
    public List<AuditLog> findByProductId(String productId) {
        return logs.stream()
                .filter(l -> l.getAffectedProductId() != null &&
                             l.getAffectedProductId().equals(productId))
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findAll() {
        return new ArrayList<>(logs);
    }
}
