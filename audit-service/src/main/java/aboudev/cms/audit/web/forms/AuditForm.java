package aboudev.cms.audit.web.forms;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuditForm {
    @NotBlank
    @Min(2)
    private String action;
    @NotBlank
    @Min(2)
    private String actionBy;
    @NotNull
    private Date date;
    private String subject;
    @NotNull
    @Min(2)
    private String service;
}
