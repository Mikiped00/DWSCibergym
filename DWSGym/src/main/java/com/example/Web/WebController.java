package com.example.Web;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Member.MemberComponent;
import com.example.Repository_Service.LessonService;
import com.example.Repository_Service.MemberService;

@Controller
public class WebController {
	
	@Autowired
	private MemberComponent memberComponent;
	
	@Autowired
	private LessonService lessonService;
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/")
	public String index(Model model, HttpServletRequest request, @AuthenticationPrincipal OAuth2User principal) {
		if (request.getUserPrincipal() == null) { //If it's an anonymous user
			model.addAttribute("name", "");
		} else {
			if (principal == null) { //If it has logged with database params, it's already in memberComponent
				model.addAttribute("name", memberComponent.getLoggedMember().getName());
			} else { // If it has logged with OAuth2
				//CAMPO authorizedClientRegistrationId de request.getUserPrincipal() contiene el servicio usado para oauth2
				String service = ((OAuth2AuthenticationToken) request.getUserPrincipal()).getAuthorizedClientRegistrationId();
				String username;
				String email = principal.getAttribute("email");
				if(service.equals("google")) {
					username = principal.getAttribute("given_name"); //GOOGLE
				} else {
					username = principal.getAttribute("login"); //GITHUB
				}
				if (this.memberService.getMemberByEmail(email).getStatusCode().equals(HttpStatus.NOT_FOUND)) {
					model.addAttribute("username", username);
					model.addAttribute("email", email);
					return "springmvc/member/autoregisterMember.html";
				} else {
					memberComponent.setLoggedMember(this.memberService.getMemberByEmail(email).getBody());
					model.addAttribute("name", memberComponent.getLoggedMember().getName());
				}
			}
		}
		model.addAttribute("registeredLessons", this.lessonService.getAllLessons());
		model.addAttribute("logged", request.getUserPrincipal() != null);
		model.addAttribute("notLogged", request.getUserPrincipal() == null);
		return "CiberGym";
	}
	 
	 @RequestMapping("/login.html")
	 public String login(Model model) {
		if (memberComponent.getLoggedMember() == null) {
			model.addAttribute("name", "");
		} else {
			model.addAttribute("name", memberComponent.getLoggedMember().getName());
		}
	    return "login.html";
	 }

	 // Login form with error
	 @RequestMapping("/login-error")
	 public String loginError(Model model) {
		 model.addAttribute("loginError", true);
		 return "login.html";
	 }
	 
	 //Method to get CiberGymSpringMVC.html
	 @RequestMapping("/menuMVC")
	 public String menuMVC(Model model, HttpServletRequest request) {
		 model.addAttribute("admin",request.isUserInRole("ADMIN"));
		 model.addAttribute("user",request.isUserInRole("USER"));
		 return "CiberGymSpringMVC";
	 }
	 
	 //Method to get CiberGymRest.html
	 @RequestMapping("/menuAPI")
	 public String menuAPI(Model model, HttpServletRequest request) {
		 model.addAttribute("admin",request.isUserInRole("ADMIN"));
		 model.addAttribute("user",request.isUserInRole("USER"));
		 return "CiberGymRest";
	 }
	 
	 //Aux method to get a button route
	 @RequestMapping("/route")
	 public String route(Model model) {
		 // memberComponent must have a logged user
		 if (memberComponent.getLoggedMember().getRoles().contains("ROLE_USER")) {
			 return "redirect:menuAPI";
		 } else if (memberComponent.getLoggedMember().getRoles().contains("ROLE_ADMIN")) {
			 return "redirect:/api/LessonRest.html";
		 } else { // It shouldn't enter here, users must be always logged in to call this method
			model.addAttribute("action", "getting the desired page");
			model.addAttribute("error", "You don't have permission to go there.");
			model.addAttribute("status", HttpStatus.FORBIDDEN.toString());
			model.addAttribute("route", "/");
			return "springmvc/errorMVC";
		 }
	 }
	 
}

