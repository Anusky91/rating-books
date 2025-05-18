package es.anusky.rating_books.shared.infrastructure.eventlistener;

import es.anusky.rating_books.shared.domain.event.AuditEvent;
import es.anusky.rating_books.shared.infrastructure.audit.AuditMapper;
import es.anusky.rating_books.shared.infrastructure.audit.SpringDataAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditEventListener {

    private final SpringDataAuditRepository springDataAuditRepository;
    private final AuditMapper mapper;

    @EventListener
    public void on(AuditEvent event) {
        springDataAuditRepository.save(mapper.toEntity(event));
        System.out.println(mapper.toString(event));
    }

}
