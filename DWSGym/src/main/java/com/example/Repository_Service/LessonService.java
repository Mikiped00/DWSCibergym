package com.example.Repository_Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.Lesson.Lesson;
import com.example.Member.Member;

@Service
public class LessonService {

	@Autowired
	private LessonRepository lessonRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	/**
	 * Initializes the lessons in the repository
	 */
	public void initLesson() {
		if (this.lessonRepository.findAll().isEmpty()) {
			lessonRepository.save(new Lesson("Biceps", "En esta actividad se potencian los Biceps con el profesor Miguel Ángel Hernández. Nivel Avanzado", 25, 12, 00, 13, 00));
			lessonRepository.save(new Lesson("Triceps", "Aquí potenciaremos los triceps con el profesor Miguel Ángel Hernández. Nivel medio", 30, 15, 30, 16, 15));
			lessonRepository.save(new Lesson("AquaGym", "En esta actividad practicaremos para el público general un poco de movilidad en el agua. Impartido por Micael. Nivel principiante", 15, 15, 30, 16, 15));
			lessonRepository.save(new Lesson("Spinning", "El mejor entrenamiento para quemar esos kilitos de más que hemos cogido durante la cuarentena, a cargo del prof. Nicolás. Nivel muy alto", 35, 19, 30, 20, 45));
			lessonRepository.save(new Lesson("Pilates", "Equilibrio cuerpo-mente, estirando y reforzando la musculatura.", 50, 15, 30, 16, 15));
			lessonRepository.save(new Lesson("Body Pump", "Ejercicios de tonificación trabajando los principales grupos musculares.", 30, 16, 30, 17, 30));
			lessonRepository.save(new Lesson("Zumba", "El monitor Micael nos guiará en esta actividad de cardio que fortalece y da flexibilidad al cuerpo a través del baile.", 35, 18, 00, 19, 00));
			lessonRepository.save(new Lesson("GAP", "Entrenamientos específicos de glúteos, abdominales y piernas con ejercicios de fuerza y tonificación", 20, 11, 00, 11, 55));
			lessonRepository.save(new Lesson("Yoga", "Disciplina para dominar y conocer nuestro cuerpo, mejorando el equilibrio entre cuerpo y mente", 25, 12, 00, 12, 55));
		}
	}
	
	/**
	 * Method to get all the lessons with a TypedQuery
	 * @return List<Lesson>
	 */
	public List<Lesson> getAllLessons(){
		TypedQuery<Lesson> q1 = entityManager.createQuery("SELECT l FROM Lesson l", Lesson.class);
		return q1.getResultList();
	}
 	
