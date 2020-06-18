package it.uniroma3.siw.taskmanager.authentication;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//import it.uniroma3.siw.taskmanager.model.Credentials;

@Configuration
@EnableWebSecurity
public class AuthConfiguration extends WebSecurityConfigurerAdapter {
	public static final String DEFAULT_ROLE="DEFAULT";
	public static final String ADMIN_ROLE="ADMIN";
	
	

	@Autowired
	DataSource datasource;

	/**
	 * policies di authentication e authorization
	 */
	protected void configure(HttpSecurity http) throws Exception {
		http
		//authorization paragraph: WHO can access WHICH pages
		.authorizeRequests() 
		//anyone (authenticated or not) can access  these pages
		.antMatchers(HttpMethod.GET, "/","/index","/login", "/users/register").permitAll()
		//anyone (authenticated or not) can access  these pages
		.antMatchers(HttpMethod.POST,"/login","/users/register").permitAll()
		//only authenticated users with ADMIN authority can access the admin page
		.antMatchers(HttpMethod.GET,"/admin/**").hasAnyAuthority(ADMIN_ROLE)
		.antMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(ADMIN_ROLE)
		//all authenticated users can access the other remaining pages
		.anyRequest().authenticated()
		//login paragraph
		.and().formLogin()
		//after login, redirect to home page
		.defaultSuccessUrl("/home")
		//logout paragraph
		.and().logout()
		.logoutUrl("/logout") //sends a GET to "/logout"
		.logoutSuccessUrl("/index") //after logout
		.invalidateHttpSession(true)
		.clearAuthentication(true).permitAll();
		
	}
	
	/**
	 * specifica il sistema, trova username,password,ruoli nel db
	 */
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
			//use autowired datasource to access saved credentials
			.dataSource(this.datasource)
			//retrieve username and role
			.authoritiesByUsernameQuery("SELECT username, role FROM credentials WHERE username=?")
			//retrieve username, password and a boolean flag specifying wheter the user is enabled or not (1 enabled)
			.usersByUsernameQuery("SELECT username,password, 1 as enabled FROM credentials WHERE username=?");
	}
	
	
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers("/css/**");
	}
	
	/**
	 * PasswordEncoder Bean is used to encrypt/decrypt the credentials password
	 * @return
	 */
	@Bean
	PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

	
}
