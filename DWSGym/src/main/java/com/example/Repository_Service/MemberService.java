package com.example.Repository_Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Lesson.Lesson;
import com.example.Member.Member;
import com.example.Member.MemberComponent;

@Service
public class MemberService {

	@Autowired
	private LessonRepository lessonRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private MemberComponent memberComponent;
	
	/**
	 * Initializes the members in the repository
	 */
	public void initMember() {
		if (this.memberRepository.findAll().isEmpty()) {
			memberRepository.save(new Member("01010101H", "Gonzalo", "tabit@gmail.com","Tabit2000","ROLE_ADMIN"));
			memberRepository.save(new Member("01234567S", "Miki", "miki@gmail.com","Miki2000","ROLE_ADMIN"));
			memberRepository.save(new Member("12341234S", "Javi", "javi@gmail.com","Javi2000","ROLE_ADMIN"));
			memberRepository.save(new Member("13571357A", "Nicolas", "nicolas.rodriguez@urjc.es","Nico2000","ROLE_USER"));
			memberRepository.save(new Member("24682468J", "Micael", "micael.gallego@urjc.es","Mica2000","ROLE_USER"));
			memberRepository.save(new Member("987278987U", "Patxi", "francisco.gortazar@urjc.es","Patxi2000","ROLE_USER"));
		}
	}
	
	/**
	 * Method to add a member to the repository
	 * @param member
	 * @return the added member
	 */
	public ResponseEntity<Member> newMember(Member member) {
		if (this.memberRepository.findByEmail(member.getEmail()) != null) {
			member.setPasswordHash("");
			member.setRoles(new ArrayList<String>());
			return new ResponseEntity<>(member, HttpStatus.IM_USED);
		} else {
			this.memberRepository.save(member);
			member.setPasswordHash("");
			member.setRoles(new ArrayList<String>());
			return new ResponseEntity<>(member, HttpStatus.OK);
		}
	}
	
	/**
	 * Method to get a member by its name
	 * @param Member's name
	 * @return
	 */
	public ResponseEntity<Member> getMemberByName(String name){
		if (this.memberRepository.findByName(name) == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(this.memberRepository.findByName(name), HttpStatus.OK);
		}
	}
	
	/**
	 * Method to get a member by its email
	 * @param Member's email
	 * @return
	 */
	public ResponseEntity<Member> getMemberByEmail(String email){
		if (this.memberRepository.findByEmail(email) == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(this.memberRepository.findByEmail(email), HttpStatus.OK);
		}
	}
	
	/**
	 * Method to get all the members with a TypedQuery
	 * @return List<Member>
	 */
	public List<Member> getAllMembers(){
		TypedQuery<Member> q1 = entityManager.createQuery("SELECT m FROM Member m", Member.class);
		List<Member> allMembers = new ArrayList<Member>();
		for(Member member : q1.getResultList()) {
			member.setPasswordHash("");
			member.setRoles(new ArrayList<String>());
			allMembers.add(member);
		}
		return allMembers;
	}
 	
	/**
	 * Method to get all the lessons, used as an auxiliary method for reserving a lesson.
	 * It is used in MemberController to generate the reserving lesson template.
	 * @return List<Lesson>
	 */
	public List<Lesson> getAllLessons(){
		return this.lessonRepository.findAll();
	}
	
