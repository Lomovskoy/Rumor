package ru.social.network.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.social.network.enums.Role;
import ru.social.network.mapings.ErrorMessage;
import ru.social.network.mapings.ViewVariables;
import ru.social.network.model.User;
import ru.social.network.model.dto.CaptchaResponseDto;
import ru.social.network.repository.UserRepository;
import ru.social.network.service.MessageService;
import ru.social.network.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageService messageService;

    UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, MessageService messageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.messageService = messageService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("Username Not Found");
        return user;
    }

    @Override
    public boolean addUser(User user) {
        var userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) return false;

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        messageService.sendMessage(user);
        return true;
    }

    @Override
    public boolean activateUser(String code) {
        var user = userRepository.findByActivationCode(code);

        if (user == null) return false;

        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);

        var roles = Arrays.stream(Role.values())
                .map(Role::name).collect(Collectors.toSet());

        user.getRoles().clear();

        for (var key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);
    }

    @Override
    public void updateProfile(User user, String password, String email) {
        var userEmail = user.getEmail();

        var isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));

        if (isEmailChanged) {
            user.setEmail(email);

            if (email != null && !email.isEmpty()) {
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }

        if (!password.isEmpty()) {
            user.setPassword(password);
        }

        userRepository.save(user);

        if (isEmailChanged) {
            messageService.sendMessage(user);
        }
    }

    @Override
    public void subscribe(User currentUser, User user) {
        user.getSubscribers().add(currentUser);
        userRepository.save(user);
    }

    @Override
    public void unsubscribe(User currentUser, User user) {
        user.getSubscribers().remove(currentUser);
        userRepository.save(user);
    }
}
