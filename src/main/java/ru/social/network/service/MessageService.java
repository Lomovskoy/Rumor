package ru.social.network.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.social.network.model.Message;
import ru.social.network.model.User;
import ru.social.network.model.dto.MessageDto;

public interface MessageService {

    void sendMessage(User user);

    Page<MessageDto> messageList(Pageable pageable, String filter, User user);

    Page<MessageDto> messageListForUser(Pageable pageable, User currentUser, User author);

    void save(Message message);

    Iterable<Message> findAll();
}
