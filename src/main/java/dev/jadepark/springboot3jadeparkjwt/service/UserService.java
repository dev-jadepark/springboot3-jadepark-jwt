package dev.jadepark.springboot3jadeparkjwt.service;

import dev.jadepark.springboot3jadeparkjwt.domain.User;
import dev.jadepark.springboot3jadeparkjwt.dto.AddUserRequest;
import dev.jadepark.springboot3jadeparkjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    //private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Unexpected userId : " + userId));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Unexpected email : " + email));
    }

    public Long save(AddUserRequest addUserRequest) {
        User user = userRepository.save(new User(addUserRequest.getEmail(), addUserRequest.getPassword()));
        return user.getId();
    }
}
