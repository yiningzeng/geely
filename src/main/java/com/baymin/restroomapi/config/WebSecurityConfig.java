package com.baymin.restroomapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/images/**");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/druid/**");


        //region swagger-ui
        web.ignoring().antMatchers("/swagger-ui.html");
        web.ignoring().antMatchers("/webjars/springfox-swagger-ui/**");
        web.ignoring().antMatchers("/v2/api-docs");
        web.ignoring().antMatchers("/swagger-resources/**");
        //endregion

        web.ignoring().antMatchers("/api/**");
        web.ignoring().antMatchers("/test/**");
        web.ignoring().antMatchers("/login");
        web.ignoring().antMatchers("/ip");
        web.ignoring().antMatchers("/**.html", "/**.css", "/img/**", "/**.js", "/third-party/**");

//        web.ignoring().antMatchers("/**");
    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .formLogin().loginPage("/login.html").loginProcessingUrl("/login").permitAll()
//                .and()
//                .authorizeRequests()
//                .antMatchers("/login.html",
//                        "/**/*.css",
//                        "/img/**",
//                        "/assets/**",
//                        "/**/heapdump",
//                        "/**/loggers",
//                        "/**/liquibase",
//                        "/**/logfile",
//                        "/**/flyway",
//                        "/**/auditevents",
//                        "/**/jolokia",
//                        "/api/**").permitAll()
//                .and()
//                .authorizeRequests()
//                .antMatchers("/**").hasRole("USER")
//                .antMatchers("/**").authenticated()
//                .and()
//                .logout().deleteCookies("remove").logoutSuccessUrl("/login.html").permitAll()
//                .and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class)
//                ;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login.html").loginProcessingUrl("/login").permitAll().defaultSuccessUrl("/").and()
        .logout().deleteCookies("remove").logoutSuccessUrl("/login.html").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/apiapplications/").permitAll()
                .antMatchers("/health").permitAll().anyRequest().authenticated()
                .and().csrf().ignoringAntMatchers("/api/**", "/**").csrfTokenRepository(csrfTokenRepository())
                .and()
//                .addFilterBefore(new TokenAuthorFilter(),Filter.class)
                .addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
    }



    private OncePerRequestFilter csrfHeaderFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                if (csrf != null) {
                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                    String token = csrf.getToken();
                    if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                        cookie = new Cookie("XSRF-TOKEN", token);
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }




    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}
