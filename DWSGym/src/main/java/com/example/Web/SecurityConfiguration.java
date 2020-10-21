package com.example.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	public MemberRepositoryAuthenticationProvider authenticationProvider;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		 // Public pages
		 http.authorizeRequests().antMatchers("/").permitAll();
		 http.authorizeRequests().antMatchers("/util/**").permitAll();
		 http.authorizeRequests().antMatchers("/login.html").permitAll();
		 http.authorizeRequests().antMatchers("/login-error").permitAll();
		 http.authorizeRequests().antMatchers("/logout").permitAll();
		 http.authorizeRequests().antMatchers("/auxLessonFilters").permitAll();
		 http.authorizeRequests().antMatchers("/lessonFilter").permitAll();
		 http.authorizeRequests().antMatchers("/about.html").permitAll();
		 
		 // Private pages (all other pages)
		 // MVC PAges
		 http.authorizeRequests().antMatchers("/springmvc/**").hasAnyRole("ADMIN");
		 http.authorizeRequests().antMatchers("/auxAddMember").hasAnyRole("ADMIN");
		 http.authorizeRequests().antMatchers("/getMembers").hasAnyRole("ADMIN");
		 http.authorizeRequests().antMatchers("/auxAddLesson").hasAnyRole("ADMIN");
		 http.authorizeRequests().antMatchers("/deleteMember/**").hasAnyRole("ADMIN");
		 http.authorizeRequests().antMatchers("/deleteLesson/**").hasAnyRole("ADMIN");
		 http.authorizeRequests().antMatchers("/auxModifyLesson/**").hasAnyRole("ADMIN");
		 http.authorizeRequests().antMatchers("/modifyLesson").hasAnyRole("ADMIN");
		 
		 // REST API Pages
		 http.authorizeRequests().antMatchers("/api/MemberRest.html").hasRole("ADMIN");
		 http.authorizeRequests().antMatchers("/api/member/postMemberRest.html").hasRole("ADMIN");
		 http.authorizeRequests().antMatchers("/api/lesson/postLessonRest.html").hasRole("ADMIN");
		 http.authorizeRequests().antMatchers("/api/lesson/putLessonRest.html").hasRole("ADMIN");

		 http.authorizeRequests().anyRequest().authenticated();
		 
		 // Login form
		 http
	        .formLogin()
	        .loginPage("/login.html")
	        .usernameParameter("email")
	        .defaultSuccessUrl("/")
	        .failureUrl("/login-error")
	      .and()
	        .logout()
	        .logoutUrl("/logout")
	        .logoutSuccessUrl("/")
		 .and()
		 	.oauth2Login();
		 
		 }
	 
	  @Override
	  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	
	        auth.authenticationProvider(authenticationProvider);
	  }

}

