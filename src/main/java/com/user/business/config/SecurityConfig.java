package com.user.business.config;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.user.business.security.CustomAuthenticationEntryPoint;
import com.user.business.security.JwtFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .authorizeHttpRequests(auth -> auth
            		
        		// Allow static frontend pages
        	    .requestMatchers("/static/profile/**").permitAll()	
            		
                // Users endpoints
                .requestMatchers(
                    "/api/users/register",
                    "/api/users/login",
                    "/api/users/send-otp",
                    "/api/users/verify-otp",
                    "/api/users/forget-password"
                ).permitAll()
                .requestMatchers("/api/users/logout").authenticated()

//                // Kite endpoints
//                .requestMatchers(
//                		"/api/kite/**",
//                        "/api/live-price/**",
//                        "/api/live-prices",
//                        "/api/access-token",
//                        "/api/instruments/**",
//                        "/getLiveprice/**",
//                        "/api/orders/**",
//                        "/api/funds/**"
//                   
//                ).permitAll()
            
                // Uncomment this if you want all other endpoints protected
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*"); // or specify "http://localhost:5500" etc.
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false); // true only if you specify a specific origin
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

// // ✅ Add this to ignore static resources completely
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring()
//            .requestMatchers("/profile/**", "/css/**", "/js/**", "/images/**");
//    }
}
