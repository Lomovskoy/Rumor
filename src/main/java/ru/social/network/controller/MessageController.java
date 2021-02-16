package ru.social.network.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import ru.social.network.mapings.Url;
import ru.social.network.mapings.ViewPages;
import ru.social.network.mapings.ViewVariables;
import ru.social.network.model.Message;
import ru.social.network.model.User;
import ru.social.network.service.FileService;
import ru.social.network.service.MessageService;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class MessageController {

    private final MessageService messageService;
    private final FileService fileService;

    public MessageController(MessageService messageService, FileService fileService) {
        this.messageService = messageService;
        this.fileService = fileService;
    }

    @GetMapping(Url.GREETING)
    public String greeting() {
        return ViewPages.GREETING;
    }

    @GetMapping(Url.ROOT)
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model,
                       @PageableDefault(sort = {ViewVariables.ID}, direction = Sort.Direction.DESC) Pageable pageable,
                       @AuthenticationPrincipal User user) {
        return tape(filter, model, pageable, user);
    }

    @PostMapping(Url.ROOT)
    public String add(@AuthenticationPrincipal User user, @Valid Message message, BindingResult bindingResult,
                      Model model, @RequestParam(value = ViewVariables.FILE, required = false) MultipartFile file) throws IOException {
        return addMain(user, message, bindingResult, model, file);
    }

    @GetMapping(Url.MAIN)
    public String tape(@RequestParam(required = false, defaultValue = "") String filter, Model model,
                       @PageableDefault(sort = {ViewVariables.ID}, direction = Sort.Direction.DESC) Pageable pageable,
                       @AuthenticationPrincipal User user) {

        var page = messageService.messageList(pageable, filter, user);
        model.addAttribute(ViewVariables.PAGE, page);
        model.addAttribute(ViewVariables.URL, Url.MAIN);
        model.addAttribute(ViewVariables.FILTERS, filter);
        return ViewPages.MAIN;
    }

    @PostMapping(Url.MAIN)
    public String addMain(@AuthenticationPrincipal User user, @Valid Message message, BindingResult bindingResult,
                          Model model, @RequestParam(value = ViewVariables.FILE, required = false) MultipartFile file) throws IOException {

        message.setAuthor(user);

        if (bindingResult.hasErrors()) {
            var errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute(ViewVariables.MESSAGE, message);
        } else {
            fileService.saveFile(message, file);
            model.addAttribute(ViewVariables.MESSAGE, null);
            messageService.save(message);
        }

        var messages = messageService.findAll();
        model.addAttribute(ViewVariables.MESSAGES, messages);
        return Url.REDIRECT + Url.MAIN;
    }

    @GetMapping(Url.USER_MESSAGES + "{author}")
    public String userMessages(@AuthenticationPrincipal User currentUser, @PathVariable User author,
                               Model model, @RequestParam(required = false) Message message,
                               @PageableDefault(sort = {ViewVariables.ID}, direction = Sort.Direction.DESC) Pageable pageable) {

        var page = messageService.messageListForUser(pageable, currentUser, author);

        model.addAttribute(ViewVariables.USER_CHANNEL, author);
        model.addAttribute(ViewVariables.SUBSCRIPTIONS_COUNT, author.getSubscriptions().size());
        model.addAttribute(ViewVariables.SUBSCRIBERS_COUNT, author.getSubscribers().size());
        model.addAttribute(ViewVariables.IS_SUBSCRIBER, author.getSubscribers().contains(currentUser));
        model.addAttribute(ViewVariables.PAGE, page);
        model.addAttribute(ViewVariables.MESSAGE, message);
        model.addAttribute(ViewVariables.IS_CURRENT_USER, currentUser.equals(author));
        model.addAttribute(ViewVariables.URL, Url.USER_MESSAGES + author.getId());

        return ViewPages.USER_MESSAGE;
    }

    @PostMapping(Url.USER_MESSAGES + "{user}")
    public String updateMessage(@AuthenticationPrincipal User currentUser, @PathVariable Long user,
                                @RequestParam(ViewVariables.ID) Message message,
                                @RequestParam(ViewVariables.TEXT) String text,
                                @RequestParam(ViewVariables.TAG) String tag,
                                @RequestParam(ViewVariables.FILE) MultipartFile file) throws IOException {

        if (message.getAuthor().equals(currentUser)) {
            if (!text.isEmpty()) message.setText(text);
            if (!tag.isEmpty()) message.setTag(tag);

            fileService.saveFile(message, file);
            messageService.save(message);
        }
        return Url.REDIRECT + Url.USER_MESSAGES + user;
    }

    @GetMapping(Url.MESSAGES + "{message}" + Url.LIKE)
    public String like(@AuthenticationPrincipal User currentUser, @PathVariable Message message,
                       RedirectAttributes redirectAttributes, @RequestHeader(required = false) String referer) {
        var likes = message.getLikes();

        if (likes.contains(currentUser))
            likes.remove(currentUser);
        else
            likes.add(currentUser);

        var components = UriComponentsBuilder.fromHttpUrl(referer).build();
        components.getQueryParams().forEach(redirectAttributes::addAttribute);
        return Url.REDIRECT + components.getPath();
    }
}