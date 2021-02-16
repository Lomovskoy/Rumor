package ru.social.network.service;

import org.springframework.ui.Model;
import ru.social.network.model.User;
import ru.social.network.model.dto.CaptchaResponseDto;

import java.util.List;
import java.util.Map;

public interface UserService {

    boolean addUser(User user);

    boolean activateUser(String code);

    List<User> findAll();

    void saveUser(User user, String username, Map<String, String> form);

    void updateProfile(User user, String password, String email);

    void subscribe(User currentUser, User user);

    void unsubscribe(User currentUser, User user);
}