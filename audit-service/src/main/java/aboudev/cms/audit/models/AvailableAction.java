package aboudev.cms.audit.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AvailableAction {
    private final String action;
    private final Long count;
}
