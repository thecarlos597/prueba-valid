package com.prueba.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.entity.User;

public interface UserRepository extends JpaRepository<User,Long>{

}
