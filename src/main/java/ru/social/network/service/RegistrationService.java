package ru.social.network.service;

import org.springframework.ui.Model;
import ru.social.network.model.User;
import ru.social.network.model.dto.CaptchaResponseDto;

public interface RegistrationService {

    void checkPassword1(String passwordConfirm, User user, Model model);

    void checkPassword2(String passwordConfirm, Model model);

    void checkCaptcha(Model model, CaptchaResponseDto response);
}
