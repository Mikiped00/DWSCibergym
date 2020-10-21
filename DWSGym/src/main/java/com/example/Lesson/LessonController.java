package com.example.Lesson;

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

import com.example.Member.Member;
import com.example.Member.MemberComponent;
import com.example.Repository_Service.LessonService;

@Controller
public class LessonController {
	
	@Autowired
	private LessonService lessonService;
	
	@Autowired
	private MemberComponent memberComponent;
	
	// Auxiliary method to inject crsf token in template
	@RequestMapping("/auxAddLesson")
	public String auxAddLesson() {
		return "springmvc/lesson/addLessonForm.html";
	}
	
	//Method to add Lesson through the MVC form
	@RequestMapping("/addLesson")
	public String addLesson (Model model, @RequestParam String newLessonName, @RequestParam String newLessonDescription, @RequestParam int newLessonPlaces, @RequestParam int newBegginingHour, @RequestParam int newBegginingMinutes, @RequestParam int newEndingHour, @RequestParam int newEndingMinutes) {
		Lesson newLesson = new Lesson(newLessonName, newLessonDescription, newLessonPlaces, newBegginingHour, newBegginingMinutes, newEndingHour, newEndingMinutes);
		ResponseEntity<Lesson> lResponseEntity = this.lessonService.newLesson(newLesson);
		if (lResponseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
			model.addAttribute("action", "adding the desired lesson");
			model.addAttribute("success", "The lesson was added to the database");
			model.addAttribute("route", "/springmvc/lessonMVC.html");
			return "springmvc/successMVC";
		} else {
			model.addAttribute("action", "adding the desired lesson");
			model.addAttribute("error", "You are trying a XSS attack. Behave properly.");
			model.addAttribute("status", lResponseEntity.getStatusCode().toString());
			model.addAttribute("route", "/springmvc/lessonMVC.html");
			return "springmvc/errorMVC";
		}
	}
	
	//Method to get all the Lessons through the MVC form
	@RequestMapping("/getLessons")
	public String getLessons (Model model) {
		if (this.lessonService.getAllLessons().isEmpty()) {
			model.addAttribute("action", "obtaining lessons");
			model.addAttribute("error", "Any lesson was found in our database.");
			model.addAttribute("status", HttpStatus.NO_CONTENT.toString());
			model.addAttribute("route", "/springmvc/lessonMVC.html");
			return "springmvc/errorMVC";
		} else {
			model.addAttribute("admin", this.memberComponent.getLoggedMember().getRoles().contains("ROLE_ADMIN"));
			model.addAttribute("user", this.memberComponent.getLoggedMember().getRoles().contains("ROLE_USER"));
			model.addAttribute("registeredLessons", this.lessonService.getAllLessons());
			return "springmvc/lesson/getLessons";
		}
	}
	
	//Method to get a specific Lesson through the MVC form
	@RequestMapping("/getLesson/{getLessonId}")
	public String getLesson (Model model, @PathVariable long getLessonId) {
		ResponseEntity<Lesson> lResponseEntity = this.lessonService.getLesson(getLessonId);
		if (lResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
			Lesson registeredLesson = lResponseEntity.getBody();
			List<Lesson> auxList = new ArrayList<>();
			auxList.add(registeredLesson);
			model.addAttribute("registeredLessons", auxList);
			return "springmvc/lesson/getOneLesson.html";
		} else {
			model.addAttribute("action", "obtaining the desired lesson");
			model.addAttribute("error", "The lesson introduced is not included in our database.");
			model.addAttribute("status", lResponseEntity.getStatusCode().toString());
			model.addAttribute("route", "/springmvc/lessonMVC.html");
			return "springmvc/errorMVC";
		}
	}
		
