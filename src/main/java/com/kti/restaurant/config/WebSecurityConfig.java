package com.kti.restaurant.config;

import com.kti.restaurant.security.TokenUtils;
import com.kti.restaurant.security.auth.RestAuthenticationEntryPoint;
import com.kti.restaurant.security.auth.TokenAuthenticationFilter;
import com.kti.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    @Lazy
    private UserService jwtUserDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Autowired
    private TokenUtils tokenUtils;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()
        .authorizeRequests().antMatchers("/auth/").permitAll()
        .antMatchers("/socket/**").permitAll().
        antMatchers("/h2-console/").permitAll().
        antMatchers("/api/v1/auth/login").permitAll().
        antMatchers("/api/v1/auth/send-reset-password-link").permitAll().
        antMatchers("/api/v1/auth/reset-password").permitAll()
        .anyRequest().authenticated().and()
        .cors().and()
        .addFilterBefore(new TokenAuthenticationFilter(tokenUtils, jwtUserDetailsService),
                BasicAuthenticationFilter.class);

    	http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.POST, "/api/v1/auth/login");
        web.ignoring().antMatchers(HttpMethod.POST, "/api/v1/auth/send-reset-password-link");
        web.ignoring().antMatchers(HttpMethod.POST, "/api/v1/auth/reset-password");
        web.ignoring().antMatchers(HttpMethod.GET,"/", "/webjars/**", "/*.html", "/favicon.ico", "/**/*.html",
                "/**/*.css", "/**/*.js");
    }

}