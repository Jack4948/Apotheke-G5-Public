/*
 * Copyright 2014-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pharmacy;

import org.salespointframework.EnableSalespoint;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * The main application class.
 */
@EnableSalespoint
public class Application {

	/**
	 * The main application method
	 * 
	 * @param args application arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Configuration
    @EnableWebSecurity
    static class WebSecurityConfiguration {

        @Bean
        SecurityFilterChain securityConfig(HttpSecurity http) throws Exception {

            return http
                    .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))
                    // CSRF-Schutz deaktiviert (in Produktion sollte das aktiviert werden)
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(requests -> requests
                        // Öffentliche Seiten
                        .requestMatchers("/", "/registrieren", "/Kasse").permitAll()
                        // Admin-Bereich nur für Benutzer mit Rolle "BOSS"
                        .requestMatchers("/admin/**").hasRole("BOSS")
                        // Alle anderen Anfragen erfordern Authentifizierung
                        .anyRequest().authenticated())
                    .formLogin(login -> login
                        // Benutzerdefinierte Login-Seite
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/Kasse")
                        .permitAll())
                    .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll())
                    .build();
        }
        
        /*@Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }*/
    }
}
