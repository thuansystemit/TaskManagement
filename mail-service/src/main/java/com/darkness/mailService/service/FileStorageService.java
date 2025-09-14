package com.darkness.mailService.service;

import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
/**
 * @author darkness
 **/
@Service
public class FileStorageService {
    private final Path root = Paths.get("uploads");

    public FileStorageService() throws IOException {
        if (!Files.exists(root)) Files.createDirectories(root);
    }

    public String saveFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = root.resolve(filename);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return path.toString();
    }

    public Resource loadFile(String path) throws MalformedURLException {
        Path filePath = Paths.get(path);
        return new UrlResource(filePath.toUri());
    }
}