	//Method to modify a specific Lesson through the MVC form
	@RequestMapping("/modifyLesson")
	public String modifyLesson (Model model, @RequestParam long updateLessonId, @RequestParam String updateLessonName, @RequestParam String updateLessonDescription, @RequestParam Integer updateLessonPlaces, @RequestParam Integer updateBegginingHour, @RequestParam Integer updateBegginingMinutes, @RequestParam Integer updateEndingHour, @RequestParam Integer updateEndingMinutes) {
		ResponseEntity<Lesson> lResponseEntity = this.lessonService.updateLesson(updateLessonId, updateLessonName,updateLessonDescription,updateLessonPlaces,updateBegginingHour, updateBegginingMinutes, updateEndingHour, updateEndingMinutes);
		if (lResponseEntity.getStatusCode().equals(HttpStatus.NOT_ACCEPTABLE)) {
			model.addAttribute("action", "modifying the desired lesson");
			model.addAttribute("error", "All inputs are empty.");
			model.addAttribute("status", lResponseEntity.getStatusCode().toString());
			model.addAttribute("route", "/getLessons");
			return "springmvc/errorMVC";
		} else if (lResponseEntity.getStatusCode().equals(HttpStatus.NOT_MODIFIED)) {
			model.addAttribute("action", "modifying the desired lesson");
			model.addAttribute("error", "You are trying a XSS attack. Behave properly.");
			model.addAttribute("status", lResponseEntity.getStatusCode().toString());
			model.addAttribute("route", "/getLessons");
			return "springmvc/errorMVC";
		} else {
			model.addAttribute("action", "modifying the desired lesson");
			model.addAttribute("success", "The lesson was modified in our database");
			model.addAttribute("route", "/getLessons");
			return "springmvc/successMVC";		}
	}
	
	// Auxiliary method to inject crsf token and lesson Id in template
	@RequestMapping("/auxModifyLesson/{id}")
	public String auxModifyLesson(Model model, @PathVariable long id) {
		model.addAttribute("id", id);
		return "springmvc/lesson/modifyLessonForm.html";
	}
	
	//Method to delete a specific Lesson through the MVC form
	@RequestMapping("/deleteLesson/{deleteLessonId}")
	public String deleteLesson (Model model, @PathVariable long deleteLessonId) {
		ResponseEntity<Lesson> lResponseEntity = this.lessonService.deleteLesson(deleteLessonId);
		if (lResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
			model.addAttribute("action", "deleting the desired lesson");
			model.addAttribute("success", "The lesson was deleted from our database");
			model.addAttribute("route", "/getLessons");
			return "springmvc/successMVC";
		} else {
			model.addAttribute("action", "deleting the desired lesson");
			model.addAttribute("error", "The lesson introduced is not included in our database.");
			model.addAttribute("status", lResponseEntity.getStatusCode().toString());
			model.addAttribute("route", "/getLessons");
			return "springmvc/errorMVC";
		}
	}
	
	//Method to show the reservations of a specific Lesson through the MVC form
	@RequestMapping("/getReservedMembers/{membersByLessonId}")
	public String getReservedMembers (Model model, @PathVariable long membersByLessonId) {
		ResponseEntity<Collection<Member>> lResponseEntity = this.lessonService.getReservedMembers(membersByLessonId);
		if (lResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
			Collection<Member> reservedMembers = lResponseEntity.getBody();
			model.addAttribute("registeredMembers", reservedMembers);
			model.addAttribute("route","/getLessons");
			return "springmvc/member/getOneMember";
		} else if(lResponseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
			model.addAttribute("action", "getting the members of the desired lesson");
			model.addAttribute("error", "The lesson introduced is not included in our database.");
			model.addAttribute("status", lResponseEntity.getStatusCode().toString());
			model.addAttribute("route", "/springmvc/lessonMVC.html");
			return "springmvc/errorMVC";
		} else {
			model.addAttribute("action", "getting the members of the desired lesson");
			model.addAttribute("error", "There are no members registered for this lesson.");
			model.addAttribute("status", lResponseEntity.getStatusCode().toString());
			model.addAttribute("route", "/getLessons");
			return "springmvc/errorMVC";
		}
			
	}
	
	//FILTERS
	
	@RequestMapping("/lessonFilter")
	public String lessonFilters(Model model,@RequestParam String name, @RequestParam  Integer placesUnder, @RequestParam Integer placesOver, @RequestParam Integer begginingHourBefore, @RequestParam Integer begginingHourAfter, @RequestParam Integer endingHourBefore,  @RequestParam Integer endingHourAfter) {
		List<Lesson> filteredLessons = this.lessonService.generalFilters(name, placesUnder, placesOver, begginingHourBefore, begginingHourAfter, endingHourBefore, endingHourAfter);
		model.addAttribute("filteredLessons", filteredLessons);
		model.addAttribute("showResults", true);
		return "springmvc/lesson/lessonFilterForm.html";
	}
	
	//Aux method to generate Thymeleaf template with csrf token injected
	@RequestMapping("/auxLessonFilters")
	public String auxLessonFilters(Model model) {
		model.addAttribute("showResults", false);
		model.addAttribute("filteredLessons", new ArrayList<Member>());
		return "springmvc/lesson/lessonFilterForm.html";
	}
	
}
