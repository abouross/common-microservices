package aboudev.cms.gateway.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.cors.CorsConfiguration;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final AppConfigs appConfigs;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(exchange -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(appConfigs.getCorsOrigins());
                    config.addAllowedMethod("*");
                    config.addAllowedHeader("*");
                    config.setAllowCredentials(false);
                    return config;
                }))
                .authorizeExchange(exchange -> {
                    appConfigs.getAccesses().forEach(access -> {
                        List<String> authoritiesList = new ArrayList<>();
                        access.getRoles().forEach(role -> {
                            authoritiesList.add(role);
                            authoritiesList.add(String.format("%s:%s", access.getResource(), role));
                        });
                        authoritiesList.add(appConfigs.getAdminRole());
                        authoritiesList.add(String.format("%s:%s", access.getResource(), appConfigs.getAdminRole()));
                        String[] authorities = authoritiesList.toArray(new String[0]);
                        log.debug(
                                "Access [{}] authorities: {}",
                                String.format("%s - %s", access.getPath(), access.getMethod()),
                                authorities
                        );
                        exchange.pathMatchers(HttpMethod.valueOf(access.getMethod()), access.getPath())
                                .hasAnyAuthority(authorities);
                    });

                    exchange.anyExchange().authenticated();
                });
        http.oauth2ResourceServer(
                oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor()))
        );
        return http.build();
    }

    Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter =
                new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new GrantedAuthoritiesExtractor());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
