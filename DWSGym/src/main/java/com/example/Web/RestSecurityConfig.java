package com.example.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(1)
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public MemberRepositoryAuthenticationProvider userRepoAuthProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
				
		http.antMatcher("/CiberGym/api/**");
		
		// URLs that need authentication to access to it
		//http.authorizeRequests().antMatchers("/api/MemberRest.html").hasRole("ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/CiberGym/api/member/").hasRole("ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/CiberGym/api/member/**").hasAnyRole("ADMIN", "USER");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/CiberGym/api/lesson/**").hasAnyRole("ADMIN", "USER");

		http.authorizeRequests().antMatchers(HttpMethod.POST, "/CiberGym/api/member/").hasRole("ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/CiberGym/api/member/**").hasAnyRole("ADMIN", "USER");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/CiberGym/api/lesson/").hasRole("ADMIN");

		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/CiberGym/api/member/").hasAnyRole("ADMIN", "USER");
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/CiberGym/api/lesson/**").hasRole("ADMIN");

		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/CiberGym/api/member/cancelLesson/").hasAnyRole("USER", "ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/CiberGym/api/member/**").hasRole("ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/CiberGym/api/lesson/**").hasRole("ADMIN");		

		// Other URLs can be accessed without authentication
		http.authorizeRequests().anyRequest().authenticated();

		// Disable CSRF protection (it is difficult to implement in REST APIs)
		http.csrf().disable();

		// Use Http Basic Authentication
		http.httpBasic();

		// Do not redirect when logout
		http.logout().logoutSuccessHandler((rq, rs, a) -> {	});
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		// Database authentication provider
		auth.authenticationProvider(userRepoAuthProvider);
	}
}