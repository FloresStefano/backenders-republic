package it.addvalue;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = false)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{

    /**
     * This section defines the user accounts which can be used for authentication as well as the roles each user has.
     * 
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
    }

    /**
     * This section defines the security policy for the app.
     * <p>
     * <ul>
     * <li>BASIC authentication is supported (enough for this REST-based demo).</li>
     * <li>/employees is secured using URL security shown below.</li>
     * <li>CSRF headers are disabled since we are only testing the REST interface, not a web one.</li>
     * </ul>
     * NOTE: GET is not shown which defaults to permitted.
     *
     * @param http
     * @throws Exception
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.authorizeRequests().//
        antMatchers(HttpMethod.POST, "/api/**").permitAll().//
        antMatchers(HttpMethod.GET, "/api/**").permitAll().and().//
        csrf().disable();
        
        http.httpBasic().disable();

    }
}