package com.java.filestorageapi.service;

import com.java.filestorageapi.model.FileEntity;
import com.java.filestorageapi.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public FileEntity uploadFile(MultipartFile file) throws IOException {
        if (file.getSize() > 5 * 1024 * 1024) { // 5MB
            throw new IllegalArgumentException("File size exceeds the limit.");
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) {
            throw new IllegalArgumentException("Invalid file name.");
        }

        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        if (!isValidExtension(extension)) {
            throw new IllegalArgumentException("Invalid file extension.");
        }

        String uniqueFileName = generateUniqueFileName(extension);

        String storagePath = "src/main/resources/static/sample-files" + uniqueFileName;
        Path storageLocation = Paths.get(storagePath);
        Files.write(storageLocation, file.getBytes());

        FileEntity newFile = new FileEntity(originalFileName, storagePath, file.getSize(), extension, file.getBytes());
        return fileRepository.save(newFile);
    }

    private boolean isValidExtension(String extension) {
        String[] validExtensions = {"png", "jpeg", "jpg", "docx", "pdf", "xlsx"};
        for (String validExtension : validExtensions) {
            if (validExtension.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    private String generateUniqueFileName(String extension) {
        return UUID.randomUUID().toString() + "." + extension;
    }


    public List<FileEntity> listFiles() {
        return fileRepository.findAll();
    }

    public byte[] downloadFileContent(Long fileId) throws IOException {
        Optional<FileEntity> fileOptional = fileRepository.findById(fileId);
        if (!fileOptional.isPresent()) {
            throw new IllegalArgumentException("File not found");
        }

        FileEntity fileEntity = fileOptional.get();

        Path filePath = Paths.get(fileEntity.getPath());

        if (!Files.exists(filePath)) {
            throw new IOException("File not found on the server.");
        }

        return Files.readAllBytes(filePath);
    }

    public void updateFile(Long fileId, FileEntity updatedFile) throws IOException {
        FileEntity existingFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("File not found"));

        existingFile.setName(updatedFile.getName());
        existingFile.setExtension(updatedFile.getExtension());

        if (updatedFile.getContent() != null) {
            String filePath = existingFile.getPath();

            Files.write(Paths.get(filePath), updatedFile.getContent(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            existingFile.setSize((long) updatedFile.getContent().length);
        }

        fileRepository.save(existingFile);
    }






    public void deleteFile(Long fileId) throws IOException {
        Optional<FileEntity> fileOptional = fileRepository.findById(fileId);
        if (!fileOptional.isPresent()) {
            throw new IllegalArgumentException("File not found");
        }

        FileEntity fileEntity = fileOptional.get();
        String filePath = fileEntity.getPath();

        Path fileLocation = Paths.get(filePath);
        if (Files.exists(fileLocation)) {
            Files.delete(fileLocation);
        }

        fileRepository.delete(fileEntity);
    }
}
