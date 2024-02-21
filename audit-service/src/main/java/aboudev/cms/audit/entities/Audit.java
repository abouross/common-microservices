package aboudev.cms.audit.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Audit {
    @Id
    @SequenceGenerator(name = "audit", sequenceName = "audit_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "audit")
    private Long id;
    @Column(nullable = false)
    private String action;
    private String actionBy;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;
    @Column(columnDefinition = "TEXT")
    private String subject;
    private String service;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date auditedAt = new Date();
}
