package it.epicode.PrenotazioneEvento.service;


import it.epicode.PrenotazioneEvento.model.User;
import it.epicode.PrenotazioneEvento.model.UserRequest;
import it.epicode.PrenotazioneEvento.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User registerUser(UserRequest newUser) {
        Optional<User> existingUser = userRepository.findByUsername(newUser.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("Username already taken");
        }
        existingUser = userRepository.findByEmail(newUser.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("Email already taken");
        }
        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setEmail(newUser.getEmail());
        user.setNome(newUser.getNome());
        user.setCognome(newUser.getCognome());
        user.setRuolo(User.Role.USER);
        return userRepository.save(user);


    }

    public User updateUserDetails(Long userId, User updatedUser) {
        return userRepository.findById(userId).map(user -> {
            user.setNome(updatedUser.getNome());
            user.setCognome(updatedUser.getCognome());
            user.setEmail(updatedUser.getEmail());

            return userRepository.save(user);
        }).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }

    public void changeUserPassword(Long userId, String newPassword) {
        userRepository.findById(userId).map(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(user);
        }).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }


    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
