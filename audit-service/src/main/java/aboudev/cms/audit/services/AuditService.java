package aboudev.cms.audit.services;

import aboudev.cms.audit.entities.Audit;
import aboudev.cms.audit.models.AvailableAction;
import aboudev.cms.audit.models.AvailableService;
import aboudev.cms.audit.repositories.AuditRepository;
import aboudev.cms.audit.web.dtos.AuditDto;
import aboudev.cms.audit.web.forms.AuditForm;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {
    static final String DATE_FORMAT = "yyyy-MM-dd";
    private final AuditRepository repository;

    public List<AvailableAction> getAvailableActions() {
        return repository.getAvailableActions();
    }

    public List<AvailableService> getAvailableServices() {
        return repository.getAvailableServices();
    }

    public Page<AuditDto> page(Pageable pageable, String[] filter) {
        String action = null;
        String actionBy = null;
        String dateBegin = null;
        String dateEnd = null;
        String service = null;
        if (filter != null && filter.length > 0) {
            for (String f : filter) {
                String[] keyvalue = f.split(":");
                String key = keyvalue[0];
                String value = keyvalue[1];
                switch (key) {
                    case "action" -> action = value;
                    case "actionBy" -> actionBy = value;
                    case "dateBegin" -> dateBegin = value;
                    case "dateEnd" -> dateEnd = value;
                    case "service" -> service = value;
                }
            }
        }
        String finalAction = action;
        String finalActionBy = actionBy;
        String finalDateBegin = dateBegin;
        String finalDateEnd = dateEnd;
        String finalService = service;
        return repository.findAll((root, query, cb) -> {
                    final List<Predicate> predicates = new ArrayList<>();
                    if (finalAction != null && !finalAction.trim().isEmpty())
                        predicates.add(cb.equal(root.get("action"), finalAction.trim()));
                    if (finalActionBy != null && !finalActionBy.trim().isEmpty())
                        predicates.add(cb.like(root.get("actionBy"), "%" + finalActionBy.trim() + "%"));
                    LocalDate localDateBegin = null;
                    LocalDate localDateEnd = null;
                    if (finalDateBegin != null && !finalDateBegin.trim().isEmpty()) {
                        try {
                            localDateBegin = LocalDate.parse(finalDateBegin.trim());
                        } catch (DateTimeParseException e) {
                            log.error("error parse dateBegin value \"" + finalDateBegin + "\". Please use below format : " + DATE_FORMAT);
                            throw new RuntimeException(e);
                        }
                    }
                    if (finalDateEnd != null && !finalDateEnd.trim().isEmpty()) {
                        try {
                            localDateEnd = LocalDate.parse(finalDateEnd.trim());
                        } catch (DateTimeParseException e) {
                            log.error("error parse dateEnd value \"" + finalDateEnd + "\". Please use below format : " + DATE_FORMAT);
                            throw new RuntimeException(e);
                        }
                    }
                    if (localDateBegin != null && localDateEnd != null) {
                        predicates.add(cb.between(root.get("date").as(LocalDate.class), localDateBegin, localDateEnd));
                    } else if (localDateBegin != null) {
                        predicates.add(cb.equal(root.get("date").as(LocalDate.class), localDateBegin));
                    }

                    if (finalService != null && !finalService.trim().isEmpty())
                        predicates.add(cb.equal(root.get("service"), finalService.trim()));

                    return cb.and(predicates.toArray(new Predicate[0]));
                }, pageable)
                .map(this::dto);
    }

    private AuditDto dto(Audit audit) {
        return new AuditDto(audit.getId(), audit.getAction(), audit.getActionBy(), audit.getDate(), audit.getSubject(), audit.getService(), audit.getAuditedAt());
    }

    public AuditDto create(AuditForm form) {
        Audit audit = new Audit(null, form.getAction(), form.getActionBy(), form.getDate(), form.getSubject(), form.getService(), new Date());
        repository.save(audit);
        return dto(audit);
    }
}
