package bank_ports;

import bank_model.AuditLog;

public interface AuditPort {

    void saveLog(AuditLog log);
}