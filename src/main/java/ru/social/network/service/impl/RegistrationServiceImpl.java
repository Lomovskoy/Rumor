package ru.social.network.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.social.network.mapings.ErrorMessage;
import ru.social.network.mapings.ViewVariables;
import ru.social.network.model.User;
import ru.social.network.model.dto.CaptchaResponseDto;
import ru.social.network.service.RegistrationService;

@Service
public class RegistrationServiceImpl implements RegistrationService {


    @Override
    public void checkPassword1(String passwordConfirm, User user, Model model) {
        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute(ViewVariables.PASSWORD_1_ERROR, ErrorMessage.PASSWORD_1_ERROR);
        }
    }

    @Override
    public void checkPassword2(String passwordConfirm, Model model) {
        if (passwordConfirm.isEmpty()) {
            model.addAttribute(ViewVariables.PASSWORD_2_ERROR, ErrorMessage.PASSWORD_2_ERROR);
        }
    }

    @Override
    public void checkCaptcha(Model model, CaptchaResponseDto response) {
        if (response != null && !response.isSuccess()) {
            model.addAttribute(ViewVariables.CAPTCHA_ERROR, ErrorMessage.CAPTCHA_ERROR);
        }
    }
}
