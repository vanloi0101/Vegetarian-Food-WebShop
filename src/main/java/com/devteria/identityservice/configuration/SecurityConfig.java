package com.devteria.identityservice.configuration;

import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CamundaBpmProperties camundaBpmProperties;
    private final CustomJwtDecoder customJwtDecoder;
    private final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/auth/token", "/api/v1/auth/introspect", "/api/v1/auth/logout", "/api/v1/auth/refresh",
            "/api/v1/users", "/api/v1/products", "/api/v1/products/**", "/api/v1/payment/create", "/api/v1/payment/return"
    };

    @Autowired
    public SecurityConfig(CamundaBpmProperties camundaBpmProperties, CustomJwtDecoder customJwtDecoder) {
        this.camundaBpmProperties = camundaBpmProperties;
        this.customJwtDecoder = customJwtDecoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String path = camundaBpmProperties.getWebapp().getApplicationPath();
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Kích hoạt CORS
                .csrf(csrf -> csrf.disable()) // Vô hiệu hóa CSRF cho API không trạng thái
                .authorizeHttpRequests(auth -> auth
                        // Các endpoint công khai không yêu cầu xác thực
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        // Cho phép yêu cầu OPTIONS cho CORS preflight
                        .requestMatchers(new AntPathRequestMatcher("/api/v1/**", "OPTIONS")).permitAll()
                        // Các endpoint dành riêng cho Camunda (nếu cần)
                        .requestMatchers(new AntPathRequestMatcher("/camunda/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/engine-rest/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/actuator/**")).permitAll()
                        // Tất cả các yêu cầu khác đều cần xác thực
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Không lưu trạng thái phiên
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(customJwtDecoder) // Sử dụng bộ giải mã JWT tùy chỉnh
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()) // Bộ chuyển đổi JWT
                        )
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint())) // Xử lý lỗi xác thực
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Nguồn gốc được phép
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Phương thức được phép
        corsConfiguration.setAllowedHeaders(Arrays.asList("*")); // Tất cả header được phép
        corsConfiguration.setExposedHeaders(Arrays.asList("Authorization")); // Header trả về
        corsConfiguration.setAllowCredentials(true); // Cho phép gửi cookie/credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix(""); // Không sử dụng tiền tố cho quyền hạn
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
        return jwtConverter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10); // Mã hóa mật khẩu với sức mạnh 10
    }
}