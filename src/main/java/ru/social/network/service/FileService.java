package ru.social.network.service;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.web.multipart.MultipartFile;
import ru.social.network.model.Message;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public interface FileService {

    default boolean checkFile(MultipartFile file) {
        return file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty();
    }

    default boolean checkContentType(MultipartFile file) {
        return Objects.equals(file.getContentType(), ".png") || Objects.equals(file.getContentType(), ".jpg")
                || Objects.equals(file.getContentType(), ".jpeg");
    }

    default String getResultFilename(MultipartFile multipartFile) {
        return UUID.randomUUID().toString() + "." + multipartFile.getOriginalFilename();
    }

    void saveFile(Message message, MultipartFile multipartFile) throws IOException;

    S3ObjectInputStream getFile(String fileName);
}
