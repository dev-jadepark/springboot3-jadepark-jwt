package dev.jadepark.springboot3jadeparkjwt.repository;

import dev.jadepark.springboot3jadeparkjwt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findById(Long userId);
    Optional<User> findByEmail(String email);
    User save(User user);
}
