package es.anusky.rating_books.shared.domain.event;

import java.time.LocalDateTime;

public record AuditEvent(LocalDateTime timestamp,
                         String entityType,
                         Long entityId,
                         String action,
                         String performedBy,
                         String details) {
}
