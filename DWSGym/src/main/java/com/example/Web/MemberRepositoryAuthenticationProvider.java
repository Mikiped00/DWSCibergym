package com.example.Web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.Member.Member;
import com.example.Member.MemberComponent;
import com.example.Repository_Service.MemberRepository;

@Component
public class MemberRepositoryAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private MemberComponent memberComponent;

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {

		Member member = memberRepository.findByEmail(auth.getName());

		String password = (String) auth.getCredentials();
		if (member == null || !new BCryptPasswordEncoder().matches(password, member.getPasswordHash())) {
			throw new BadCredentialsException("Wrong credentials");
		}
		List<GrantedAuthority> roles = new ArrayList<>();
		for (String role : member.getRoles()) {
			roles.add(new SimpleGrantedAuthority(role));
		}
		memberComponent.setLoggedMember(member);
		return new UsernamePasswordAuthenticationToken(member.getName(), password, roles);

	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return true;
	}

}