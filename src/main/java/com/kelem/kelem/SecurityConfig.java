package com.kelem.kelem;

import com.kelem.kelem.dao.UserRepository;
import com.kelem.kelem.services.MyUserDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Our securiy configuration file.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    CustomLoginSuccessHandler customLoginSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // the auth is injected by spring boot context
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // the http is parameter is injected by spring boot context
        http.csrf().disable()
                .authorizeRequests()
                // may or may not have helped with the static file
                .antMatchers("/resources/**").permitAll()
                // definetly helped with the static files.
                .antMatchers("/**/*.js", "/**/*.css", "/**/*.otf").permitAll()
                .antMatchers("/admin", "/add-admin", "/reported-answers", "/resolve-report/{id}",
                             "/dismiss-report/{id}", "/reported-questions", "/ques-resolve-report/{id}",
                             "/ques-dismiss-report/{id}", "/add-quiz").hasRole("ADMIN")
                .antMatchers("/user-photos/**/*", "/delete-userProfile/*").hasAnyRole("ADMIN", "MEMBER") // find better way
                .antMatchers("/reported-questions").hasRole("ADMIN")
                .antMatchers("/currently-registered-user/*").hasAnyRole("ADMIN", "MEMBER") // find better way
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/**").hasRole("MEMBER")
                .antMatchers("/currently-registered-user/**").hasRole("MEMBER")
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(customLoginSuccessHandler)
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login");

    }

    @Bean
    PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return new MyUserDetailService();
    }

    // may or may not have helped with the static files
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**");
    }

}
