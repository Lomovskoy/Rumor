package ru.social.network.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ru.social.network.mapings.ErrorMessage;
import ru.social.network.mapings.Url;
import ru.social.network.mapings.ViewPages;
import ru.social.network.mapings.ViewVariables;
import ru.social.network.model.User;
import ru.social.network.model.dto.CaptchaResponseDto;
import ru.social.network.service.RegistrationService;
import ru.social.network.service.UserService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Objects;

@Controller
public class RegistrationController {

    private final String secret;
    private final RestTemplate restTemplate;
    private final UserService userService;
    private final RegistrationService registrationService;

    public RegistrationController(@Value("${recaptcha.secret}") String secret,
                                  RestTemplate restTemplate, UserService userService, RegistrationService registrationService) {
        this.secret = secret;
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.registrationService = registrationService;
    }

    @GetMapping(Url.REGISTRATION)
    public String registration() {
        return ViewPages.REGISTRATION;
    }

    @PostMapping(Url.REGISTRATION)
    public String addUser(@RequestParam(ViewVariables.PASSWORD_2) String passwordConfirm,
                          @RequestParam(ViewVariables.G_RECAPTCHA) String captchaResponse,
                          @Valid User user, BindingResult bindingResult, Model model) {


        var url = String.format(Url.CAPTCHA_URL, secret, captchaResponse);
        var response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        registrationService.checkCaptcha(model, response);
        registrationService.checkPassword2(passwordConfirm, model);
        registrationService.checkPassword1(passwordConfirm, user, model);

        if (passwordConfirm.isEmpty() || bindingResult.hasErrors() || !Objects.requireNonNull(response).isSuccess()) {
            var errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return ViewPages.REGISTRATION;
        }

        if (!userService.addUser(user)) {
            model.addAttribute(ViewVariables.USER_NAME_ERROR, ErrorMessage.USER_NAME_ERROR);
            return ViewPages.REGISTRATION;
        }

        return Url.REDIRECT + Url.LOGIN;
    }

    @GetMapping(Url.ACTIVATE + "{code}")
    public String activate(Model model, @PathVariable String code) {

        if (userService.activateUser(code)) {
            model.addAttribute(ViewVariables.MESSAGE_TYPE, ErrorMessage.SUCCESS);
            model.addAttribute(ViewVariables.MESSAGE, ErrorMessage.SUCCESS_MESSAGE);
        } else {
            model.addAttribute(ViewVariables.MESSAGE_TYPE, ErrorMessage.DANGER);
            model.addAttribute(ViewVariables.MESSAGE, ErrorMessage.DANGER_MESSAGE);
        }

        return ViewPages.LOGIN;
    }
}
