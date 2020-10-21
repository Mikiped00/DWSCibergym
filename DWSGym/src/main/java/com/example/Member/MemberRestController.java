package com.example.Member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.Repository_Service.MemberService;

@RestController
@RequestMapping("/CiberGym/api/")
public class MemberRestController {

	// @Autowired connects with @Bean in DwsGymApplication.java to initialize values
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberComponent memberComponent;
	
	@PostConstruct
	public void init() {
		this.memberService.initMember();
	}
	
	@RequestMapping("/member")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }
	
	// Method to add a Member with the API Rest
	@PostMapping("/member/")
	public ResponseEntity<Member> registerMember(@RequestBody Member member) {
		return this.memberService.newMember(member);
	}

	// Method to get all the Members with the API Rest
	@GetMapping("/member/")
	public Collection<Member> getAllMembers() {
		return memberService.getAllMembers();
	}

	// Method to get a specific Member with the API Rest
	@GetMapping("/member/{id}")
	public ResponseEntity<Member> getMember(@PathVariable long id) {
		ResponseEntity<Member> response = this.memberService.getMember(id);
		response.getBody().setPasswordHash("");
		response.getBody().setRoles(new ArrayList<String>());
		return response;
	}

	// Method to modify a specific Member with the API Rest
	@PutMapping("/member/")
	public ResponseEntity<Member> updateMember(@RequestBody Member updatedMember) {
		long id = memberComponent.getLoggedMember().getId();
		return this.memberService.updateMember(id, updatedMember.getDni(),updatedMember.getName());
	}

	// Method to delete a specific Member with the API Rest
	
	@DeleteMapping("/member/{id}")
	public ResponseEntity<Member> deleteMember(@PathVariable long id) {
		return this.memberService.deleteMember(id);
	}
	
	//Method to add a reservation of a Lesson with the MVC form. Redirects to LessonController.java
	@PostMapping("/member/reserveLesson/{lessonId}")
	public ResponseEntity<Member> reserveLesson(@PathVariable long lessonId) {
		long memberId = memberComponent.getLoggedMember().getId();
		return this.memberService.addReserve(memberId, lessonId);
	}
	
	//Method to delete a reservation of a Lesson with the MVC form. Redirects to LessonController.java
	@DeleteMapping("/member/cancelLesson/")
	public ResponseEntity<Member> cancelLesson() {
		long memberId = memberComponent.getLoggedMember().getId();
		return this.memberService.cancelReserve(memberId);
	}

	// Method to get a specific Member with the API Rest
	@GetMapping("/profile/")
	@ResponseStatus(HttpStatus.OK)
	public Member showProfile() {
		Member member = memberComponent.getLoggedMember();
		member.setPasswordHash("");
		member.setRoles(new ArrayList<String>());
		return member;
	}
	
	// Method to change the password
	@PatchMapping("/changePassword")
	public ResponseEntity<Member> changePassword(@RequestParam String newPass, @RequestParam String newPassRepeated) {
		long memberId = memberComponent.getLoggedMember().getId();
		return this.memberService.changePassword(memberId, newPass, newPassRepeated);
	}
	
	// FILTERS
	@GetMapping("/member/filterByBegginingName")
	public Collection<Member> getMemberByBegginingName(@RequestParam String name) {
		return this.memberService.getBegginingMemberName(name);
	}

}