	/**
	 * Method to get the member associated to the introduced @param id
	 * @param Member's id 
	 * @return selected Member and HttpStatus.OK if member exists
	 * @return HttpStatus.NOT_FOUND if member doesn't exist
	 */
	public ResponseEntity<Member> getMember(long id){
		if(this.memberRepository.existsById(id)) {
			Member member = this.memberRepository.getOne(id);
			member.setPasswordHash("");
			member.setRoles(new ArrayList<String>());
			return new ResponseEntity<>(member, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Method to update the member associated to the introduced @param id
	 * If any field is blank, the previous value is kept
	 * @param Member's id 
	 * @param New DNI
	 * @param New name
	 * @return updated Member and HttpStatus.OK if member already existed
	 * @return HttpStatus.NOT_MODIFIED if every input is blank
	 * @return HttpStatus.NOT_FOUND if member doesn't exist
	 */
	public ResponseEntity<Member> updateMember(long id, String dni, String name){
		if(this.memberRepository.existsById(id)) {
			if (dni.equals("") && name.equals("")) {
				return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
			} else {
				Member auxMember = this.memberRepository.getOne(id);
				if (!dni.equals("")) auxMember.setDni(dni);
				if (!name.equals("")) auxMember.setName(name);
				this.memberRepository.saveAndFlush(auxMember);
				memberComponent.setLoggedMember(auxMember);
				auxMember.setPasswordHash("");
				auxMember.setRoles(new ArrayList<String>());
				return new ResponseEntity<>(auxMember, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Method to change a member's password
	 * @param Member's id
	 * @param New Password
	 * @param Repeated new password
	 * @return HttpStatus.OK if password is changed successfully
	 * @return HttpStatus.NOT_MODIFIED if passwords don't match
	 * @return HttpStatus.BAD_REQUEST if any input is blank
	 */
	public ResponseEntity<Member> changePassword(long id, String newPass, String newPassRepeated){
		if (newPass.equals("") || newPassRepeated.equals("")) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			if (newPass.equals(newPassRepeated)) {
				Member passModifiedMember = this.memberRepository.getOne(id);
				passModifiedMember.setPasswordHash(new BCryptPasswordEncoder().encode(newPass));
				this.memberRepository.saveAndFlush(passModifiedMember);
				memberComponent.setLoggedMember(passModifiedMember);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
			}
		}

	}
	
	/**
	 * Method to delete the member associated to the introduced @param id
	 * @param Member's id
	 * @return HttpStatus.OK if member existed
	 * @return HttpStatus.NOT_FOUND if member doesn't exist
	 */
	public ResponseEntity<Member> deleteMember(long id){
		if(this.memberRepository.existsById(id)) {
			this.memberRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Method to add a reservation of a Lesson
	 * @param Member's id
	 * @param Lesson's id
	 * @return Selected member and HttpStatus.OK if both member and lesson exists,
	 * there are available places and the member hasn't got any lesson reserved yet 
	 * @return HttpStatus.INSUFFICIENT_STORAGE if the lesson doesn't have any available places
	 * @return HttpStatus.TOO_MANY_REQUESTS if the member has already reserved a lesson
	 * @return HttpStatus.NOT_FOUND if the member doesn't exist
	 */
	public ResponseEntity<Member> addReserve (long memberId, long lessonId) {
		if (this.memberRepository.existsById(memberId)) {
			Member auxMember = this.memberRepository.getOne(memberId);
			if (auxMember.getMembersLesson() == null) {
				Lesson auxLesson = this.lessonRepository.getOne(lessonId);
				if (auxLesson.getAvailablePlaces() >= 1) {
					auxLesson.setAvailablePlaces(auxLesson.getAvailablePlaces() - 1);			
					auxMember.setMembersLesson(auxLesson);
					auxLesson.getReservedMember().add(auxMember);
					this.lessonRepository.saveAndFlush(auxLesson);
					this.memberRepository.saveAndFlush(auxMember);
					memberComponent.setLoggedMember(auxMember);
					auxMember.setPasswordHash("");
					auxMember.setRoles(new ArrayList<String>());
					return new ResponseEntity<>(auxMember, HttpStatus.OK);
				} else {return new ResponseEntity<>(HttpStatus.INSUFFICIENT_STORAGE);}
			} else {return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);}
		} else {return new ResponseEntity<>(HttpStatus.NOT_FOUND);}
		
	}

	/**
	 * Method to delete a reservation of a Lesson
	 * @param Member's id
	 * @return Selected member and HttpStatus.OK if the reservation is deleted successfully
	 * @return HttpStatus.NO_CONTENT if there isn't any reservation yet
	 * @return HttpStatus.NOT_FOUND if the member doesn't exist
	 */
	public ResponseEntity<Member> cancelReserve (long memberId){
		if (this.memberRepository.existsById(memberId)) {
			Member auxMember = this.memberRepository.getOne(memberId);
			Lesson auxLesson = auxMember.getMembersLesson();
			if (auxLesson!=null) {
				auxLesson.setAvailablePlaces(auxLesson.getAvailablePlaces() + 1);			
				auxMember.setMembersLesson(null);
				auxLesson.getReservedMember().remove(auxMember);
				this.lessonRepository.saveAndFlush(auxLesson);
				this.memberRepository.saveAndFlush(auxMember);
				memberComponent.setLoggedMember(auxMember);
				auxMember.setPasswordHash("");
				auxMember.setRoles(new ArrayList<String>());
				return new ResponseEntity<>(auxMember,HttpStatus.OK);
			} else {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Filter to get members by name beginning
	 * @param name beginning
	 * @return Collection<Member> with the members whose name starts with the @param name
	 */
	public Collection<Member> getBegginingMemberName(String name){
		Collection<Member> listCollection = new ArrayList<>();
		Collection<Member> registeredMembers = this.memberRepository.findAll();
		for(Member Member : registeredMembers) {
			if (Member.getName().startsWith(name)) {
				listCollection.add(Member);
			}
		}	
		return listCollection;
	}
}
