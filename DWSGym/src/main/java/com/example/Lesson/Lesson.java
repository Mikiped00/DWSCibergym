package com.example.Lesson;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.example.Member.Member;

@Entity
public class Lesson {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "membersLesson")
	private List<Member> reservedMember;
	
	private String name;
	private String description;
	private int availablePlaces;
	private int begginingHour;
	private int begginingMinutes;
	private int endingHour;
	private int endingMinutes;
	
	public Lesson() {}
	
	public Lesson(String name, String description, int availablePlaces, int begginingHour, int begginingMinutes, int endingHour, int endingMinutes) {
		this.name = name;
		this.description = description;
		this.availablePlaces = availablePlaces;
		this.begginingHour = begginingHour;
		this.begginingMinutes = begginingMinutes;
		this.endingHour = endingHour;
		this.endingMinutes = endingMinutes;
		this.reservedMember = new ArrayList<Member>();
	}

	
	public List<Member> getReservedMember() {
		return reservedMember;
	}


	public void setReservedMember(List<Member> reservedMember) {
		this.reservedMember = reservedMember;
	}

	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAvailablePlaces() {
		return availablePlaces;
	}

	public void setAvailablePlaces(int availablePlaces) {
		this.availablePlaces = availablePlaces;
	}

	public int getBegginingHour() {
		return begginingHour;
	}

	public void setBegginingHour(int begginingHour) {
		this.begginingHour = begginingHour;
	}

	public int getBegginingMinutes() {
		return begginingMinutes;
	}

	public void setBegginingMinutes(int begginingMinutes) {
		this.begginingMinutes = begginingMinutes;
	}

	public int getEndingHour() {
		return endingHour;
	}

	public void setEndingHour(int endingHour) {
		this.endingHour = endingHour;
	}

	public int getEndingMinutes() {
		return endingMinutes;
	}

	public void setEndingMinutes(int endingMinutes) {
		this.endingMinutes = endingMinutes;
	}

}
