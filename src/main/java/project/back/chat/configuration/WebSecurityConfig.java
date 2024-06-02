package project.back.chat.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers()
                .frameOptions().sameOrigin()
                .and()
                .formLogin()
                .and()
                .authorizeRequests()
                .antMatchers("/chat/**").hasRole("USER")
                .anyRequest().permitAll();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("happydaddy")
                .password("{noop}1234")
                .roles("USER")
                .build());
        manager.createUser(User.withUsername("angrydaddy")
                .password("{noop}1234")
                .roles("USER")
                .build());
        manager.createUser(User.withUsername("guest")
                .password("{noop}1234")
                .roles("GUEST")
                .build());
        return manager;
    }
}
