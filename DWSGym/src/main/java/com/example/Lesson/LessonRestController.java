package com.example.Lesson;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Member.Member;
import com.example.Repository_Service.LessonService;

@RestController
@RequestMapping("/CiberGym/api/")
public class LessonRestController {
	
	@Autowired
	private LessonService lessonService;
	
	@PostConstruct
	public void init() {
		this.lessonService.initLesson();
	}
	
	//Method to add a Lesson with the API Rest
	@PostMapping("/lesson/")
	public ResponseEntity<Lesson> newLesson(@RequestBody Lesson lesson ) { 
		return this.lessonService.newLesson(lesson);
	}
	
	//Method to get all the available Lessons with the API Rest
	@GetMapping("/lesson/")
	public ResponseEntity<Collection<Lesson>> getAllLessons(HttpServletRequest request){
		if (request.isUserInRole("ROLE_ADMIN")) {
			return new ResponseEntity<>(this.lessonService.getAllLessons(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(this.lessonService.getAllLessons(), HttpStatus.ACCEPTED);
		}
	}
	
	//Method to get a specific Lesson with the API Rest
	@GetMapping("/lesson/{id}")
	public ResponseEntity<Lesson> getLesson(@PathVariable long id) {
		return this.lessonService.getLesson(id);
	}
	
	//Method to modify a specific Lesson with the API Rest
	@PutMapping("/lesson/{id}")
	public ResponseEntity<Lesson> updateLesson(@PathVariable long id, @RequestBody Lesson updatedLesson) { 
		return this.lessonService.updateLesson(id, updatedLesson.getName(),updatedLesson.getDescription(),updatedLesson.getAvailablePlaces(),updatedLesson.getBegginingHour(), updatedLesson.getBegginingMinutes(),updatedLesson.getEndingHour(),updatedLesson.getEndingMinutes());
	}
	
	//Method to delete a specific Lesson with the API Rest
	
	@DeleteMapping("/lesson/{id}")
	public ResponseEntity<Lesson> deleteLesson(@PathVariable long id) { 
		return this.lessonService.deleteLesson(id);
		
	}
	
	//Method to get the members of a specific Lesson with the API Rest
	@GetMapping("/lesson/{id}/getReservedMember/")
	public ResponseEntity<Collection<Member>> getReservedMembers(@PathVariable long id){
		return this.lessonService.getReservedMembers(id);
	}

	//FILTERS
	
	@GetMapping("/lesson/LessonFilters")
	public List<Lesson> lessonFilters(@RequestParam String name, @RequestParam Integer placesUnder, @RequestParam Integer placesOver, @RequestParam Integer begginingHourBefore, @RequestParam Integer begginingHourAfter, @RequestParam Integer endingHourBefore,  @RequestParam Integer endingHourAfter) {
		return this.lessonService.generalFilters(name, placesUnder, placesOver, begginingHourBefore, begginingHourAfter, endingHourBefore, endingHourAfter);
		
	}
}
