package org.markhan.springboot.thymeleafdemo.dao;

import java.util.Optional;

import org.markhan.springboot.thymeleafdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findUserByUsername(String username);
}
