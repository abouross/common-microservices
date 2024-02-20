package aboudev.cms.audit.web.controllers;

import aboudev.cms.audit.models.AvailableAction;
import aboudev.cms.audit.models.AvailableService;
import aboudev.cms.audit.services.AuditService;
import aboudev.cms.audit.web.dtos.AuditDto;
import aboudev.cms.audit.web.forms.AuditForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audits")
@RequiredArgsConstructor
public class AuditController {
    private final AuditService service;

    @GetMapping
    public Page<AuditDto> index(Pageable pageable, String[] filter) {
        return service.page(pageable, filter);
    }

    @GetMapping("/available-actions")
    public List<AvailableAction> availableActions() {
        return service.getAvailableActions();
    }

    @GetMapping("/available-services")
    public List<AvailableService> availableServices() {
        return service.getAvailableServices();
    }

    @PostMapping
    public AuditDto create(@RequestBody @Valid AuditForm form) {
        return service.create(form);
    }
}
