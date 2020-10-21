package com.example.Member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Lesson.Lesson;
import com.example.Repository_Service.MemberService;

@Controller
public class MemberController {

	//@Autowired connects with @Bean in DwsGymApplication.java to initialize values
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MemberComponent memberComponent;
	
	@RequestMapping("/auxAddMember")
	public String auxAddMember() {
		return "springmvc/member/addMemberForm.html";
	}
	
	//Method to add a Member with the MVC form
	@RequestMapping("/addMember")
	public String addMember (Model model, @RequestParam String newMemberDni, @RequestParam String newMemberName, @RequestParam String newMemberEmail, @RequestParam String newMemberpass, @RequestParam String... newMemberRol) {
		Member newMember = new Member(newMemberDni, newMemberName, newMemberEmail, newMemberpass, newMemberRol);
		ResponseEntity<Member> responseEntity = this.memberService.newMember(newMember);
		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			model.addAttribute("action", "adding the desired member");
			model.addAttribute("success", "The member was added to our database");
			model.addAttribute("route", "/springmvc/memberMVC.html");
			return "springmvc/successMVC";
		} else {
			model.addAttribute("action", "adding member");
			model.addAttribute("error", "The email is already taken.");
			model.addAttribute("status", responseEntity.getStatusCode().toString());
			model.addAttribute("route", "/springmvc/memberMVC.html");
			return "springmvc/errorMVC";
		}
		
	}
	
	//Method to autoRegister a Member with the MVC form and OAuth2
	@RequestMapping("/autoRegister")
	public String autoRegister (Model model, @RequestParam String newMemberDni, @RequestParam String newMemberName, @RequestParam String newMemberEmail, @RequestParam String newMemberpass) {
		Member newMember = new Member(newMemberDni, newMemberName, newMemberEmail, newMemberpass, "ROLE_USER");
		ResponseEntity<Member> responseEntity = this.memberService.newMember(newMember);
		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			model.addAttribute("action", "adding the desired member");
			model.addAttribute("success", "The member was added to our database");
			model.addAttribute("route", "/");
			memberComponent.setLoggedMember(newMember); // To set logged user
			return "springmvc/successMVC";
		} else { //It should never enter here, because it's already checked in the mainPage ("/"), and the email can't be modified in the form
			model.addAttribute("action", "adding member");
			model.addAttribute("error", "The email is already taken.");
			model.addAttribute("status", responseEntity.getStatusCode().toString());
			model.addAttribute("route", "/");
			return "springmvc/errorMVC";
		}
		
	}
	
	//Method to get all the Members with the MVC form
	@RequestMapping("/getMembers")
	public String getMembers (Model model) {
		List<Member> memberServiceList = this.memberService.getAllMembers();
		if (memberServiceList.isEmpty()) {
			model.addAttribute("action", "obtaining members");
			model.addAttribute("error", "Any member was found in our database.");
			model.addAttribute("status", HttpStatus.NOT_FOUND.toString());
			model.addAttribute("route", "/springmvc/memberMVC.html");
			return "springmvc/errorMVC";
		} else {
			model.addAttribute("registeredMembers", memberServiceList);
			return "springmvc/member/getMembers";
		}
	}
	
	//Method to generate member profile
	@RequestMapping("/showProfile")
	public String showProfile(Model model) {		
		 Member member = memberComponent.getLoggedMember();
		 model.addAttribute("memberProfile",member);
		 if(member.getMembersLesson()==null) {
			 model.addAttribute("membersLessonName","No lesson");
		 } else {
			 model.addAttribute("membersLessonName", member.getMembersLesson().getName());
		}
		 model.addAttribute("route","/menuMVC");
		 return "springmvc/member/profile";
	}
	
	@RequestMapping("/auxChangePass")
	public String auxChangePassword() {
		return "springmvc/member/changePasswordForm.html";
	}
	
	@RequestMapping("/changePassword")
	public String changePassword (Model model, @RequestParam String newpassWord,@RequestParam String newpassWordRepeted) {
		Member member = this.memberComponent.getLoggedMember();
		ResponseEntity<Member> responseEntity = this.memberService.changePassword(member.getId(), newpassWord, newpassWordRepeted);
		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			model.addAttribute("action", "changing password");
			model.addAttribute("success","The password was changed successfully.");
			model.addAttribute("route","/showProfile");
			return "springmvc/successMVC";
		} else if (responseEntity.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
			model.addAttribute("action","changing password.");
			model.addAttribute("error", "Check your inputs.");
			model.addAttribute("status", responseEntity.getStatusCode().toString());
			model.addAttribute("route", "/showProfile");
			return "springmvc/errorMVC";
		} else {
			model.addAttribute("action","changing password.");
			model.addAttribute("error", "The passwords do not match");
			model.addAttribute("status", responseEntity.getStatusCode().toString());
			model.addAttribute("route", "/showProfile");
			return "springmvc/errorMVC";
		}
	}

	//Method to get a specific Member with the MVC form
	@RequestMapping("/getMember/{getMemberId}")
	public String getMember (Model model, @PathVariable long getMemberId) {
		ResponseEntity<Member> registeredMember = this.memberService.getMember(getMemberId);
		if (registeredMember.hasBody()) {
			List<Member> auxList = new ArrayList<>();
			auxList.add(registeredMember.getBody());
			model.addAttribute("registeredMembers",auxList);
			model.addAttribute("route","/getMembers");
			return "springmvc/member/getOneMember";
		} else {
			model.addAttribute("action", "obtaining the desired member");
			model.addAttribute("error", "The member required was not found in our database.");
			model.addAttribute("status", registeredMember.getStatusCode().toString());
			model.addAttribute("route", "/getMembers");
			return "springmvc/errorMVC";
		}
	}
	
	//Method to modify a specific Member with the MVC form
	@RequestMapping("/modifyMember")
	public String modifyMember (Model model, @RequestParam String updateMemberDni, @RequestParam String updateMemberName) {
		long id = memberComponent.getLoggedMember().getId();
		ResponseEntity<Member> lResponseEntity = this.memberService.updateMember(id, updateMemberDni, updateMemberName);
		if (lResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
			model.addAttribute("action", "modifying the desired member");
			model.addAttribute("success", "The member was modified in our database");
			model.addAttribute("route", "/showProfile");
			return "springmvc/successMVC";
		} else if (lResponseEntity.getStatusCode().equals(HttpStatus.NOT_MODIFIED)) {
			model.addAttribute("action", "modifing the desired member");
			model.addAttribute("error", "All inputs are empty");
			model.addAttribute("status", lResponseEntity.getStatusCode().toString());
			model.addAttribute("route", "/auxModifyMember");
			return "springmvc/errorMVC";
		} else { //It shouldn't enter here, because members should always exist as it's logged.
			model.addAttribute("action", "modifing the desired member");
			model.addAttribute("error", "The member required was not found in our database.");
			model.addAttribute("status", lResponseEntity.getStatusCode().toString());
			model.addAttribute("route", "/menuMVC");
			return "springmvc/errorMVC";
		}
	}
	
	@RequestMapping("/auxModifyMember")
	public String auxModifyMember(Model model) {
		return "springmvc/member/modifyMemberForm.html";
	}
	
	//Method to delete a specific Member with the MVC form
	@RequestMapping("/deleteMember/{deleteMemberId}")
	public String deleteMember (Model model, @PathVariable long deleteMemberId) {
		ResponseEntity<Member> lResponseEntity = this.memberService.deleteMember(deleteMemberId);
		if (lResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
			model.addAttribute("action", "deleting the desired lesson");
			model.addAttribute("success", "The member was deleted from our database");
			model.addAttribute("route", "/getMembers");
			return "springmvc/successMVC";
		} else { //It shouldn't enter here, because members should always exist as it's in the page.
			model.addAttribute("action", "deleting the desired member");
			model.addAttribute("error", "The member required was not found in our database.");
			model.addAttribute("status", lResponseEntity.getStatusCode().toString());
			model.addAttribute("route", "/getMembers");
			return "springmvc/errorMVC";
		}
	}
	
	//Method to add a reservation of a Lesson with the MVC form. Redirects to LessonController.java
	@RequestMapping("/reserveLesson")
	public String reserveLesson(Model model, @RequestParam long reserveLessonId) {
		ResponseEntity<Member> responseEntity = this.memberService.addReserve(this.memberComponent.getLoggedMember().getId(), reserveLessonId);
		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			model.addAttribute("action", "adding the reservation");
			model.addAttribute("success", "The reservation was added to our database");
			model.addAttribute("route", "/showProfile");
			return "springmvc/successMVC";
		} else if (responseEntity.getStatusCode().equals(HttpStatus.INSUFFICIENT_STORAGE)) {
			model.addAttribute("action", "reserving the desired lesson");
			model.addAttribute("error", "The lesson does not have more available places.");
			model.addAttribute("status", responseEntity.getStatusCode().toString());
			model.addAttribute("route", "/showProfile");
			return "springmvc/errorMVC";
		} else if (responseEntity.getStatusCode().equals(HttpStatus.TOO_MANY_REQUESTS)){
			model.addAttribute("action", "reserving the desired lesson");
			model.addAttribute("error", "The member has already reserved a lesson");
			model.addAttribute("status", responseEntity.getStatusCode().toString());
			model.addAttribute("route", "/showProfile");
			return "springmvc/errorMVC";		
		} else {
			model.addAttribute("action", "reserving the desired lesson");
			model.addAttribute("error", "The member is not included in our database.");
			model.addAttribute("status", responseEntity.getStatusCode().toString());
			model.addAttribute("route", "/showProfile");
			return "springmvc/errorMVC";	
		}
	}
	
	@RequestMapping("/auxReserveLesson")
	public String auxReserveLesson(Model model) {
		long id = this.memberComponent.getLoggedMember().getId();
		ResponseEntity<Member> responseEntity = this.memberService.getMember(id);
		Lesson auxReservedLesson = responseEntity.getBody().getMembersLesson();
		if (auxReservedLesson == null) {
			model.addAttribute("registeredLesson", this.memberService.getAllLessons());
			return "springmvc/member/reserveLessonForm.html";
		} else {
			model.addAttribute("action", "reserving the desired lesson");
			model.addAttribute("error", "The member has already reserved the lesson: " + auxReservedLesson.getName());
			model.addAttribute("status", HttpStatus.TOO_MANY_REQUESTS.toString());
			model.addAttribute("route", "/showProfile");
			return "springmvc/errorMVC";		
		}
	}
	
	
	//Method to delete a reservation of a Lesson with the MVC form. Redirects to LessonController.java
	@RequestMapping("/cancelLesson")
	public String cancelLesson(Model model) {
		ResponseEntity<Member> canceledResponseEntity = this.memberService.cancelReserve(this.memberComponent.getLoggedMember().getId());
		if (canceledResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
			model.addAttribute("action", "deleting the reservation");
			model.addAttribute("success", "The reservation for the lesson was deleted from our database");
			model.addAttribute("route", "/showProfile");
			return "springmvc/successMVC";
		} else if (canceledResponseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
			model.addAttribute("action", "canceling the desired reservation");
			model.addAttribute("error", "The member did not reserve any lesson");
			model.addAttribute("status", canceledResponseEntity.getStatusCode().toString());
			model.addAttribute("route", "/showProfile");
			return "springmvc/errorMVC";
		} else {
			model.addAttribute("action", "canceling the desired reservation");
			model.addAttribute("error", "The member is not included in our database.");
			model.addAttribute("status", canceledResponseEntity.getStatusCode().toString());
			model.addAttribute("route", "/showProfile");
			return "springmvc/errorMVC";
		}
	} 	
	
	//FILTER by name beginning
	@RequestMapping("/membersByName")
	public String filterMembers(Model model, @RequestParam String name) {
		Collection <Member> filteredMembers = this.memberService.getBegginingMemberName(name);
		model.addAttribute("filteredMembers",filteredMembers);
		model.addAttribute("showResults", true);
		return "springmvc/member/memberFilterForm.html";
	}
	
	//Aux method to generate Thymeleaf template with csrf token injected
	@RequestMapping("/auxMemberFilter")
	public String auxMemberFilter(Model model) {
		model.addAttribute("showResults", false);
		model.addAttribute("filteredMembers", new ArrayList<Member>());
		return "springmvc/member/memberFilterForm.html";
	}
}
