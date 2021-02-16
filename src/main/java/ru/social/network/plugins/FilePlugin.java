package ru.social.network.plugins;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.io.File;

public interface FilePlugin {

    void putFile(File file);

    S3ObjectInputStream getFile(String fileName);
}
