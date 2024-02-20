package aboudev.cms.audit.repositories;

import aboudev.cms.audit.entities.Audit;
import aboudev.cms.audit.models.AvailableAction;
import aboudev.cms.audit.models.AvailableService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {
    @Query("SELECT new aboudev.cms.audit.models.AvailableAction(a.action, COUNT(a.id)) FROM Audit AS a GROUP BY a.action")
    List<AvailableAction> getAvailableActions();

    @Query("SELECT new aboudev.cms.audit.models.AvailableService(a.service, COUNT(a.id)) FROM Audit AS a GROUP BY a.service")
    List<AvailableService> getAvailableServices();

    Page<Audit> findAll(Specification<Audit> spec, Pageable pageable);
}
