package com.example.Member;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * This class is designed to manage the information for the Member while he is
 * logged in the service. This object can be used in any other @Component
 * auto-wiring it as usual.
 * 
 * Instances of this class are never sent to the Member in any REST endpoint. It
 * can hold sensible information that can not be known in the client. 
 * 
 * NOTE: This class is intended to be extended by developer adding new
 * attributes. Current attributes can not be removed because they are used in
 * authentication procedures.
 */

@Component
@SessionScope
public class MemberComponent {

	private Member Member;

	public Member getLoggedMember() {
		return Member;
	}

	public void setLoggedMember(Member Member) {
		this.Member = Member;
	}

	public boolean isLoggedMember() {
		return this.Member != null;
	}

}
