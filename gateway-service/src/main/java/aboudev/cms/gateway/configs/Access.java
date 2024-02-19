package aboudev.cms.gateway.configs;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Access {
    private String path;
    private String method;
    private List<String> roles;
    private String resource;
}
