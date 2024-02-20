package aboudev.cms.audit.web.dtos;

import java.util.Date;

public record AuditDto(Long id, String action, String actionBy, Date date, String subject, String service,
                       Date auditedAt) {
}
