package es.anusky.rating_books.shared.infrastructure.audit;

import es.anusky.rating_books.shared.domain.event.AuditEvent;
import org.springframework.stereotype.Component;

@Component
public class AuditMapper {

    public AuditLogEntity toEntity(AuditEvent event) {
        return AuditLogEntity.builder()
                .entityType(event.entityType())
                .action(event.action())
                .entityId(event.entityId())
                .performedBy(event.performedBy())
                .details(event.details())
                .timestamp(event.timestamp())
                .build();
    }

    public String toString(AuditEvent event) {
        return String.format(
                "[AUDIT] [%s] %s - %s(ID: %d) by %s | Details: %s",
                event.timestamp(),
                event.action().toUpperCase(),
                event.entityType().toUpperCase(),
                event.entityId(),
                event.performedBy(),
                event.details() != null && !event.details().isBlank() ? event.details() : "N/A"
        );
    }

}
