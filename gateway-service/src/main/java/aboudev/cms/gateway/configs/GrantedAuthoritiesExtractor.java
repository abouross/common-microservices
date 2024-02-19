package aboudev.cms.gateway.configs;


import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class GrantedAuthoritiesExtractor implements Converter<Jwt, Collection<GrantedAuthority>> {

    public Collection<GrantedAuthority> convert(Jwt jwt) {
        List<String> authorities = new ArrayList<>();

        // Resources access
        Map<String, LinkedTreeMap<String, Object>> resourcesAccess = jwt.getClaim("resource_access");
        resourcesAccess.forEach((resource, access) -> {
            if (access.containsKey("roles")) {
                List<String> roles = (List<String>) access.get("roles");
                authorities.addAll(
                        roles.stream()
                                .map(role -> String.format("%s:%s", resource, role))
                                .toList()
                );
            }
        });

        // Realm access
        LinkedTreeMap<String, List<String>> realmAccess = jwt.getClaim("realm_access");
        authorities.addAll(realmAccess.get("roles"));

        log.info("User authorities : {}", authorities);
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}

