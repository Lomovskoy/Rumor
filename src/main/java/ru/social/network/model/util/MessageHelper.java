package ru.social.network.model.util;

import ru.social.network.model.User;

public abstract class MessageHelper {

    public static String getAuthorName(User author) {
        return author != null ? author.getUsername() : "<none>";
    }
}