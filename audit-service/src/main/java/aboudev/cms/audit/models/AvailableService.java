package aboudev.cms.audit.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AvailableService {
    protected final String service;
    protected final Long count;
}
