package ru.social.network.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    private static final Logger LOG = LoggerFactory.getLogger(MailConfig.class);
    private final static String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
    private final static String MAIL_DEBUG= "mail.debug";

    private final String host;
    private final String username;
    private final String password;
    private final int port;
    private final String protocol;
    private final String debug;

    public MailConfig(@Value("${spring.mail.host}") String host, @Value("${mail.username}") String username,
                      @Value("${mail.password}") String password, @Value("${spring.mail.port}") int port,
                      @Value("${spring.mail.protocol}") String protocol, @Value("${mail.debug}") String debug) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
        this.protocol = protocol;
        this.debug = debug;
    }

    @Bean
    public JavaMailSender getMailSender() {
        var mailSender = new JavaMailSenderImpl();
        LOG.info("JavaMailSender: host={} port={} username={} password={}", host, port, username, password);
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        var properties = mailSender.getJavaMailProperties();

        properties.setProperty(MAIL_TRANSPORT_PROTOCOL, protocol);
        properties.setProperty(MAIL_DEBUG, debug);

        return mailSender;
    }
}