	/**
	 * Method to add a lesson
	 * @param lesson
	 * @return the added lesson and HttpStatus.CREATED if lesson is added
	 * @return HttpStatus.NOT_ACCEPTABLE if the lesson's description has any suspicious text
	 */
	public ResponseEntity<Lesson> newLesson(Lesson lesson) {
		if (checkXSS(lesson.getDescription()).getStatusCode().equals(HttpStatus.ACCEPTED)) {
			this.lessonRepository.save(lesson);
			return new ResponseEntity<>(lesson, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	/**
	 * Method to get the lesson associated to the introduced @param id
	 * @param Lesson's id 
	 * @return selected Lesson and HttpStatus.OK if lesson exists
	 * @return HttpStatus.NOT_FOUND if lesson doesn't exist
	 */
	public ResponseEntity<Lesson> getLesson(long id){
		if(this.lessonRepository.existsById(id)) {
			Lesson lesson = this.lessonRepository.getOne(id);
			return new ResponseEntity<>(lesson, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Method to update the lesson associated to the introduced @param id
	 * If any field is blank, the previous value is kept
	 * @param Lesson's id 
	 * @param New name
	 * @param New description
	 * @param New available places
	 * @param New beginning hour
	 * @param New beginning minutes
	 * @param New ending hour
	 * @param New ending minutes
	 * @return updated Lesson and HttpStatus.OK if lesson already existed
	 * @return HttpStatus.NOT_MODIFIED if every input is blank
	 * @return HttpStatus.NOT_ACCEPTABLE if the new description contains suspicious text
	 */
	public ResponseEntity<Lesson> updateLesson(long id, String updateLessonName, String updateLessonDescription, Integer updateLessonPlaces, Integer updateBegginingHour, Integer updateBegginingMinutes, Integer updateEndingHour, Integer updateEndingMinutes){
		if (updateLessonName.equals("") && updateLessonDescription.equals("") && updateLessonPlaces.equals("") && updateBegginingHour.equals("") && updateBegginingMinutes.equals("") && updateEndingHour.equals("") && updateEndingMinutes.equals("")) {
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		} else {
			if (checkXSS(updateLessonDescription).getStatusCode().equals(HttpStatus.ACCEPTED)) {
				Lesson auxLesson = getLesson(id).getBody();
				if (!updateLessonName.equals("")) auxLesson.setName(updateLessonName); 
				if (!updateLessonDescription.equals(""))auxLesson.setDescription(updateLessonDescription); 
				if (updateLessonPlaces!=null)auxLesson.setAvailablePlaces((int)updateLessonPlaces); 
				if (updateBegginingHour!=null)auxLesson.setBegginingHour((int)updateBegginingHour);
				if (updateBegginingMinutes!=null)auxLesson.setBegginingMinutes((int)updateBegginingMinutes); 
				if (updateEndingHour!=null) auxLesson.setEndingHour((int)updateEndingHour); 
				if(updateEndingMinutes!=null)auxLesson.setEndingMinutes((int)updateEndingMinutes); 
				this.lessonRepository.saveAndFlush(auxLesson);
				return new ResponseEntity<>(auxLesson, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
			}
		}
	}
	
	/**
	 * Method to delete the lesson associated to the introduced @param id
	 * @param Lesson's id
	 * @return HttpStatus.OK if lesson existed
	 * @return HttpStatus.NOT_FOUND if lesson doesn't exist
	 */
	public ResponseEntity<Lesson> deleteLesson(long id){
		
		if(this.lessonRepository.existsById(id)) {
			this.lessonRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Method to get all the members with reservation for the introduced @param id
	 * @param Lesson's id
	 * @return Collection<Member> with all the members and HttpStatus.OK if lesson exists and there's any reservation
	 * @return HttpStatus.NO_CONTENT if lesson exists but there isn't any reservation
	 * @return HttpStatus.NOT_FOUND if lesson doesn't exist
	 */
	public ResponseEntity<Collection<Member>> getReservedMembers(long lessonId){
		if (this.lessonRepository.existsById(lessonId)) {
			Collection<Member> listCollection = new ArrayList<>();
			listCollection = this.lessonRepository.getOne(lessonId).getReservedMember();
			if (!listCollection.isEmpty()) {
				return new ResponseEntity<>(listCollection, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Method to check if a string contains any suspicious word, trying to attempt a XSS attack
	 * @param string
	 * @return HttpStatus.ACCEPTED if the string doesn't contain any suspicious word
	 * @return HttpStatus.NOT_ACCEPTABLE if it does
	 */
	public ResponseEntity<Lesson> checkXSS(String description) {
		if (description.contains("<script>") || description.contains("</script>") || 
				description.contains("&lt;script&gt;") || description.contains("&lt;/script&gt;") || description.contains("script")) {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		} else {
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		}
	}
	
	/**
	 * Method to get lessons that satisfy the introduced parameters
	 * It uses a Parameterized Query to prevent SQL injection
	 * @param Name beginning
	 * @param Value to obtain lessons whose available places are under or equal to it
	 * @param Value to obtain lessons whose available places are over or equal to it
	 * @param Value to obtain lessons whose beginning hour are before or equal to it
	 * @param Value to obtain lessons whose beginning hour are after or equal to it
	 * @param Value to obtain lessons whose ending hour are before or equal to it
	 * @param Value to obtain lessons whose ending hour are after or equal to it
	 * @return List<Lesson> with the selected lessons
	 * @return List<Lesson> with every lesson if every @param is blank
	 */
	public List<Lesson> generalFilters(String name, Integer availablePlacesUnder, Integer availablePlacesOver, Integer begginingHourUnder, Integer begginingHourOver,Integer endingHourUnder, Integer endingHourOver){
		String queryString = "Select l FROM Lesson l WHERE ";
		boolean empty = true;
		if (!name.equals("")) {
			queryString+= "name LIKE :name AND ";
			empty = false;
		}
		if (availablePlacesUnder != null) {
			queryString+= "availablePlaces <= :availablePlacesUnder AND " ;
			empty = false;
		}
		if (availablePlacesOver != null) {
			queryString+= "availablePlaces >= :availablePlacesOver AND ";
			empty = false;
		}
		if (begginingHourUnder != null) {
			queryString+= "begginingHour <= :begginingHourUnder AND ";
			empty = false;
		}
		if (begginingHourOver != null) {
			queryString+="begginingHour >= :begginingHourOver AND ";
			empty = false;
		}
		if (endingHourUnder != null) {
			queryString+= "endingHour <= :endingHourUnder AND ";
			empty = false;
		}
		if (endingHourOver != null) {
			queryString+="endingHour >= :endingHourOver AND ";
			empty = false;
		}
		if (empty) {
			queryString = queryString.substring(0,queryString.length()-7);
		} else {
			queryString= queryString.substring(0, queryString.length()-4);

		}
		
		TypedQuery<Lesson> query2 = entityManager.createQuery(queryString, Lesson.class);
		
		if (!name.equals("")) {query2.setParameter("name", name+"%");}
		if (availablePlacesUnder != null) {query2.setParameter("availablePlacesUnder", availablePlacesUnder);}
		if (availablePlacesOver != null) {query2.setParameter("availablePlacesOver", availablePlacesOver);}
		if (begginingHourUnder != null) {query2.setParameter("begginingHourUnder", begginingHourUnder);}
		if (begginingHourOver != null) {query2.setParameter("begginingHourOver", begginingHourOver);}
		if (endingHourUnder != null) {query2.setParameter("endingHourUnder", endingHourUnder);}
		if (endingHourOver != null) {query2.setParameter("endingHourOver", endingHourOver);}
	
		return query2.getResultList();
	}
}
