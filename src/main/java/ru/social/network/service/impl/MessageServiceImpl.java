package ru.social.network.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.social.network.model.Message;
import ru.social.network.model.User;
import ru.social.network.model.dto.MessageDto;
import ru.social.network.repository.MessageRepository;
import ru.social.network.service.MailSenderService;
import ru.social.network.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MailSenderService mailSender;
    private final String hostname;

    public MessageServiceImpl(MessageRepository messageRepository, MailSenderService mailSender,
                              @Value("${myhostname}") String hostname) {
        this.messageRepository = messageRepository;
        this.mailSender = mailSender;
        this.hostname = hostname;
    }

    @Override
    public void sendMessage(User user) {
        if (!user.getEmail().isEmpty()) {
            var message = String.format(
                    "Привет, %s! \nЭто письмо активации аккаунта в Rumor. " +
                            "Перейдите по следующей ссылке : http://%s/activate/%s \n для активации",
                    user.getUsername(),
                    hostname,
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Активация аккаунта", message);
        }
    }

    @Override
    public Page<MessageDto> messageList(Pageable pageable, String filter, User user) {
        if (filter != null && !filter.isEmpty()) {
            return messageRepository.findByTag(filter, pageable, user);
        } else {
            return messageRepository.findAll(pageable, user);
        }
    }

    @Override
    public Page<MessageDto> messageListForUser(Pageable pageable, User currentUser, User author) {
        return messageRepository.findByUser(pageable, author, currentUser);
    }

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }

    @Override
    public Iterable<Message> findAll(){
        return messageRepository.findAll();
    }
}