package ru.social.network.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.social.network.mapings.Url;
import ru.social.network.service.FileService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(Url.IMG + "{fileName}")
    public void greeting(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        var stream =  fileService.getFile(fileName);
        FileCopyUtils.copy(stream, response.getOutputStream());
    }
}
