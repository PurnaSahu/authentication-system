package com.pbs.root.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pbs.root.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer>{
	public boolean existsByEmail(String email);
}
