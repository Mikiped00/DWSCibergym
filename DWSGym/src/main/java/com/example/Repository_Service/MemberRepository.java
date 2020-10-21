package com.example.Repository_Service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Member.Member;


public interface MemberRepository extends JpaRepository<Member, Long>{
	Member findByName(String name);
	Member findByEmail(String email);
}
