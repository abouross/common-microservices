package aboudev.cms.audit.web.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuditForm {
    @NotBlank
    @Size(min = 2)
    private String action;
    @NotBlank
    @Size(min = 2)
    private String actionBy;
    @NotNull
    private Date date;
    private String subject;
    @NotNull
    @Size(min = 2)
    private String service;
}
