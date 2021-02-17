package ru.social.network.service.impl;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.social.network.model.Message;
import ru.social.network.service.FileService;

import javax.validation.ValidationException;
import java.io.File;
import java.io.IOException;

@Service
@ConditionalOnProperty(value = "file.store.impl", havingValue = "local", matchIfMissing = true)
public class FileServiceImpl implements FileService {
    private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);

    private final String uploadPath;

    public FileServiceImpl(@Value("${upload.path}") String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Override
    public void saveFile(Message message, MultipartFile multipartFile) throws IOException {
        LOG.info("saveFile: run");
        if (checkFile(multipartFile)) {
            if (checkContentType(multipartFile)){
                var uploadDir = new File(uploadPath);

                if (!uploadDir.exists())
                    if (!uploadDir.mkdir())
                        throw new IOException(String.format("Не удалось создать директорию [%s]для файла", uploadPath));

                var resultFilename = getResultFilename(multipartFile);

                multipartFile.transferTo(new File(uploadPath + File.separator + resultFilename));
                message.setFilename(resultFilename);
            } else {
                throw new ValidationException("Файл не может быть с расширением " + multipartFile.getContentType());
            }
        }
    }

    @Override
    public S3ObjectInputStream getFile(String fileName) {
        return null;
    }
}
