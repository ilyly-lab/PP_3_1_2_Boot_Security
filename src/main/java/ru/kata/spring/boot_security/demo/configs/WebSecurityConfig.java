package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

//@Configuration
@EnableWebSecurity//@Configuration входит в нее
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private UserServiceImpl userService;
    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {//метод от WebSecurityConfigurerAdapter
        http
                //.csrf().disable()//отключаем защиту от межсайтовой подделки запросов
                .authorizeRequests()//правила доступа
                .antMatchers("/auth/login","/error").permitAll()//указывает URL-адреса, к которым разрешен доступ. В данном случае разрешен доступ к главной странице и странице index.
                .antMatchers("/admin/**", "/addNewUser","/updateInfo").hasRole("ADMIN")
                .antMatchers("/user/**").authenticated()
                .anyRequest().authenticated()//Метод anyRequest() указывает, что для всех остальных URL-адресов требуется аутентификация (т.е. пользователь должен быть зарегистрирован и аутентифицирован).
                .and()
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/admin", true)
                .successHandler(successUserHandler)//настраивается форма входа для пользователей с помощью метода formLogin(). Метод successHandler() указывает обработчик успешной аутентификации (successUserHandler).
                .failureUrl("/auth/login?error");
                //.permitAll()//разрешает доступ всем пользователям к форме входа.
                //.and()
                //.logout()//настраивается возможность выхода из приложения
                //.permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }


}