package com.example.ese21.Services;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    @Value("${fileRepositoryFolder}")
    private String fileRepositoryFolder;

    public String upload(MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID().toString();
        String completeFileName = newFileName + "." + extension;
        Path finalFolder = Paths.get(fileRepositoryFolder);
        if(!Files.exists(finalFolder) || !Files.isDirectory(finalFolder))
            throw new IOException("Final folder not found");

        Path finalDestination = finalFolder.resolve(completeFileName);
        if(Files.exists(finalDestination))
            throw new IOException("File name already exists");

        file.transferTo(finalDestination.toFile());
        return completeFileName;
    }
    public byte[] download(String fileName)throws IOException{
        File fileFromRepository = new File(fileRepositoryFolder + "\\" + fileName);
        if(!fileFromRepository.exists()) throw new IOException("File does not exist");
        return IOUtils.toByteArray(new FileInputStream(fileFromRepository));
    }

    public void removeFile(String fileName) throws Exception {
        File fileFromRepository = new File(fileRepositoryFolder + "\\" + fileName);
        if(!fileFromRepository.delete()) throw new Exception("File not deleted");
    }
}
