package es.anusky.rating_books.shared.infrastructure.audit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAuditRepository extends JpaRepository<AuditLogEntity, Long> {
}
