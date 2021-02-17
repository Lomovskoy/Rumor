package ru.social.network.service.impl;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.social.network.model.Message;
import ru.social.network.plugins.FilePlugin;
import ru.social.network.service.FileService;
import javax.validation.ValidationException;
import java.io.File;
import java.io.IOException;

@Service
@ConditionalOnProperty(value = "file.store.impl", havingValue = "s3")
public class FileServiceS3Impl implements FileService {
    private static final Logger LOG = LoggerFactory.getLogger(FileServiceS3Impl.class);


    private final FilePlugin filePlugin;
    private final String uploadPath;

    public FileServiceS3Impl(FilePlugin filePlugin, @Value("${upload.path}") String uploadPath) {
        this.filePlugin = filePlugin;
        this.uploadPath = uploadPath;
    }

    @Async
    @Override
    public void saveFile(Message message, MultipartFile multipartFile) throws IOException {
        LOG.info("saveFile: run");
        if (checkFile(multipartFile)) {
            if (checkContentType(multipartFile)){
                var file = new File(uploadPath + File.separator + getResultFilename(multipartFile));
                message.setFilename(file.getName());
                multipartFile.transferTo(file);
                filePlugin.putFile(file);
            } else {
                throw new ValidationException("Файл не может быть с расширением " + multipartFile.getContentType());
            }
        }
    }

    @Async
    @Override
    public S3ObjectInputStream getFile(String fileName) {
        return filePlugin.getFile(fileName);
    }
}
