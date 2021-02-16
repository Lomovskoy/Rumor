package ru.social.network.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.social.network.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByActivationCode(String code);
}
