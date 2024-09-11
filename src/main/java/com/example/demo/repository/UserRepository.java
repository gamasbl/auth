package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, String>{

	@Query(value = "SELECT * FROM users WHERE username=?",nativeQuery = true)
    public Users findByUsername(String username);
}
