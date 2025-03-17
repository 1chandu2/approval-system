package org.example.service;

import org.example.dao.UserRepository;
import org.example.dto.UserResponse;
import org.example.dto.UserSignUpRequest;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(UserSignUpRequest userSignUpRequest) {
        User user = new User(userSignUpRequest.getFirstName(),
                userSignUpRequest.getLastName(),
                userSignUpRequest.getEmailId(),
                userSignUpRequest.getUserName(),
                userSignUpRequest.getPassword());

        userRepository.save(user);
    }

    public List<UserResponse> getAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponse(user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmailId()))
                .toList();
    }

    public User getUserByUserName(String userName) {
       return userRepository.findByUserName(userName).orElseThrow(()-> new RuntimeException("User not found"));
    }
}
