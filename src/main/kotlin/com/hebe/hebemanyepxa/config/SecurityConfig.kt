import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/admin/**").authenticated()
                    .requestMatchers("/api/admin/**").authenticated()
                    .requestMatchers("/", "/login", "/api/public/**", "/assets/**", "/uploads/**", "/error").permitAll()
                    .anyRequest().permitAll()
            }
            .formLogin { form ->
                form
                    .loginPage("/login")  // Explicitly set the login page
                    .loginProcessingUrl("/login")  // URL to submit login
                    .defaultSuccessUrl("/admin/dashboard")
                    .failureUrl("/login?error=true")  // URL for authentication failure
                    .permitAll()
            }
            .logout { logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true")
                    .permitAll()
            }

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}