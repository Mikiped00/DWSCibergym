package com.example.Member;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.Lesson.Lesson;

@Entity
public class Member {
	private static final float FEE = 40;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String passwordHash;
	private String dni;
	private String name;
	private String email;
	private float fee;
	
	@ElementCollection(fetch= FetchType.EAGER)
	private List<String> roles;
	
	//@JsonIgnore
	@ManyToOne
	private Lesson membersLesson;
	
	public Member(){
		this.fee = Member.FEE;
	}
	
	public Member(String dni, String name, String email, String passwordHash, String... roles) {
		this.dni = dni;
		this.name = name;
		this.email = email;
		this.fee = Member.FEE;
		this.passwordHash = new BCryptPasswordEncoder().encode(passwordHash);
		this.roles = new ArrayList<String>(Arrays.asList(roles));
	}
	
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Lesson getMembersLesson() {
		return membersLesson;
	}

	public void setMembersLesson(Lesson membersLesson) {
		this.membersLesson = membersLesson;
	}

	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public float getFee() {
		return fee;
	}
	public void setFee(float fee) {
		this.fee = fee;
	}
	
}
