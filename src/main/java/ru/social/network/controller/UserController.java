package ru.social.network.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.social.network.enums.Role;
import ru.social.network.mapings.Url;
import ru.social.network.mapings.ViewPages;
import ru.social.network.mapings.ViewVariables;
import ru.social.network.model.User;
import ru.social.network.service.UserService;

import java.util.Map;

@Controller
@RequestMapping(Url.USER)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userList(Model model) {
        model.addAttribute(ViewVariables.USERS, userService.findAll());
        return ViewPages.USER_LIST;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute(ViewVariables.USER, user);
        model.addAttribute(ViewVariables.ROLES, Role.values());
        return ViewPages.USER_EDIT;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(@RequestParam String username, @RequestParam Map<String, String> form,
                           @RequestParam(ViewVariables.USER_ID) User user) {
        userService.saveUser(user, username, form);
        return Url.REDIRECT + Url.USER;
    }

    @GetMapping(Url.PROFILE)
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute(ViewVariables.USER_NAME, user.getUsername());
        model.addAttribute(ViewVariables.EMAIL, user.getEmail());
        return ViewPages.PROFILE;
    }

    @PostMapping(Url.PROFILE)
    public String updateProfile(@AuthenticationPrincipal User user, @RequestParam String password,
                                @RequestParam String email) {
        userService.updateProfile(user, password, email);
        return Url.REDIRECT + Url.USER_PROFILE;
    }

    @GetMapping(Url.SUBSCRIBE + "{user}")
    public String subscribe(@AuthenticationPrincipal User currentUser, @PathVariable User user) {
        userService.subscribe(currentUser, user);
        return Url.REDIRECT + Url.USER_MESSAGES + user.getId();
    }

    @GetMapping(Url.UNSUBSCRIBE + "{user}")
    public String unsubscribe(@AuthenticationPrincipal User currentUser, @PathVariable User user) {
        userService.unsubscribe(currentUser, user);
        return Url.REDIRECT + Url.USER_MESSAGES + user.getId();
    }

    @GetMapping("{type}/{user}" + Url.LIST)
    public String userList(Model model, @PathVariable User user, @PathVariable String type) {
        model.addAttribute(ViewVariables.USER_CHANNEL, user);
        model.addAttribute(ViewVariables.TYPE, type);

        if (ViewVariables.SUBSCRIPTIONS.equals(type))
            model.addAttribute(ViewVariables.USERS, user.getSubscriptions());
        else
            model.addAttribute(ViewVariables.USERS, user.getSubscribers());

        return ViewPages.SUBSCRIPTIONS;
    }
}
