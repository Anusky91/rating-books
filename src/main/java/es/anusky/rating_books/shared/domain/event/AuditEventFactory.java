package es.anusky.rating_books.shared.domain.event;

import es.anusky.rating_books.shared.domain.enums.Actions;
import es.anusky.rating_books.shared.domain.enums.Entities;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuditEventFactory {

    public AuditEvent create(Entities entity, Long entityId, Actions action, String actorAlias, String description) {
        return new AuditEvent(
                LocalDateTime.now(),
                entity.name(),
                entityId,
                action.name(),
                actorAlias,
                description
        );
    }
}
