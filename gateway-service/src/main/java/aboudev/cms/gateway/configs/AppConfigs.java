package aboudev.cms.gateway.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppConfigs {
    private List<String> corsOrigins;
    private List<Access> accesses;
    private String adminRole;
}
