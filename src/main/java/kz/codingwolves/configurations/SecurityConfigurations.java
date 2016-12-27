package kz.codingwolves.configurations;

import kz.codingwolves.enums.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sagynysh on 12/21/16.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Autowired
    private JsonAuthenticationFilter jsonAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/register").anonymous()
                .antMatchers(HttpMethod.POST,"/login").anonymous()
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint((HttpServletRequest request,
                                                                     HttpServletResponse response, AuthenticationException authException) -> {
                    response.setStatus(401);
                    response.getWriter().write(Messages.login.toString());
                }).accessDeniedHandler((HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) -> {
                    response.setStatus(403);
                    response.getWriter().write(Messages.forbidden.toString());
                }).and().addFilterBefore(jsonAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout().logoutUrl("/logout").logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(200);
                    response.getWriter().write(Messages.success.toString());
                });
    }
}
